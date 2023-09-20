package com.example.JBoard.controller;

import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.UID;

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
        System.out.println("회원가입이 완료되었습니다.");
        return "redirect:/boardlist";
    }

    @GetMapping("/validation")
    public void validationUid(@RequestParam("uid") String uid, Model model) {
        boolean result = userService.duplication(uid);
        model.addAttribute("result", result);
        System.out.println("전달 완료");
    }
}
