package com.example.JBoard.Dto;

import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Entity.constant.MemberRole;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

public record BoardPrincipal(
        String uid,
        String password,
        String username,
        String email,
        String nickname,
        String role
) implements UserDetails {
    public static BoardPrincipal of(String uid, String password, String username, String email, String nickname, String role) {
        return new BoardPrincipal(uid, password, username, email, nickname, role);
    }

    public static UserDetails of(String uid, String password) {
        return new BoardPrincipal(uid, password, null, null, null, null);
    }

    public UserAccountDto toDto() { // 이미 인스턴스가 만들어진 상황이니까 static 메서드가 아니다.
        return UserAccountDto.of(
                uid, password, username, email, nickname, role
        );  // 역으로 회원 정보를 저장하기 위해 생성
    }

    public static BoardPrincipal from(UserAccountDto dto) {
        return BoardPrincipal.of(
                dto.uid(),
                dto.password(),
                dto.username(),
                dto.email(),
                dto.nickname(),
                dto.role()
        );
    }

    @Override   //계정이 갖고있는 권한 목록을 리턴한다. (권한이 여러개 있을 경우 루프를 돌아야 한다)
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));

        return authorities;
    }

    @Override
    public String getPassword() {
        return new BCryptPasswordEncoder().encode(password);
    }

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
