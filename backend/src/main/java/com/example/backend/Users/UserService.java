package com.example.backend.Users;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;
import jakarta.validation.constraints.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDate;
import java.util.*;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public User registerUser(User user) {
        return userRepository.save(user);
    }

    public Optional<User> getUserByUsername(String username) {
        return Optional.ofNullable(userRepository.findByUsername(username));
    }

    public boolean authenticateUser(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return true;
        }
        return false;
    }

    /**
     * Search users by partial username
     */
    public List<User> searchUsers(String partUsername) {
        return userRepository.findByUsernameStartingWith(partUsername);
    }

    /**
     * Handle user following another user
     */
    public Map<String, String> followUser(String followerUsername, String followingUsername) {
        Map<String, String> response = new HashMap<>();
        User follower = userRepository.findByUsername(followerUsername);
        User following = userRepository.findByUsername(followingUsername);

        if (follower == null || following == null) {
            response.put("message", "One or both users not found");
            response.put("status", "404");
            return response;
        }

        if (following.getFollowers().contains(follower)) {
            response.put("message", followerUsername + " is already following " + followingUsername);
            response.put("status", "409");
            return response;
        }

        following.getFollowers().add(follower);
        follower.getFollowing().add(following);
        userRepository.save(following);
        userRepository.save(follower);

        if (following.isFriend(follower)) {
            following.getFriends().add(follower);
            follower.getFriends().add(following);
            userRepository.save(following);
            userRepository.save(follower);
            response.put("message", "Users are now friends");
        } else {
            response.put("message", "Follow successful");
        }
        response.put("status", "200");
        return response;
    }

    /**
     * Handle user unfollowing another user
     */
    public Map<String, String> unfollowUser(String followerUsername, String followingUsername) {
        Map<String, String> response = new HashMap<>();
        User follower = userRepository.findByUsername(followerUsername);
        User following = userRepository.findByUsername(followingUsername);

        if (follower == null || following == null) {
            response.put("message", "One or both users not found");
            response.put("status", "404");
            return response;
        }

        if (!follower.isFollowing(following)) {
            response.put("message", "User: " + follower.getUsername() + " is not following User: " + following.getUsername());
            response.put("status", "409");
            return response;
        }

        follower.getFollowing().remove(following);
        following.getFollowers().remove(follower);
        follower.getFriends().remove(following);
        following.getFriends().remove(follower);
        userRepository.save(follower);
        userRepository.save(following);

        response.put("message", "Successfully unfollowed");
        response.put("status", "200");
        return response;
    }

    /**
     * Get user's friends
     */
    public Set<User> getUserFriends(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getFriends() : new HashSet<>();
    }

    /**
     * Get user's followers
     */
    public Set<User> getUserFollowers(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getFollowers() : new HashSet<>();
    }

    /**
     * Get users that user is following
     */
    public Set<User> getUserFollowing(String username) {
        User user = userRepository.findByUsername(username);
        return user != null ? user.getFollowing() : new HashSet<>();
    }
}
