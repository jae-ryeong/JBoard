package com.example.JBoard.jwt.filter;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.UserAccountRepository;
import com.example.JBoard.config.JwtProperties;
import com.example.JBoard.service.UserSecurityService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import java.io.IOException;
import java.util.Optional;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    @Autowired private UserAccountRepository userAccountRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Value("${jwt.secretKey}")
    private String secret;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        String header = request.getHeader(JwtProperties.HEADER_STRING);
        if(header == null || !header.startsWith(JwtProperties.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        String token = request.getHeader(JwtProperties.HEADER_STRING)
                .replace(JwtProperties.TOKEN_PREFIX, "");

        String uid = JWT.require(Algorithm.HMAC512(secret)).build().verify(token)
                .getClaim("uid").asString();

        /*if(uid != null) {
            Optional<UserAccount> user = userAccountRepository.findByUid(uid);

            BoardPrincipal principalDetails = new BoardPrincipal();
            Authentication authentication =
                    new UsernamePasswordAuthenticationToken(
                            principalDetails,
                            null, // 패스워드는 모르니까 null 처리, 인증 용도 x
                            principalDetails.getAuthorities());

            // 권한 관리를 위해 세션에 접근하여 값 저장
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }*/

        chain.doFilter(request, response);
    }
}
