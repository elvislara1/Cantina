package com.example.proyectocantina;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProductoRepository {
    
    LiveData<List<Producto>> ProductosLiveData;
    LiveData<List<Producto>> BocatasLiveData;
    LiveData<List<Producto>> BebidasLiveData;
    LiveData<List<Producto>> CafesLiveData;

    LiveData<List<Producto>> BaratoLiveData;
    LiveData<List<Producto>> CaroLiveData;
    LiveData<List<Producto>> AlfabetLiveData;

    List<Producto> carrito;
    List<Producto> favorito;
    ProductoRepository() {

        List<Producto> allProducts = Arrays.asList(
                new Producto("Triangulo", "1€", R.drawable.triangulo),
                new Producto("Panini","1.30€", R.drawable.panini),
                new Producto("Croissant Choco","1€", R.drawable.choco),
                new Producto("Bifrutas","1€", R.drawable.bifrutas),
                new Producto("Bocata Tortilla", "1€", R.drawable.tortilla),
                new Producto("Croissant","1€", R.drawable.croissant),
                new Producto("Bocata Bacon","1.70€", R.drawable.bacon),
                new Producto("Frankfurt","1.20€", R.drawable.frankf),
                new Producto("Agua mineral", "0.75€", R.drawable.agua),
                new Producto("Zumo Manzana", "0.75€", R.drawable.manzana),
                new Producto("Café","1.50€", R.drawable.cafe),
                new Producto("Cafe con leche","1.80€", R.drawable.cafelechee),
                new Producto("Capucchino","1.60€", R.drawable.capuchino),
                new Producto("Té","1.70€", R.drawable.te)
        );

        List<Producto> bocatas = Arrays.asList(
                new Producto("Bocata Bacon","1.70€", R.drawable.bacon),
                new Producto("Bocata Tortilla", "1€", R.drawable.tortilla),
                new Producto("Frankfurt","1.20€", R.drawable.frankf),
                new Producto("Croissant","1€", R.drawable.croissant),
                new Producto("Croissant Choco","1€", R.drawable.choco),
                new Producto("Panini","1.30€", R.drawable.panini),
                new Producto("Triangulo", "1€", R.drawable.triangulo)
        );

        List<Producto> bebidas = Arrays.asList(
                new Producto("Agua mineral", "0.75€", R.drawable.agua),
                new Producto("Bifrutas","1€", R.drawable.bifrutas),
                new Producto("Zumo Manzana", "0.75€", R.drawable.manzana)
        );

        List<Producto> cafes = Arrays.asList(
                new Producto("Café","1.50€", R.drawable.cafe),
                new Producto("Té","1.70€", R.drawable.te),
                new Producto("Capucchino","1.60€", R.drawable.capuchino),
                new Producto("Cafe con leche","1.80€", R.drawable.cafelechee)
        );

        List<Producto> barato = Arrays.asList(
                new Producto("Zumo Manzana", "0.75€", R.drawable.manzana),
                new Producto("Agua mineral", "0.75€", R.drawable.agua),
                new Producto("Bifrutas","1€", R.drawable.bifrutas),
                new Producto("Croissant Choco","1€", R.drawable.choco),
                new Producto("Croissant","1€", R.drawable.croissant),
                new Producto("Bocata Tortilla", "1€", R.drawable.tortilla),
                new Producto("Triangulo", "1€", R.drawable.triangulo)
        );

        List<Producto> caro = Arrays.asList(
                new Producto("Cafe con leche","1.80€", R.drawable.cafelechee),
                new Producto("Té","1.70€", R.drawable.te),
                new Producto("Bocata Bacon","1.70€", R.drawable.bacon),
                new Producto("Capucchino","1.60€", R.drawable.capuchino)
        );

        List<Producto> alfabet = Arrays.asList(
                new Producto("Agua mineral", "0.75€", R.drawable.agua),
                new Producto("Bocata Tortilla", "1€", R.drawable.tortilla),
                new Producto("Bocata Bacon","1.70€", R.drawable.bacon),
                new Producto("Bifrutas","1€", R.drawable.bifrutas),
                new Producto("Capucchino","1.60€", R.drawable.capuchino),
                new Producto("Café","1.50€", R.drawable.cafe),
                new Producto("Cafe con leche","1.80€", R.drawable.cafelechee),
                new Producto("Croissant","1€", R.drawable.croissant),
                new Producto("Croissant Choco","1€", R.drawable.choco),
                new Producto("Frankfurt","1.20€", R.drawable.frankf),
                new Producto("Panini","1.30€", R.drawable.panini),
                new Producto("Té","1.70€", R.drawable.te),
                new Producto("Triangulo", "1€", R.drawable.triangulo)
        );

        ProductosLiveData = new MutableLiveData<>(allProducts);
        BocatasLiveData = new MutableLiveData<>(bocatas);
        BebidasLiveData = new MutableLiveData<>(bebidas);
        CafesLiveData = new MutableLiveData<>(cafes);

        BaratoLiveData = new MutableLiveData<>(barato);
        CaroLiveData = new MutableLiveData<>(caro);
        AlfabetLiveData = new MutableLiveData<>(alfabet);

        carrito = new ArrayList<>();
        favorito = new ArrayList<>();
    }


    public LiveData<List<Producto>> allProducts() {
        return ProductosLiveData;
    }
    public LiveData<List<Producto>> bocatas() {
        return BocatasLiveData;
    }
    public LiveData<List<Producto>> bebidas() {
        return BebidasLiveData;
    }
    public LiveData<List<Producto>> cafes() {
        return CafesLiveData;
    }

    public LiveData<List<Producto>> barato() {
        return BaratoLiveData;
    }
    public LiveData<List<Producto>> caro() {
        return CaroLiveData;
    }
    public LiveData<List<Producto>> alfabet() {
        return AlfabetLiveData;
    }

    public List<Producto> carrito() {
        return carrito;
    }
    public List<Producto> favorito() {
        return favorito;
    }

    public void anadirAlCarrito(Producto producto) {
        carrito.add(producto);
    }
    public void anadirFavoritos(Producto producto) {
        favorito.add(producto);
    }

    public void eliminarProductoDelCarrito(Producto producto) {
        carrito.remove(producto);
    }
    public void eliminarProductoDeFavorito(Producto producto) {
        favorito.remove(producto);
    }
}
