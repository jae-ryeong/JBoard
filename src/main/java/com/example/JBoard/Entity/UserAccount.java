package com.example.JBoard.Entity;

import com.example.JBoard.Entity.constant.MemberRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 5, max = 50)
    @Column(length = 50, unique = true)
    private String uid;

    //@NotNull
    private String password;
    private String username;    // 실명
    // @Column(unique = true)
    private String email;
    @Column(unique = true)
    private String nickname;    // 별명

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private UserAccount(String uid, String password, String username, String email, String nickname) {
        this.uid = uid;
        this.password = password;
        this.username = username;
        this.email = email;
        this.nickname = nickname;
        this.role = MemberRole.USER;    // 기본적으로 user role을 주어준다.
    }

    public static UserAccount of(String uid, String password, String username, String email, String nickname) {
        return new UserAccount(uid, password, username, email, nickname);
    }



}
