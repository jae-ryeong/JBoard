package com.example.JBoard.Dto;

import com.example.JBoard.Entity.UserAccount;

public record UserDto(
        String userId,
        String password,
        String username,
        String email,
        String nickname
) {
    public UserAccount toEntity() {
        return UserAccount.of(userId, password, username, email, nickname);
    }
}
