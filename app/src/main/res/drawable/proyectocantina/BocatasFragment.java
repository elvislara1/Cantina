package com.example.proyectocantina;

import androidx.lifecycle.LiveData;

import java.util.List;


public class BocatasFragment extends ProductosFragment {

    @Override
    LiveData<List<Producto>> obtenerProductos() {
        return productosViewModel.bocatas();
    }
}

