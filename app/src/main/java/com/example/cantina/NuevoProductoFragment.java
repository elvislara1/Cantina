package com.example.cantina;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentNuevoProductoBinding;
import com.example.cantina.viewmodel.CantinaViewModel;

public class NuevoProductoFragment extends Fragment {

    private FragmentNuevoProductoBinding binding;
    private NavController navController;
    private CantinaViewModel cantinaViewModel;

    private Uri imagenSeleccionada;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentNuevoProductoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        navController = Navigation.findNavController(view);

        binding.insertar.setOnClickListener(v -> {
            if (imagenSeleccionada == null) {
                Toast.makeText(requireContext(), "Seleccione una imagen", Toast.LENGTH_SHORT).show();
                return;
            } else if (binding.nombre.getText().toString().isEmpty()) {
                binding.nombre.setError("Introduzca el titulo");
                return;
            } else if (binding.precio.getText().toString().isEmpty()) {
                binding.precio.setError("Introduzca el año");
                return;
            } else if (binding.tipo.getText().toString().isEmpty()) {
                binding.tipo.setError("Introduzca el tipo de producto (D,C,S)");
                return;
            }
            String nombre = binding.nombre.getText().toString();
            String precio = binding.precio.getText().toString();
            String tipo = binding.tipo.getText().toString();

            //cantinaViewModel.insertarProducto(nombre, precio, imagenSeleccionada.toString(), tipo);

            cantinaViewModel.establecerImagenSeleccionada(null);
            navController.popBackStack();
        });

        binding.portada.setOnClickListener(v -> {
            lanzadorGaleria.launch("image/*");
        });

        cantinaViewModel.imagenSeleccionada.observe(getViewLifecycleOwner(), uri -> {
            if (uri != null) {
                imagenSeleccionada = uri;
                Glide.with(requireView()).load(uri).into(binding.portada);
            }
        });
    }

    private final ActivityResultLauncher<String> lanzadorGaleria = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        cantinaViewModel.establecerImagenSeleccionada(uri);
    });
}