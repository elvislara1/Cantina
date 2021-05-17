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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;


public class NuevoComentarioFragment extends Fragment {
    private FragmentNuevoComentarioBinding binding;
    private AutenticacionViewModel autenticacionViewModel;
    private String n;
    private FirebaseFirestore db;
    private FirebaseUser user;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentNuevoComentarioBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        NavController navController = Navigation.findNavController(view);

        autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);

        autenticacionViewModel.usuarioAutenticado.observe(getViewLifecycleOwner(), usuario -> n = usuario.username);

        binding.nombre.setVisibility(View.GONE);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        binding.crear.setOnClickListener(v->{
            String cabecera = binding.cabecera.getText().toString().toUpperCase();
            String coment = binding.comentario.getText().toString();
            float stars = binding.rating.getRating();
            String rating = String.valueOf(stars);
            String date = new SimpleDateFormat("dd-MM-yyyy").format(new Date());

            db.collection("comunidad")
                    .add(new Comentario(user.getEmail(), user.getDisplayName(), user.getPhotoUrl().toString(), cabecera, coment, date, rating));
            navController.navigate(R.id.comunidadFragment);
        });
    }
}