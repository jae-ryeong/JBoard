package com.example.JBoard.jwt.filter;

import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.UserAccountRepository;
import com.example.JBoard.jwt.util.PasswordUtil;
import com.example.JBoard.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 Jwt 인증 필터
 "/user/login" 이외의 URI 요청이 왔을 때 처리하는 필터

 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
 AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청

 1. RefreshToken이 없고, AccessToken이 유효하면 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않으면 -> 인증 실패 처리, 403 ERROR
 3. RefreshToken이 있는 경우 -> DB의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR 방식)
        인증 성공 처리는 하지 않고 실패 처리
 */
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String NO_CHECK_URL = "/user/login"; // "/user/login"으로 들어오는 요청은 Filter 요청 X

    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURL().equals(NO_CHECK_URL)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 사용자 요청 헤더에서 RefreshToken 추출 후
        // RefreshToken이 없거나 유효하지 않다면(DB에 저장된 RefreshToken과 다르다면) null을 반환
        // RefreshToken이 있는 경우는 RefreshToken 비교 후 AccessToken을 재발급
        String refreshToken = jwtService.extractRefreshToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);

        // refreshToken이 존재한다면
        // DB의 refreshToken과 일치하는지 확인 후
        // 일치한다면 AccessToken을 재발급
        if (refreshToken != null) {
            checkRefreshTokenAndReIssueAccessToken(response, refreshToken);
            return;
        }

        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        if (refreshToken == null) {
            checkAccessTokenAndAuthentication(request, response, filterChain);
        }
    }


    public void checkRefreshTokenAndReIssueAccessToken(HttpServletResponse response, String refreshToken) {
        userAccountRepository.findByRefreshToken(refreshToken)  // refreshToken으로 DB에서 유저를 찾고
                .ifPresent(userAccount -> { // 유저가 존재한다면
                    String reIssuedRefreshToken = reIssueRefreshToken(userAccount); // refreshToken을 재발급 후
                    jwtService.sendAccessAndRefreshToken(response, jwtService.createAccessToken(userAccount.getUid()), // refreshToken과 생성한 AccessToken을 응답헤더로 전송
                            reIssuedRefreshToken);
                });
    }

    private String reIssueRefreshToken(UserAccount userAccount) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();  // refreshToken 재발급
        userAccount.updateRefreshToken(reIssuedRefreshToken);
        userAccountRepository.saveAndFlush(userAccount);    // DB에 재발급한 refreshToken을 업데이트 후 Flush
        return reIssuedRefreshToken;
    }

    public void checkAccessTokenAndAuthentication(HttpServletRequest request,
                                                  HttpServletResponse response, FilterChain filterChain) throws  ServletException, IOException{
        log.info("checkAccessTokenAndAuthentication() 호출");
        jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)   // 유효한 토큰인지 검증
                .ifPresent(accessToken -> jwtService.extractUid(accessToken)   // uid 추출
                        .ifPresent(uid -> userAccountRepository.findByUid(uid)   // uid로 유저 반환
                                .ifPresent(this::saveAuthentication))); // 유저 객체를 인증 처리 후 인증 허가 처리된 객체를 SecurityContextHolder에 담기

        filterChain.doFilter(request, response); // 그 후 다음 인증 필터로 진행
    }

    /**
     [인증 허가 메소드]
     파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체

     new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
     UsernamePasswordAuthenticationToken의 파라미터
     1. 위에서 만든 UserDetailsUser 객체 (유저 정보)
     2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
     3. Collection < ? extends GrantedAuthority>로,
     UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
     new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기

     SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
     setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
     */
    public void saveAuthentication(UserAccount user){   // 인증 허가 메소드
        String password = user.getPassword();
        if (password == null) { // 소셜 로그인 유저의 비밀번호를 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(user.getUid())
                .password(password)
                .roles(user.getRole().name())
                .build();

        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication); // 유저 객체를 SecurityContextHolder에 담아 인증 처리를 진행
    }
}
