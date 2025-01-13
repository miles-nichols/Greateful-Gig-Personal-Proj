package com.example.backend.Users;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);

    List<User> findAll();

    List<User> findByUsernameStartingWith(String username);

}
