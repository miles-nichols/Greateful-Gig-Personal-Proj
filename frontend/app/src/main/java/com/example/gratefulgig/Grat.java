package com.example.gratefulgig;

public class Grat {
    private String name;
    private String description;
    private String date;

    public Grat(String name, String description, String date) {
        this.name = name;
        this.description = description;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }
}
