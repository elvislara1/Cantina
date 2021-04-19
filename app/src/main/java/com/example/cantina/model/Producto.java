package com.example.cantina.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Producto {
    @PrimaryKey(autoGenerate = true)
    public int productoId;

    public String nombre;
    public double preciod;
    public String precio;
    public String img;
    public String tipo;

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
}