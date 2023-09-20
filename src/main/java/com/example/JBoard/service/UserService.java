package com.example.JBoard.service;

import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserAccountRepository userAccountRepository;
    public void createUser(UserAccountDto dto) {
        /*if (userAccountRepository.existsByUid(dto.uid())){
            throw new
        }*/
        UserAccount userAccount = userAccountRepository.save(dto.toEntity());
    }

    public UserAccount getUser(String uid) {
        UserAccount user = userAccountRepository.findByUid(uid).get();
        return user;
    }

    public boolean duplication(String uid) {
        Optional<UserAccount> user = userAccountRepository.findByUid(uid);
        if (userAccountRepository.findByUid(uid).isEmpty()) {
            return false;
        }else{
            return true;
        }
    }
}
