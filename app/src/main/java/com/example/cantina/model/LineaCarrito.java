package com.example.cantina.model;
//@Entity(primaryKeys = {"userId", "productoId"})
public class LineaCarrito {
    public String userId;

    public LineaCarrito(String userId) {
        this.userId = userId;
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