package com.example.cantina.model;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Comentario {
    @PrimaryKey(autoGenerate = true)
    public int comentarioId;

    public String usuario;
    public String cabecera;
    public String comentario;
    public float valoracion;

    public Comentario(String usuario, String cabecera, String comentario, float valoracion) {
        this.usuario = usuario;
        this.cabecera = cabecera;
        this.comentario = comentario;
        this.valoracion = valoracion;
    }
}
