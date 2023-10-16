package com.example.JBoard.Dto.Request;

public record LoginRequest(
        String uid,
        String password
) {
    public LoginRequest(String uid, String password) {
        this.uid = uid;
        this.password = password;
    }
}
