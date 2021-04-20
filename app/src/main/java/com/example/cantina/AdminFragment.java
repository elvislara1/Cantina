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
                new Producto("Triangulo", 3, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/triangulo.png?alt=media&token=ab6b7bd7-e116-4a03-8f5a-509c0d8180b0", "colesterol"),
                new Producto("Cafe", 2, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/triangulo.png?alt=media&token=ab6b7bd7-e116-4a03-8f5a-509c0d8180b0", "bebidas")
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