package com.example.JBoard.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BoardController {

    @GetMapping("/boardlist")
    public String boardlist() {
        return "articles/boardList";
    }

    @GetMapping("/boardCreateForm")
    public String boardCreateForm() {
        return "articles/boardCreateForm";
    }
}
