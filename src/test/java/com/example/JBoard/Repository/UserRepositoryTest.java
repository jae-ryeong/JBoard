package com.example.JBoard.Repository;

import com.example.JBoard.Entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class UserRepositoryTest {

    @Autowired UserRepository userRepository;

    @Test
    @Transactional
    @Rollback(false)
    public void testUser() {
        User user = new User();
        user.setUsername("wofud");
        Long saveId = userRepository.save(user);

        User findUser = userRepository.find(saveId);
        System.out.println("findUser = " + findUser);

        assertThat(findUser.getUserId()).isEqualTo(user.getUserId());
        assertThat(findUser.getUsername()).isEqualTo(user.getUsername());

        assertThat(findUser).isEqualTo(user);
    }
}