package com.example.gratefulgig;

import java.util.List;

public class User {
    private String username;
    private String password; // Not secure, consider only using hashed passwords or omitting this field
    private String email;
    private String birthdate; // Nullable
    private List<Grat> grats;

    // Getters and setters
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

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public List<Grat> getGrats() {
        return grats;
    }

    public void setGrats(List<Grat> grats) {
        this.grats = grats;
    }
}
