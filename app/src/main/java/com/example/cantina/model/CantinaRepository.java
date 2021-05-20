package com.example.cantina.model;

import android.app.Application;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class CantinaRepository {

    Executor executor = Executors.newSingleThreadExecutor();

    AppBaseDeDatos.CantinaDao dao;

    public CantinaRepository(Application application) {
        dao = AppBaseDeDatos.getInstance(application).dao();
    }
/*
    public LiveData<List<Producto>> obtenerProductos() {
        return dao.obtenerProductos();
    }
    public LiveData<List<Producto>> obtenerBocatas() {
        return dao.obtenerBocatas();
    }
    public LiveData<List<Producto>> obtenerCafes() {
        return dao.obtenerCafes();
    }
    public LiveData<List<Producto>> obtenerBebidas() {
        return dao.obtenerBebidas();
    }
    public LiveData<List<Producto>> alfabeticamente() {
        return dao.alfabeticamente();
    }
    public LiveData<List<Producto>> caro() {
        return dao.caro();
    }
    //Buscar
    public LiveData<List<Producto>> buscar(String d) {
        return dao.buscar(d);
    }

    public LiveData<List<ProductoFavorito>> favoritos(int userId) {
        return dao.obtenerFavorito(userId);
    }

    public LiveData<List<ProductoFavorito>> productosFavoritos(int userId) {
        return dao.productosFavoritos(userId);
    }

    //comprobar si es favorito un producto en carrito o en cualquier otro fragment sin necesidad de joins
    public LiveData<Integer> isFavorite(int userId, int productId) {
        return dao.isFavorite(userId, productId);
    }

    public void insertarProducto(String nombre, String precio, String portada, String tipo) {
        executor.execute(() -> dao.insertarProducto(new Producto(nombre, precio, portada, tipo)));
    }
    */
    /*
    public void anadirAlCarrito(int userId, int  productoId) {
        executor.execute(() -> {
            dao.anadirAlCarrito(new LineaCarrito(userId, productoId));
        });
    }
    public void anadirFavoritos(int userId, int  productoId) {
        executor.execute(() -> {
            dao.anadirFavorito(new Favorito(userId, productoId));
        });
    }
    public void eliminarProductoDelCarrito(int userId, int productoId) {
        executor.execute(() -> {
            dao.eliminarProductoDelCarrito(userId, productoId);
        });
    }

    public void eliminarCarrito(int userId) {
        executor.execute(() -> {
            dao.eliminarCarrito(userId);
        });
    }

    public void eliminarProductoDeFavorito(int userId, int productoId) {
        executor.execute(() -> {
            dao.eliminarProductoFavorito(userId, productoId);
        });
    }
    public LiveData<List<ProductoEnCarrito>> carrito(int userId) {
        return dao.obtenerCarrito(userId);
    }

    public LiveData<List<Comentario>> obtenerComunidad() {
        return dao.obtenerComentarios();
    }
    public void insertarComentario(String usuario, String cabecera, String comentario, Float valoracion) {
        executor.execute(() -> dao.insertarComentario(new Comentario(usuario, cabecera, comentario, valoracion)));
    }

     */
}
