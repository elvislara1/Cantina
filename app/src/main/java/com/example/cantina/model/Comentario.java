package com.example.cantina.model;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Exclude;

@Entity
public class Comentario {
    @PrimaryKey(autoGenerate = true)
    @Exclude
    public int comentarioId;

    public String usuario;
    public String email;
    public String autorFoto;
    public String cabecera;
    public String comentario;
    public String fecha;
    public String valoracion;

    @Ignore
    public Comentario(int comentarioId, String usuario, String email, String cabecera, String comentario, String fecha, String valoracion) {
        this.comentarioId = comentarioId;
        this.usuario = usuario;
        this.email = email;
        this.cabecera = cabecera;
        this.comentario = comentario;
        this.fecha = fecha;
        this.valoracion = valoracion;
    }

    public Comentario(String usuario, String email, String autorFoto, String cabecera, String comentario, String fecha, String valoracion) {
        this.usuario = usuario;
        this.email = email;
        this.autorFoto = autorFoto;
        this.cabecera = cabecera;
        this.comentario = comentario;
        this.fecha = fecha;
        this.valoracion = valoracion;
    }

    public Comentario(DocumentSnapshot m){
        this.email = m.getString("email");
        this.usuario = m.getString("usuario");
        this.fecha = m.getString("fecha");
        this.cabecera = m.getString("cabecera");
        this.comentario = m.getString("comentario");
        this.autorFoto = m.getString("autorFoto");
        this.valoracion = m.getString("valoracion");

    }
}
