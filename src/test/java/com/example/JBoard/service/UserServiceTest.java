package com.example.JBoard.service;

import com.example.JBoard.Dto.UserAccountDto;
import com.example.JBoard.Entity.UserAccount;
import com.example.JBoard.Repository.UserAccountRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserAccountRepository userAccountRepository;

    @InjectMocks
    private UserService userService;


    @DisplayName("유저 계정 생성")
    @Test
    void createUser() {
        //given
        UserAccountDto userAccountDto = createUserDto();
        given(userAccountRepository.save(any(UserAccount.class))).willReturn(userAccountDto.toEntity());

        //when
        userService.createUser(userAccountDto);

        //then
        then(userAccountRepository).should().save(any(UserAccount.class));
        assertThat(userAccountDto.toEntity().getUid()).isEqualTo("wofud");
    }

    @DisplayName("아이디 중복 확인 테스트")
    @Test
    public void test() throws Exception{
        //given
        UserAccountDto user = createUserDto();
        given(userAccountRepository.save(any(UserAccount.class))).willReturn(user.toEntity());
        given(userService.duplicationId(user.uid())).willReturn(true);

        //when
        userService.createUser(user);
        boolean result = userService.duplicationId(user.uid());
        boolean result2 = userService.duplicationId(anyString());

        //then
        then(userAccountRepository).should().save(any(UserAccount.class));
        then(userAccountRepository).should().existsByUid(user.uid());
        assertThat(result).isTrue();
        assertThat(result2).isFalse();
    }
    private UserAccountDto createUserDto() {
        return UserAccountDto.of("wofud", "1234", null, null, null);
    }

}