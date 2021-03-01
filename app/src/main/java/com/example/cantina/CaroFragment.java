package com.example.cantina;

import androidx.lifecycle.LiveData;

import com.example.cantina.model.Producto;

import java.util.List;

public class CaroFragment extends ProductosFragment {
    @Override
    LiveData<List<Producto>> obtenerProductos() {
        return cantinaViewModel.caro();
    }
}