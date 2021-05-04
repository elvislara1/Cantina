package com.example.cantina;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class CaroFragment extends ProductosFragment {
    @Override
    Task<QuerySnapshot> obtenerProductos() {
        //return cantinaViewModel.caro();
        return db.collection("productos").orderBy("preciod", Query.Direction.DESCENDING).get();
    }
}