package com.example.proyectocantina;

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

import com.example.proyectocantina.databinding.FragmentCheckoutBinding;

import es.dmoral.toasty.Toasty;

public class CheckoutFragment extends Fragment {

    FragmentCheckoutBinding binding;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentCheckoutBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = NavHostFragment.findNavController(requireParentFragment());

        binding.comerIn.setOnClickListener(v -> {
            Toasty.error(getActivity(), "Las nuevas restricciones que ha impuesto el Gobierno Español dejarán a los bares y restaurantes sin servicio dentro del local.", Toast.LENGTH_LONG).show();
            binding.siguiente.setVisibility(View.GONE);
        });

        binding.comerOut.setOnClickListener(view1 -> {
            binding.siguiente.setVisibility(View.VISIBLE);
        });

        binding.siguiente.setOnClickListener(v -> {
            navController.navigate(R.id.action_CheckoutFragment_to_MetodoPagoFragment);
        });
    }
}