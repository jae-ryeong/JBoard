package com.example.JBoard.controller;

import com.example.JBoard.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.*;

@WebMvcTest(UserController.class)
class UserControllerTest {

    private final MockMvc mvc;

    @MockBean private UserService userService;

    public UserControllerTest(@Autowired MockMvc mvc) {
        this.mvc = mvc;
    }

    @Test
    void login() {
    }

    @Test
    void join() {
    }

    @Test
    void join2() {
    }
}