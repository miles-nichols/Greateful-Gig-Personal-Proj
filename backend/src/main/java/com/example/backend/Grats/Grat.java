package com.example.backend.Grats;

import com.example.backend.Users.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

@Entity
@Table(name = "app_grat")
public class Grat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 3, max = 255)
    private String gratName;

    @Size(min = 3, max = 500)
    private String gratDescription;

    private LocalDate gratDate;

    @ManyToOne
    @JoinColumn(name = "username")
    private User user;

    public Grat() {}

    public Grat(String gratName, String gratDescription, LocalDate gratDate, User user) {
        this.gratName = gratName;
        this.gratDescription = gratDescription;
        this.gratDate = gratDate;
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public LocalDate getGratDate() {
        return gratDate;
    }

    public void setGratDate(LocalDate gratDate) {
        this.gratDate = gratDate;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
