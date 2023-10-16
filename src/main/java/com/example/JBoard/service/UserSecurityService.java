package com.example.JBoard.service;

import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.UserAccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserSecurityService implements UserDetailsService {

    private final UserAccountRepository userAccountRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSecurityService(@Autowired UserAccountRepository userAccountRepository,
                               @Autowired PasswordEncoder passwordEncoder) {
        this.userAccountRepository = userAccountRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String uid) throws UsernameNotFoundException {    // 로그인을 처리해준다.
        Optional<UserAccount> user = Optional.ofNullable(userAccountRepository.findByUid(uid)
                .orElseThrow(() -> new UsernameNotFoundException(uid + "을 찾을 수 없습니다.")));

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("사용자를 찾을수 없습니다.");
        }

        BoardPrincipal principal = BoardPrincipal.from(UserAccountDto.from(user.get()));

        return principal;   // UserAccount -> UserAccountDTO -> BoardPrincipal(UserDetails)
    }
}
