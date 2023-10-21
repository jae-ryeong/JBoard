package com.example.JBoard.jwt.filter;

import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.jwt.util.JwtTokenUtil;
import com.example.JBoard.service.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.web.authentication.BearerTokenAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter { // OncePerRequestFilter : 매번 들어갈 때 마다 체크 해주는 필터

    private final UserService userService;
    private final String secretKey;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorizationHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        log.info(authorizationHeader);

        //String authorizationHeader =  "Bearer " + getCookie(request);

        // Header의 Authorization 값이 비어있으면 => Jwt Token을 전송하지 않음 => 로그인 하지 않음
            if (authorizationHeader == null) {
            log.info("Authorization 값이 비어있습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // Header의 Authorization 값이 'Beader '로 시작하지 않으면 => 잘못된 토큰
        if (!authorizationHeader.startsWith("Bearer ")) {
            log.info("'Bearer'로 시작하지 않습니다.");
            filterChain.doFilter(request, response);
            return;
        }

        // 전송받은 값에서 'Bearer ' 뒷부분(Jwt Token)을 추출
        String token = authorizationHeader.split(" ")[1];

        // 전송받은 Jwt Token이 만료되었다면 => 다음 필터 진행(인증 X)
        if (JwtTokenUtil.isExpired(token, secretKey)) {

            filterChain.doFilter(request, response);
            return;
        }

        // Jwt Token에서 loginId 추출
        String tokenUid = JwtTokenUtil.getUid(token, secretKey);

        // 추출한 LoginId로 유저 찾아오기
        UserAccount loginUser = userService.getUser(tokenUid);
        BoardPrincipal user = BoardPrincipal.from(UserAccountDto.from(loginUser));

        // loginUser 정보로 UsernamePasswordAuthenticationToken 발급 // TODO: 여기서 건드려보자.
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                user.getUsername(), user.getPassword(), List.of(new SimpleGrantedAuthority(user.role())));
        authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

        // 권한 부여

        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request, response);
    }

    public String getCookie(HttpServletRequest req){
        Cookie[] cookies=req.getCookies(); // 모든 쿠키 가져오기
        if(cookies!=null){
            for (Cookie c : cookies) {
                String name = c.getName(); // 쿠키 이름 가져오기
                String value = c.getValue(); // 쿠키 값 가져오기
                if (name.equals("accessToken")) {
                    return value;
                }
            }
        }
        return null;
    }
    public void deleteCookie(HttpServletResponse res){
        Cookie cookie = new Cookie("accessToken", null); // 삭제할 쿠키에 대한 값을 null로 지정
        cookie.setMaxAge(0); // 유효시간을 0으로 설정해서 바로 만료시킨다.
        res.addCookie(cookie); // 응답에 추가해서 없어지도록 함
    }

    /*public Cookie createCookie(String userName) {
        String cookieName = "refreshtoken";
        String cookieValue = createReFreshToken(userName); // 쿠키벨류엔 글자제한이 이써, 벨류로 만들어담아준다.
        Cookie cookie = new Cookie(cookieName, cookieValue);
        // 쿠키 속성 설정
        cookie.setHttpOnly(true);  //httponly 옵션 설정
        cookie.setSecure(true); //https 옵션 설정
        cookie.setPath("/"); // 모든 곳에서 쿠키열람이 가능하도록 설정
        cookie.setMaxAge(60 * 60 * 24); //쿠키 만료시간 설정
        return cookie;
    }*/
}
