package com.example.JBoard.jwt.service;

import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.UserAccountRepository;
import com.example.JBoard.jwt.util.SecurityUtil;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class inflearnUserService {
    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserAccount signup(UserAccountDto userAccountDto) {
        if (userAccountRepository.findByUid(userAccountDto.toEntity().getUid()).orElse(null) != null) {
            throw new EntityExistsException("이미 가입되어 있는 유저입니다.");
        }

        UserAccount user = userAccountDto.toEntity();

        return user;
    }

    @Transactional(readOnly = true)
    public UserAccountDto getUserWithAuthorities(String username) {
        return UserAccountDto.from(userAccountRepository.findByUid(username).orElse(null));
    }

    @Transactional(readOnly = true)
    public UserAccountDto getMyUserWithAuthorities() {
        return UserAccountDto.from(
                SecurityUtil.getCurrentUsername()
                        .flatMap(userAccountRepository::findByUid)
                        .orElseThrow(() -> new UsernameNotFoundException("Member not found"))
        );
    }
}
