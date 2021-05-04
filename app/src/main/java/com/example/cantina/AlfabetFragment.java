package com.example.cantina;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class AlfabetFragment extends ProductosFragment {
    @Override
    Task<QuerySnapshot> obtenerProductos() {
        //return cantinaViewModel.alfabeticamente();
        return db.collection("productos").orderBy("nombre", Query.Direction.ASCENDING).get();
    }
}