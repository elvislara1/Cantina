package com.example.cantina.model;

import androidx.room.Ignore;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class Producto {
    public String productoId;
    public String nombre;
    public double preciod;
    @Exclude
    public String precio;
    public String img;
    public String tipo;

    @Ignore
    public Producto(String id, String nombre, double preciod, String img, String tipo) {
        this.productoId = id;
        this.nombre = nombre;
        this.preciod = preciod;
        this.img = img;
        this.tipo = tipo;
    }

    public Producto(String nombre, String precio, String img, String tipo) {
        this.nombre = nombre;
        this.precio = precio;
        this.img = img;
        this.tipo = tipo;
    }

    public Producto(QueryDocumentSnapshot qds) {
        productoId = qds.getId();
        this.img = qds.getString("img");
        this.nombre = qds.getString("nombre");
        this.preciod = qds.getDouble("preciod");
        this.tipo = qds.getString("categoria");
    }
}