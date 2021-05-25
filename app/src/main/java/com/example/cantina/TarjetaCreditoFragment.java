package com.example.cantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cantina.databinding.FragmentTarjetaCreditoBinding;


public class TarjetaCreditoFragment extends Fragment {

    FragmentTarjetaCreditoBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentTarjetaCreditoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(requireParentFragment());

        binding.cancelar.setOnClickListener(v -> {
            navController.navigate(R.id.action_tarjetaCreditoFragment_to_metodoPagoFragment);
        });
        binding.pagar.setOnClickListener(v -> {
            navController.navigate(R.id.action_tarjetaCreditoFragment_to_compraFinalizadaFragment);
        });
    }
}