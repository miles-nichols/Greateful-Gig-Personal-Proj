package com.example.backend.Users;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody User loginUser) {
        boolean isAuthenticated = userService.authenticateUser(loginUser.getUsername(), loginUser.getPassword());
        if (isAuthenticated) {
            return ResponseEntity.ok("Login successful");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> registerUser(@RequestBody User userRequest) {
        if (userRequest.getUsername() == null || userRequest.getPassword() == null || userRequest.getEmail() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("message", "Invalid request: All fields are required"));
        }
        Optional<User> existingUser = userService.getUserByUsername(userRequest.getUsername());
        if (existingUser.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "User already exists"));
        }
        userService.registerUser(userRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User created successfully"));
    }

    @GetMapping("/{username}")
    public ResponseEntity<User> getUserByUsername(@PathVariable String username) {
        Optional<User> user = userService.getUserByUsername(username);
        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null); // Or return a custom error message
        }
    }

    @Operation(summary="Gets User's Friends")
    @GetMapping("/user/friend/{username}")
    public Set<User> getFriends(@PathVariable String username) {
        return userService.getUserFriends(username);
    }

    @Operation(summary="Gets User's Followers")
    @GetMapping("/user/followers/{username}")
    public Set<User> getFollowers(@PathVariable String username) {
        return userService.getUserFollowers(username);
    }

    @Operation(summary="Gets Users who User is Following")
    @GetMapping("/user/following/{username}")
    public Set<User> getFollowing(@PathVariable String username) {
        return userService.getUserFollowing(username);
    }


    @Operation(summary="follower_username unfollows following_username")
    @PutMapping("/user/unfollow")
    public Map<String, String> unfollow(@RequestParam String follower_username, @RequestParam String following_username) {
        return userService.unfollowUser(follower_username, following_username);
    }

    @Operation(summary="follower_username follows following_username")
    @PutMapping("/user/follow")
    public Map<String, String> follow(@RequestParam String follower_username, @RequestParam String following_username) {
        return userService.followUser(follower_username, following_username);
    }
}
