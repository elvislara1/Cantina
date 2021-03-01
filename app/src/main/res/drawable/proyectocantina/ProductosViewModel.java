package com.example.proyectocantina;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

public class ProductosViewModel extends AndroidViewModel {

    ProductoRepository productosRepository;

    MutableLiveData<Producto> productoSeleccionado = new MutableLiveData<>();


    public ProductosViewModel(@NonNull Application application) {
        super(application);

        productosRepository = new ProductoRepository();
    }

    LiveData<List<Producto>> barato() {
        return productosRepository.barato();
    }
    LiveData<List<Producto>> caro() {
        return productosRepository.caro();
    }
    LiveData<List<Producto>> alfabet() {
        return productosRepository.alfabet();
    }

    //
    LiveData<List<Producto>> productos() {
        return productosRepository.allProducts();
    }
    LiveData<List<Producto>> bocatas() {
        return productosRepository.bocatas();
    }
    LiveData<List<Producto>> bebidas() {
        return productosRepository.bebidas();
    }
    LiveData<List<Producto>> cafes() {
        return productosRepository.cafes();
    }

    //

    MutableLiveData<Producto> seleccionado(){
        return productoSeleccionado;
    }


    public void productoSeleccionado(Producto producto) {
        productoSeleccionado.setValue(producto);
    }

    public void anadirAlCarrito(Producto producto) {
        productosRepository.anadirAlCarrito(producto);
    }

    public void anadirFavoritos(Producto producto) {
        productosRepository.anadirFavoritos(producto);
    }

    public void eliminarProductoDelCarrito(Producto producto) {
        productosRepository.eliminarProductoDelCarrito(producto);
    }

    public void eliminarProductoDeFavorito(Producto producto) {
        productosRepository.eliminarProductoDeFavorito(producto);
    }

    public List<Producto> carrito() {
        return productosRepository.carrito();
    }
    public List<Producto> favorito() {
        return productosRepository.favorito();
    }
}