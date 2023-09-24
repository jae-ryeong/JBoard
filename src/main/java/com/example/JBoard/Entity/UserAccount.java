package com.example.JBoard.Entity;

import com.example.JBoard.Entity.constant.MemberRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserAccount implements UserDetails {

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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
