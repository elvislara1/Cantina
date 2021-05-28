package com.example.cantina.model;

import com.google.firebase.firestore.QueryDocumentSnapshot;

public class CompraEnEspera {
    public String productoId;
    public String nombre;
    public double precio;
    public String idDrawable;
    public String cantidad;
    public String numPedido;

    public CompraEnEspera(String productoId, String nombre, double precio, String idDrawable, String cantidad, String numPedido) {
        this.productoId = productoId;
        this.nombre = nombre;
        this.precio = precio;
        this.idDrawable = idDrawable;
        this.cantidad = cantidad;
        this.numPedido = numPedido;
    }

    public CompraEnEspera(QueryDocumentSnapshot m) {
        this.productoId = m.getString("productoId");
        this.nombre = m.getString("nombre");
        this.precio = m.getDouble("precio");
        this.idDrawable = m.getString("idDrawable");
        this.cantidad = m.getString("cantidad");
        this.numPedido = m.getString("numPedido");
    }
}