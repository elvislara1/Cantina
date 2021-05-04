package com.example.cantina;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class TodoFragment extends ProductosFragment {
    @Override
    Task<QuerySnapshot> obtenerProductos() {
        return db.collection("productos").get();
    }
}