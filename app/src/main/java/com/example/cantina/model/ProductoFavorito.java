package com.example.cantina.model;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ProductoFavorito {
    public String productoId;
    public String nombre;
    public double precio;
    public String idDrawable;

    public ProductoFavorito(String productoId, String nombre, double precio, String idDrawable) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.idDrawable = idDrawable;
    }

    public ProductoFavorito(QueryDocumentSnapshot m) {
        this.productoId = m.getString("productoId");
        this.nombre = m.getString("nombre");
        this.precio = m.getDouble("precio");
        this.idDrawable = m.getString("idDrawable");
    }
}
