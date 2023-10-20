package com.example.JBoard.config;

public interface JwtProperties {
    int EXPIRATION_TIME = 10000; // 10초 (1/1000초)
    String TOKEN_PREFIX = "Bearer ";
    String HEADER_STRING = "Authorization";
}
