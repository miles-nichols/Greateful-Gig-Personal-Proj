package com.example.backend.Users;

import com.example.backend.Grats.Grat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.*;

@Entity
@Table(name = "app_user")
public class User {

    @Id
    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotBlank
    @Email
    private String email;

    private LocalDate birthdate;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Grat> grats;

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name="friends_with",
            joinColumns={@JoinColumn(name="user_username", referencedColumnName = "username")},
            inverseJoinColumns={@JoinColumn(name="friend_username", referencedColumnName = "username")})
    @JsonIgnore
    private Set<User> friends = new HashSet<User>();

    @ManyToMany(cascade={CascadeType.PERSIST, CascadeType.MERGE })
    @JoinTable(name="followers",
            joinColumns={@JoinColumn(name="following_username")},
            inverseJoinColumns={@JoinColumn(name="follower_username")})
    @JsonIgnore
    private Set<User> followers = new HashSet<User>();

    @ManyToMany(mappedBy = "followers", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JsonIgnore
    private Set<User> following = new HashSet<>();

    public User() { }

    public User(String username, String password, String email, LocalDate birthdate) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthdate = birthdate;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public List<Grat> getGrats() {
        return grats;
    }

    public void setGrats(List<Grat> grats) {
        this.grats = grats;
    }

    public Set<User> getFriends() {
        return this.friends;
    }

    public Set<User> getFollowers() {
        return this.followers;
    }

    public Set<User> getFollowing() {
        return this.following;
    }

    public boolean isFriend(User other_user){
        Set<User> user1_followers = this.getFollowers();
        Set<User> user2_followers = other_user.getFollowers();
        return (user1_followers.contains(other_user) && user2_followers.contains(this));
    }

    public boolean isFollowing(User other_user){
        return (other_user.getFollowers().contains(this));
    }
}
