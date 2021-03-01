package com.example.cantina.model;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Usuario {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String username;
    public String password;
    public String biography;

    public Usuario(String username, String password, String biography) {
        this.username = username;
        this.password = password;
        this.biography = biography;
    }

    @NonNull
    @Override
    public String toString() {
        return "USER = " + username + " " + password + " " + biography;
    }
}