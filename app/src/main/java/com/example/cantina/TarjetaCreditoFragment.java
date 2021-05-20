package com.example.cantina;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cantina.databinding.FragmentMetodoPagoBinding;
import com.example.cantina.databinding.FragmentTarjetaCreditoBinding;
import com.example.cantina.viewmodel.AutenticacionViewModel;

import es.dmoral.toasty.Toasty;


public class TarjetaCreditoFragment extends Fragment {

    FragmentTarjetaCreditoBinding binding;
    private AutenticacionViewModel autenticacionViewModel;
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

        autenticacionViewModel.estadoDeLaAutenticacion.observe(getViewLifecycleOwner(), estadoDeLaAutenticacion -> {
            switch (estadoDeLaAutenticacion){
                case AUTENTICADO:
                    binding.pagar.setOnClickListener(v -> {
                        navController.navigate(R.id.action_tarjetaCreditoFragment_to_compraFinalizadaFragment);
                    });
                    break;

                case AUTENTICACION_INVALIDA:
                    Toast.makeText(getContext(), "CREDENCIALES NO VALIDAS", Toast.LENGTH_SHORT).show();
                    break;
            }
        });

        binding.cancelar.setOnClickListener(v -> {
            navController.navigate(R.id.action_tarjetaCreditoFragment_to_metodoPagoFragment);
        });

    }
}