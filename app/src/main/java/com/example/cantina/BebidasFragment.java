package com.example.cantina;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class BebidasFragment extends ProductosFragment {
    @Override
    Task<QuerySnapshot> obtenerProductos() {
        return db.collection("bebidas").get();
    }
}