package com.example.JBoard.Dto;

import com.example.JBoard.Entity.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

public record BoardPrincipal(
        String uid,
        String password,
        String username,
        String email,
        String nickname
) implements UserDetails {

    public static BoardPrincipal of(String uid, String password, String username, String email, String nickname) {
        return new BoardPrincipal(uid, password, username, email, nickname);
    }

    public static UserDetails of(String uid, String password) {
        return new BoardPrincipal(uid, password, null, null, null);
    }

    public UserAccountDto toDto() { // 이미 인스턴스가 만들어진 상황이니까 static 메서드가 아니다.
        return UserAccountDto.of(
                uid, password, username, email, nickname
        );  // 역으로 회원 정보를 저장하기 위해 생성
    }

    public static BoardPrincipal from(UserAccountDto dto) {
        return BoardPrincipal.of(
                dto.uid(),
                dto.password(),
                dto.username(),
                dto.email(),
                dto.nickname()
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {return new BCryptPasswordEncoder().encode(password);}

    @Override
    public String getUsername() {
        return uid;
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
