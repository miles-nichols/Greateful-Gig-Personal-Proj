package com.example.backend.Grats;

import com.example.backend.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GratRepository extends JpaRepository<Grat, Long> {
    List<Grat> findByUserUsername(String username);
}
