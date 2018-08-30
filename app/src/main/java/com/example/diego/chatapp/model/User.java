package com.example.diego.chatapp.model;

public class User {

    private String key;
    private String name;

    public User(String key, String name) {
        this.key = key;
        this.name = name;
    }

    public String getKey() {
        return key;
    }

    public String getName() {
        return name;
    }
}
