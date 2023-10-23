package com.example.JBoard.Repository;

import com.example.JBoard.Entity.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
    Optional<UserAccount> findByUid(String uid);

    boolean existsByUid(String uid);    // id 중복 검사
}
