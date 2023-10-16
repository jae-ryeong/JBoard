package com.example.JBoard.service;

import com.example.JBoard.Dto.BoardPrincipal;
import com.example.JBoard.Dto.Request.LoginRequest;
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

    public UserAccount login(LoginRequest req) {
        Optional<UserAccount> optionalUser = userAccountRepository.findByUid(req.uid());

        // loginId와 일치하는 User가 없으면 null return
        if(optionalUser.isEmpty()) {
            return null;
        }

        UserAccount user = optionalUser.get();

        // 찾아온 User의 password와 입력된 password가 다르면 null return
        if(!user.getPassword().equals(req.password())) {
            return null;
        }

        return user;
    }
}
