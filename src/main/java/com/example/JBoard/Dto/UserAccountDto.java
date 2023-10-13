package com.example.JBoard.Dto;

import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Entity.constant.MemberRole;

public record UserAccountDto(
        String uid,
        String password,
        String username,
        String email,
        String nickname,
        String role
) {

    public static UserAccountDto of(String uid, String password, String username, String email, String nickname, String role) {
        return new UserAccountDto(uid, password, username, email, nickname, role);
    }

    public UserAccount toEntity() {
        return UserAccount.of(uid, password, username, email, nickname);
    }

    public static UserAccountDto from(UserAccount entity) {
        return new UserAccountDto(
                entity.getUid(),
                entity.getPassword(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getNickname(),
                entity.getRole().toString()
        );
    }
}
