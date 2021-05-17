package com.example.cantina.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.Exclude;
import com.google.firebase.firestore.QueryDocumentSnapshot;

@Entity
public class Producto {
    @PrimaryKey(autoGenerate = true)
    @Exclude
    public int productoId;

    public String nombre;
    public double preciod;
    @Exclude
    public String precio;
    public String img;
    public String tipo;

    @Ignore
    public Producto(String nombre, double preciod, String img, String tipo) {
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

    public Producto(QueryDocumentSnapshot m) {
    }
}