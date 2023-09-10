package com.example.JBoard.Entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {

    @Id @Column(length = 50)
    private Long userId;

    private String username;    // 실명

    private String password;

    private String email;

    private String nickname;    // 별명
}
