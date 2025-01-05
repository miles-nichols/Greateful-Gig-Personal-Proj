package com.example.backend.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    List<User> findAll();

    List<User> findByUsernameStartingWith(String username);

    User findByEmail(String email);

    @Transactional
    void deleteByUsername(String username);

    User findByEmailAndUsername(String email, String username);
}
