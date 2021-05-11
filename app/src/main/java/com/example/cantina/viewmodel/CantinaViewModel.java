package com.example.cantina.viewmodel;

import android.app.Application;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.arch.core.util.Function;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Transformations;

import com.example.cantina.model.CantinaRepository;
import com.example.cantina.model.Comentario;
import com.example.cantina.model.Producto;
import com.example.cantina.model.ProductoEnCarrito;
import com.example.cantina.model.ProductoFavorito;

import java.util.List;

public class CantinaViewModel extends AndroidViewModel {


    CantinaRepository cantinaRepository;

    public MutableLiveData<Uri> imagenSeleccionada = new MutableLiveData<>();
    MutableLiveData<Producto> productoSeleccionado = new MutableLiveData<>();
    public MutableLiveData<String> terminoBusqueda = new MutableLiveData<>();
    MutableLiveData<List<Comentario>> listComunidadMutableLiveData = new MutableLiveData<>();

    LiveData<List<Producto>> resultadoBusqueda = Transformations.switchMap(terminoBusqueda, new Function<String, LiveData<List<Producto>>>() {
        @Override
        public LiveData<List<Producto>> apply(String input) {
            return cantinaRepository.buscar(input);
        }
    });

    public CantinaViewModel(@NonNull Application application) {
        super(application);
        cantinaRepository = new CantinaRepository(application);
    }
    //obtener
    public LiveData<List<Producto>> obtenerProductos() {
        return cantinaRepository.obtenerProductos();
    }
    public LiveData<List<Producto>> obtenerBocatas() {
        return cantinaRepository.obtenerBocatas();
    }
    public LiveData<List<Producto>> obtenerCafes() {
        return cantinaRepository.obtenerCafes();
    }
    public LiveData<List<Producto>> obtenerBebidas() { return cantinaRepository.obtenerBebidas(); }
    public LiveData<List<Producto>> alfabeticamente() {
        return cantinaRepository.alfabeticamente();
    }
    public LiveData<List<Producto>> caro() {
        return cantinaRepository.caro();
    }

    //buscar
    public LiveData<List<Producto>> buscar(){
        return resultadoBusqueda;
    }

    //insertar nuevo producto
    public void insertarProducto(String nombre, String precio, String portada, String tipo) {
        cantinaRepository.insertarProducto(nombre, precio, portada, tipo);
    }

    public LiveData<Integer> isFavorite(int userId, int productId) {
        return cantinaRepository.isFavorite(userId, productId);
    }

//---------------------------------------

    public LiveData<List<ProductoFavorito>> productosFavoritos(int userId) {
        return cantinaRepository.productosFavoritos(userId);
    }
    public LiveData<List<ProductoEnCarrito>> carrito(int userId) {
        return cantinaRepository.carrito(userId);
    }
    //---------------------------------------
    public MutableLiveData<Producto> seleccionado(){
        return productoSeleccionado;
    }

    public void anadirAlCarrito(int userId, int productoId) {
        cantinaRepository.anadirAlCarrito(userId, productoId);
    }
    public void anadirFavorito(int userId, int productoId) {
        cantinaRepository.anadirFavoritos(userId, productoId);
    }
    public void eliminarProductoDelCarrito(int userId, int productoId) {
        cantinaRepository.eliminarProductoDelCarrito(userId, productoId);
    }
    public void eliminarProductoDeFavorito(int userId, int productoId) {
        cantinaRepository.eliminarProductoDeFavorito(userId, productoId);
    }
    public void eliminarCarrito(int userId) {
        cantinaRepository.eliminarCarrito(userId);
    }


    public void seleccionar(Producto producto) {
        productoSeleccionado.setValue(producto);
    }
    public void establecerTerminoBusqueda(String s){
        terminoBusqueda.setValue(s);
    }
    public void establecerImagenSeleccionada(Uri uri){
        imagenSeleccionada.setValue(uri);
    }


    MutableLiveData<List<Comentario>> obtener() {
        return listComunidadMutableLiveData;
    }

    public LiveData<List<Comentario>> obtenerComunidad() {
        return cantinaRepository.obtenerComunidad();
    }

    //insertar nuevo producto
    public void insertarComentario(String usuario, String cabecera, String comentario, float valoracion) {
        cantinaRepository.insertarComentario(usuario, cabecera, comentario, valoracion);
    }
}
