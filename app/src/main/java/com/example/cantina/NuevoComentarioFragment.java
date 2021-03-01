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
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;


public class NuevoComentarioFragment extends Fragment {
    private FragmentNuevoComentarioBinding binding;
    private AutenticacionViewModel autenticacionViewModel;
    private String user;

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

        binding.crear.setOnClickListener(v -> {
            String cabecera = binding.cabecera.getText().toString().toUpperCase();
            String comentario = binding.comentario.getText().toString();
            float rating = binding.rating.getRating();

            cantinaViewModel.insertarComentario(user, cabecera, comentario, rating);
            navController.popBackStack();
        });
    }
}