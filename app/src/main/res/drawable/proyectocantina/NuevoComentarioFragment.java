package com.example.proyectocantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.proyectocantina.databinding.FragmentNuevoComentarioBinding;


public class NuevoComentarioFragment extends Fragment {

    private FragmentNuevoComentarioBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentNuevoComentarioBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ComunidadViewModel comunidadViewModel = new ViewModelProvider(requireActivity()).get(ComunidadViewModel.class);
        NavController navController = Navigation.findNavController(view);

        binding.crear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = binding.nombre.getText().toString();
                String cabecera = binding.cabecera.getText().toString().toUpperCase();
                String comentario = binding.comentario.getText().toString();

                RatingBar ratingBar = (RatingBar) v.findViewById(R.id.rating);
                float rating = binding.rating.getRating();

                comunidadViewModel.insertar(new Comunidad(nombre, cabecera, comentario, rating));
                navController.popBackStack();

            }
        });
    }
}