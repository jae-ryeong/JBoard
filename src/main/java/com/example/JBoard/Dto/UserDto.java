package com.example.JBoard.Dto;

import com.example.JBoard.Entity.UserAccount;

public record UserDto(
        String uid,
        String password,
        String username,
        String email,
        String nickname
) {

    public static UserDto of(String uid, String password, String username, String email, String nickname) {
        return new UserDto(uid, password, username, email, nickname);
    }
    public UserAccount toEntity() {
        return UserAccount.of(uid, password, username, email, nickname);
    }
}
