package com.example.JBoard.Dto;

import com.example.JBoard.Entity.UserAccount;

public record UserAccountDto(
        String uid,
        String password,
        String username,
        String email,
        String nickname
) {

    public static UserAccountDto of(String uid, String password, String username, String email, String nickname) {
        return new UserAccountDto(uid, password, username, email, nickname);
    }
    public UserAccount toEntity() {
        return UserAccount.of(uid, password, username, email, nickname);
    }
}
