package com.example.cantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.cantina.databinding.FragmentAdminBinding;
import com.example.cantina.model.Producto;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;


public class AdminFragment extends Fragment {
    private FragmentAdminBinding binding;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentAdminBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

        List<Producto> productList = Arrays.asList(
                new Producto("Triangulo", 1, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/triangulo.png?alt=media&token=ab6b7bd7-e116-4a03-8f5a-509c0d8180b0", "bollos"),
                new Producto("Agua", 0.75, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/agua.jpg?alt=media&token=ead23b11-034b-4585-9f03-144e33bb8db7", "bebidas"),
                new Producto("Zumo de manzana", 0.75, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/manzana.jpg?alt=media&token=d3885fc4-0e12-4b2b-86c6-017ccb45e70e", "bebidas"),
                new Producto("Té", 1.70, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/te.jpg?alt=media&token=8cd737a9-95d2-4d0e-9cf1-852cb1439961", "cafe"),
                new Producto("Bifrutas", 1, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/bifrutas.png?alt=media&token=4c928d76-be79-4416-b452-cc4fbee8c4b6", "bebidas"),
                new Producto("Panini", 1.30, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/panini.jpg?alt=media&token=699c842f-8c18-408d-b359-d498c385ba75", "bocatas"),
                new Producto("Bocata de Bacon", 1.70, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/bacon.png?alt=media&token=57e5f117-67eb-464f-94c8-677bc2ffcc94", "bocatas"),
                new Producto("Bocata de Tortilla", 1, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/tortilla.png?alt=media&token=1ed03ea1-d05c-4c7b-b51d-68be0195aaf6", "bocatas"),
                new Producto("Bocata de Frankfurt", 1.20, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/frankf.jpg?alt=media&token=b09b5f6e-cd5b-4cf7-b571-b258b1facb18", "bocatas"),
                new Producto("Croissant", 1, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/croissant.jpg?alt=media&token=cc2d370f-9f7c-4e28-995b-f27aab8f391c", "bollos"),
                new Producto("Croissant de chocolate", 1, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/choco.png?alt=media&token=fb0006f9-3b74-4f18-b29f-b1fed4a0c963", "bollos"),
                new Producto("Capuccino", 1.60, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/capuchino.png?alt=media&token=a8a14503-e529-4d84-bc34-a1bdf3ec32b8", "cafe"),
                new Producto("Cafe con leche", 1.80, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/cafelechee.jpg?alt=media&token=a57db0ee-128c-40e2-93bc-faf333951618", "cafe"),
                new Producto("Cafe", 1.50, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/triangulo.png?alt=media&token=ab6b7bd7-e116-4a03-8f5a-509c0d8180b0", "cafe")
        );

        binding.button.setOnClickListener(v->{
            productList.forEach(producto -> {
                db.collection("productos").add(producto).addOnSuccessListener(documentReference -> {
                    db.collection(producto.tipo).document(documentReference.getId()).set(producto);
                });
            });
        });
    }
}