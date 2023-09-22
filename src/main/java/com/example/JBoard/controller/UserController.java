package com.example.JBoard.controller;

import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Dto.UserCreateDto;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.rmi.server.UID;

@Controller
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login(UserCreateDto userCreateDto) {
        return "/user/login";
    }

    @GetMapping("/Registration")
    public String join() {
        return "/user/userCreateForm";
    }

    @PostMapping("/Registration")
    public String join(UserAccountDto dto) {

        try {
            userService.createUser(dto);
            System.out.println("회원가입이 완료되었습니다.");
        } catch (DataIntegrityViolationException e) {
            e.printStackTrace();
            return "/user/userCreateForm";
        } catch (Exception e) {
            e.printStackTrace();
            return "/user/userCreateForm";
        }   // 중복 값 존재시 가입 X


        return "redirect:/boardlist";
    }

    @GetMapping("/validation")
    @ResponseBody
    public boolean validationUid(@RequestParam("uid") String uid, Model model) {
        boolean result = userService.duplicationId(uid);
        model.addAttribute("result", result);
        return result;
    }

}
