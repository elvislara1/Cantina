package com.example.cantina;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class SearchFragment extends ProductosFragment {
    String ts = "";
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cantinaViewModel.terminoBusqueda.observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                ts = s;
                cargarProductos();
            }
        });
    }

    @Override
    Task<QuerySnapshot> obtenerProductos() {
        return db.collection("productos").whereGreaterThanOrEqualTo("nombre", ts).get();
    }
}
