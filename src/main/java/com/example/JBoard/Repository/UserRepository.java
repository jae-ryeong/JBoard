package com.example.JBoard.Repository;

import com.example.JBoard.Entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

@Repository
public class UserRepository {

    @PersistenceContext
    EntityManager em;

    public Long save(User user){
        em.persist((user));
        return user.getUserId();
    }

    public User find(Long user_id){
        return em.find(User.class, user_id);
    }
}
