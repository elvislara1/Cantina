package com.example.cantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cantina.databinding.FragmentLoMasVendidoBinding;
import com.example.cantina.model.LoMasVendido;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;

public class LoMasVendidoFragment extends Fragment {

    private FragmentLoMasVendidoBinding binding;
    private FirebaseFirestore db;
    private NavController navController;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentLoMasVendidoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        db = FirebaseFirestore.getInstance();

        List<LoMasVendido> productList = Arrays.asList(
                new LoMasVendido("", "Triangulo", 1, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/triangulo.png?alt=media&token=ab6b7bd7-e116-4a03-8f5a-509c0d8180b0"),
                new LoMasVendido("", "Bocata de Bacon", 1.70, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/bacon.png?alt=media&token=57e5f117-67eb-464f-94c8-677bc2ffcc94"),
                new LoMasVendido("", "Bifrutas", 1, "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/bifrutas.png?alt=media&token=4c928d76-be79-4416-b452-cc4fbee8c4b6")
        );

        binding.add.setOnClickListener(v->{
            productList.forEach(producto -> {
                db.collection("loMasVendido").add(producto).addOnSuccessListener(documentReference -> {
                    db.collection("loMasVendido").document(documentReference.getId()).set(producto);
                });
            });
        });
    }
}