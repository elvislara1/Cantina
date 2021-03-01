package com.example.proyectocantina;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.example.proyectocantina.databinding.FragmentMostrarProductoBinding;

import es.dmoral.toasty.Toasty;

public class MostrarProductoFragment extends Fragment {

    public FragmentMostrarProductoBinding binding;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMostrarProductoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ProductosViewModel productosViewModel = new ViewModelProvider(requireActivity()).get(ProductosViewModel.class);

        NavController navController = Navigation.findNavController(view);

        productosViewModel.seleccionado().observe(getViewLifecycleOwner(), producto -> {
            Glide.with(MostrarProductoFragment.this).load(producto.idDrawable).into(binding.image);

            binding.nombre.setText(producto.nombre);
            binding.precio.setText(producto.precio);

            binding.addToCart.setOnClickListener(v -> {
                productosViewModel.anadirAlCarrito(producto);
                Toasty.success(getActivity(), "Producto añadido!", Toast.LENGTH_LONG).show();
                navController.popBackStack();
            });

            //COMPRUEBA si El PRODUCTO ESTA EN FAVORITOS
            if (productosViewModel.favorito().contains(producto)){
                binding.corazon.setVisibility(View.VISIBLE);
                binding.corazonb.setVisibility(View.GONE);
            } else{
                binding.corazonb.setVisibility(View.VISIBLE);
                //CUANDO HACE CLICK en el CORAZON ....
                binding.corazonb.setOnClickListener(v -> {
                    productosViewModel.anadirFavoritos(producto);
                    binding.corazon.setVisibility(View.VISIBLE);
                    binding.corazonb.setVisibility(View.GONE);
                    Toasty.success(getActivity(), "Producto añadido a favoritos!", Toast.LENGTH_LONG).show();
                });
            }

            //ELIMINA EL PRODUCTO DE FAVORITOS
            binding.corazon.setOnClickListener(view1 -> {
                binding.corazon.setVisibility(View.GONE);
                binding.corazonb.setVisibility(View.VISIBLE);
                Toasty.info(getActivity(), "Producto eliminado de favoritos", Toast.LENGTH_LONG).show();
                productosViewModel.eliminarProductoDeFavorito(producto);
            });
        });
    }
}
