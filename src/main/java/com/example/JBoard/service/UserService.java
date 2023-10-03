package com.example.JBoard.service;

import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class UserService {
    private final UserAccountRepository userAccountRepository;
    public void createUser(UserAccountDto dto) {
        UserAccount userAccount = userAccountRepository.save(dto.toEntity());
    }

    public UserAccount getUser(String uid) {
        UserAccount user = userAccountRepository.findByUid(uid).get();
        return user;
        /*UserAccountDto userAccountDto = userAccountRepository.findByUid(uid).
                map(UserAccountDto::from)
                .orElseThrow(() -> new EntityNotFoundException("게시글이 없습니다 - uid: " + uid));
        return userAccountDto;*/
    }

    public boolean duplicationId(String uid) {
        boolean user = userAccountRepository.existsByUid(uid);
        return user;
    }
}
