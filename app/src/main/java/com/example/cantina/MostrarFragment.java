package com.example.cantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentMostrarBinding;
import com.example.cantina.model.ProductoFavorito;
import com.example.cantina.model.Usuario;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import es.dmoral.toasty.Toasty;

public class MostrarFragment extends Fragment {

    public FragmentMostrarBinding binding;
    public AutenticacionViewModel autenticacionViewModel;
    public CantinaViewModel cantinaViewModel;
    private Usuario usuario;
    private NavController navController;
    private FirebaseUser user;

    private int userId;
    private int productoId;
    private Integer fav;
    private ProductoFavorito productoFavorito;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMostrarBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);
        navController = Navigation.findNavController(view);
        user = FirebaseAuth.getInstance().getCurrentUser();

        usuario = autenticacionViewModel.usuarioAutenticado.getValue();
        userId = usuario.id;

        cantinaViewModel.seleccionado().observe(getViewLifecycleOwner(), producto -> {
            Glide.with(MostrarFragment.this).load(producto.img).into(binding.image);

            binding.nombre.setText(producto.nombre);
            binding.precio.setText(producto.precio);

            binding.addToCart.setOnClickListener(v -> {
                cantinaViewModel.anadirAlCarrito(userId, producto.productoId);
                Toasty.success(getActivity(), "Producto añadido!", Toast.LENGTH_LONG).show();
                navController.popBackStack();
            });


            cantinaViewModel.isFavorite(userId, productoId).observe(getViewLifecycleOwner(), integer3 -> {
                fav = integer3;

                if (fav == 1){
                    //is favorite
                    binding.corazon.setVisibility(View.VISIBLE);
                }else{
                    //not favorite
                    binding.corazonb.setVisibility(View.VISIBLE);
                    binding.corazonb.setOnClickListener(v -> {
                        cantinaViewModel.anadirFavorito(userId, producto.productoId);
                        binding.corazon.setVisibility(View.VISIBLE);
                        binding.corazonb.setVisibility(View.GONE);
                        Toasty.success(getActivity(), "Producto añadido a favoritos!",Toast.LENGTH_LONG).show();
                    });
                }
            });
        });
    }
}