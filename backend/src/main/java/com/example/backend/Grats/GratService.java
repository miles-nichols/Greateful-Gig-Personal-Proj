package com.example.backend.Grats;

import com.example.backend.Users.User;
import com.example.backend.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GratService {

    @Autowired
    private GratRepository gratRepository;

    @Autowired
    private UserRepository userRepository;

    // Add a Grat for a specific user
    public Grat addGrat(String username, Grat grat) {
        Optional<User> user = userRepository.findById(username);
        if (user.isPresent()) {
            grat.setUser(user.get());
            return gratRepository.save(grat);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Get all Grats for a specific user
    public List<Grat> getGratsByUser(String username) {
        return gratRepository.findByUserUsername(username);
    }
}
