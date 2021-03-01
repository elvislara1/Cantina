package com.example.cantina;

import androidx.lifecycle.LiveData;

import com.example.cantina.model.Producto;

import java.util.List;

public class BocatasFragment extends ProductosFragment {
    @Override
    LiveData<List<Producto>> obtenerProductos() {
        return cantinaViewModel.obtenerBocatas();
    }
}