package com.example.cantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cantina.databinding.FragmentNuevoComentarioBinding;
import com.example.cantina.model.Comentario;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Arrays;
import java.util.List;


public class NuevoComentarioFragment extends Fragment {
    private FragmentNuevoComentarioBinding binding;
    private AutenticacionViewModel autenticacionViewModel;
    private String user;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentNuevoComentarioBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CantinaViewModel cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        NavController navController = Navigation.findNavController(view);

        autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);

        autenticacionViewModel.usuarioAutenticado.observe(getViewLifecycleOwner(), usuario -> user = usuario.username);

        binding.nombre.setVisibility(View.GONE);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
/*
        binding.crear.setOnClickListener(v -> {
            String cabecera = binding.cabecera.getText().toString().toUpperCase();
            String comentario = binding.comentario.getText().toString();
            float rating = binding.rating.getRating();

            //Comentario(new Comentario(user, null, null, rating));
            //cantinaViewModel.insertarComentario(user, cabecera, comentario, rating);
            navController.popBackStack();
        });

*/
        db = FirebaseFirestore.getInstance();

        List<Comentario> comentarioList = Arrays.asList(
                new Comentario("Juan", "juan@gmail.com", "https://firebasestorage.googleapis.com/v0/b/cantina-b1018.appspot.com/o/triangulo.png?alt=media&token=ab6b7bd7-e116-4a03-8f5a-509c0d8180b0", "Buenos productos!", "Los mejores triangulos que he probado en mi vida ","21/12/2001", "3.5")
        );

        binding.crear.setOnClickListener(v->{
            comentarioList.forEach(comentario -> {
                db.collection("comunidad").add(comentario).addOnSuccessListener(documentReference -> {
                    db.collection(comentario.comentario).document(documentReference.getId()).set(comentario);
                    navController.popBackStack();
                });
            });
        });
    }
}