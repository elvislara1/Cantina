package com.example.cantina;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public class BollosFragment extends ProductosFragment {

    @Override
    Task<QuerySnapshot> obtenerProductos() {
        return db.collection("bollos").get();
    }
}