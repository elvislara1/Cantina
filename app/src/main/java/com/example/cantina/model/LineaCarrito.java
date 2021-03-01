package com.example.cantina.model;
import androidx.room.Entity;

@Entity(primaryKeys = {"userId", "productoId"})
public class LineaCarrito {
    public int userId;
    public int productoId;
    public int cantidad;

    public LineaCarrito(int userId, int productoId) {
        this.userId = userId;
        this.productoId = productoId;
    }

    public LineaCarrito(LineaCarrito lineaCarrito) {
    }
}

/*
1 Cafe 2
1 Fanta 4
2 Cafe 2
3 Fanta 1000



 */