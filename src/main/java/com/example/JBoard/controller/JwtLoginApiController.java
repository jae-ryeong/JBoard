package com.example.JBoard.controller;

import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.Request.LoginRequest;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.jwt.util.JwtTokenUtil;
import com.example.JBoard.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/jwt-login")
public class JwtLoginApiController {
    private final UserService userService;

    @PostMapping("/login")  // TODO: JWT 발급만 됐지 로그인X
    public String login( LoginRequest loginRequest, HttpServletResponse response) {

        //UserAccount user = userService.login(loginRequest);
        BoardPrincipal user = userService.login(loginRequest);

        // 로그인 아이디나 비밀번호가 틀린 경우 global error return
        if(user == null) {
            return"로그인 아이디 또는 비밀번호가 틀렸습니다.";
        }
        // 로그인 성공 => Jwt Token 발급

        // AccessToken
        long expireTimeMs = 1000 * 60 * 10;     // Token 만료 기간 1분
        System.out.println(user);
        String secretKey = "my-secret-key-123123";
        // String encodedString = Base64.getEncoder().encodeToString(secretKey.getBytes());
        String AccessToken = JwtTokenUtil.createAccessToken(user.getUsername(), secretKey, expireTimeMs);

        response.setHeader("Authorization", AccessToken);

        Cookie cookie = new Cookie("accessToken", AccessToken);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge((int) expireTimeMs);

        response.addCookie(cookie);

        return AccessToken;
    }

    @GetMapping("/info")
    public String userInfo(Authentication auth) {
        UserAccount loginUser = userService.getUser(auth.getName());

        return String.format("loginId : %s\nnickname : %s\nrole : %s",
                loginUser.getUid(), loginUser.getNickname(), loginUser.getRole().name());
    }

    @PostMapping("/test")
    public String test(){
        return "<h1>test 통과</h1>";
    }
}
