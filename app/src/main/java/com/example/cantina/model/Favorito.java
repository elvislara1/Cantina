package com.example.cantina.model;

import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "productoId"})
public class Favorito {
    public int userId;
    public int productoId;

    public Favorito(int userId, int productoId) {
        this.userId = userId;
        this.productoId = productoId;
    }
}
