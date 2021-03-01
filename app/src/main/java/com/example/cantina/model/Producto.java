package com.example.cantina.model;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Producto {
    @PrimaryKey(autoGenerate = true)
    public int productoId;

    public String nombre;
    public String precio;
    public String idDrawable;
    public String tipo;

    public Producto(String nombre, String precio, String idDrawable, String tipo) {
        this.nombre = nombre;
        this.precio = precio;
        this.idDrawable = idDrawable;
        this.tipo = tipo;
    }
}