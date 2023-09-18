package com.example.JBoard.controller;

import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "/user/login";
    }

    @GetMapping("/Registration")
    public String join() {
        return "/user/userCreateForm";
    }

    @PostMapping("/Registration")
    public String createUser(UserAccountDto dto) {
        userService.createUser(dto);
        // BCryptPasswordEncoder bCryptPasswordEncoder
        System.out.println("회원가입이 완료되었습니다.");
        return "redirect:/boardlist";
    }
}
