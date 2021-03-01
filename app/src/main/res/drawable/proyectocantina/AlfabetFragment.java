package com.example.proyectocantina;

import androidx.lifecycle.LiveData;

import java.util.List;


public class AlfabetFragment extends ProductosFragment {

    @Override
    LiveData<List<Producto>> obtenerProductos() {
        return productosViewModel.alfabet();
    }
}