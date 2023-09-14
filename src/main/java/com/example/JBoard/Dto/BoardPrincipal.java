package com.example.JBoard.Dto;

import com.example.JBoard.Entity.UserAccount;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return password;
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
