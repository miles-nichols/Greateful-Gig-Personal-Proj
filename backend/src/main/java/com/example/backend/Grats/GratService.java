package com.example.backend.Grats;

import com.example.backend.Users.User;
import com.example.backend.Users.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            grat.setUsers(Set.of(user.get())); // Set the user as the associated user for the Grat
            return gratRepository.save(grat);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Get all Grats for a specific user
    public List<Grat> getGratsByUser(String username) {
        return gratRepository.findAllByUsers_UsernameIn(List.of(username));
    }

    public List<Grat> getGratsForUserAndFriends(String username) {
        List<Grat> grats = gratRepository.findGratsForUserAndFriends(username);
        System.out.println("Retrieved grats: " + grats);
        return grats;
    }
}
