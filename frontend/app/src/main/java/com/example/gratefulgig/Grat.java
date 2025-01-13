package com.example.gratefulgig;

import java.util.List;

public class Grat {
    private long id;
    private String gratName;
    private String gratDescription;
    private String gratDate; // Can be parsed to a Date if needed
    private User user; // Can be null
    private List<User> users;

    // Getters and setters
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGratName() {
        return gratName;
    }

    public void setGratName(String gratName) {
        this.gratName = gratName;
    }

    public String getGratDescription() {
        return gratDescription;
    }

    public void setGratDescription(String gratDescription) {
        this.gratDescription = gratDescription;
    }

    public String getGratDate() {
        return gratDate;
    }

    public void setGratDate(String gratDate) {
        this.gratDate = gratDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }
}