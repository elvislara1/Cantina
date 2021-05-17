package com.example.cantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.example.cantina.databinding.FragmentMetodoPagoBinding;

import es.dmoral.toasty.Toasty;

public class MetodoPagoFragment extends Fragment {
    FragmentMetodoPagoBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentMetodoPagoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(requireParentFragment());
        binding.recibo.setVisibility(View.GONE);
        binding.siguiente.setVisibility(View.GONE);

        binding.tarjetaCredito.setOnClickListener(v -> {
            binding.siguiente.setVisibility(View.VISIBLE);
            binding.seleccionar.setVisibility(View.GONE);
            binding.recibo.setVisibility(View.GONE);

        });
        binding.otros.setOnClickListener(v -> {
            YoYo.with(Techniques.Shake)
                    .duration(1200)
                    .repeat(0)
                    .playOn(view.findViewById(R.id.imagen2));
            YoYo.with(Techniques.Shake)
                    .duration(1200)
                    .repeat(0)
                    .playOn(view.findViewById(R.id.otros));
            Toasty.error(getActivity(), "No disponible.", Toast.LENGTH_LONG).show();
            binding.siguiente.setVisibility(View.GONE);
            binding.seleccionar.setVisibility(View.GONE);
            binding.recibo.setVisibility(View.GONE);

        });
        binding.dineroEfectivo.setOnClickListener(v -> {
            binding.siguiente.setVisibility(View.GONE);
            binding.seleccionar.setVisibility(View.VISIBLE);
            binding.recibo.setVisibility(View.GONE);

        });
        binding.seleccionar.setOnClickListener(v -> {
            Toasty.warning(getActivity(), "Se ha seleccionado en efectivo.", Toast.LENGTH_LONG).show();
            binding.seleccionar.setVisibility(View.GONE);
            binding.recibo.setVisibility(View.VISIBLE);
            binding.siguiente.setVisibility(View.GONE);
        });

        binding.siguiente.setOnClickListener(v -> {
            navController.navigate(R.id.action_metodoPagoFragment_to_tarjetaCreditoFragment);
            binding.seleccionar.setVisibility(View.GONE);
            binding.recibo.setVisibility(View.GONE);
            binding.siguiente.setVisibility(View.VISIBLE);
        });

        binding.recibo.setOnClickListener(v -> {
            navController.navigate(R.id.action_metodoPagoFragment_to_compraFinalizadaFragment);
        });
    }
}