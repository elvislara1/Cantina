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
import com.example.cantina.model.LineaCarrito;
import com.example.cantina.model.ProductoEnCarrito;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import es.dmoral.toasty.Toasty;

public class MostrarFragment extends Fragment {

    public FragmentMostrarBinding binding;
    public AutenticacionViewModel autenticacionViewModel;
    public CantinaViewModel cantinaViewModel;

    private NavController navController;
    private FirebaseUser user;
    private FirebaseFirestore mDb;
    private int cantidad = 0;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMostrarBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(view);

        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);

        //usuario = autenticacionViewModel.usuarioAutenticado.getValue();
        //userId = usuario.id;

        cantinaViewModel.seleccionado().observe(getViewLifecycleOwner(), producto -> {
            Glide.with(MostrarFragment.this).load(producto.img).into(binding.image);

            binding.nombre.setText(producto.nombre);
            binding.precio.setText(producto.precio);

            binding.increment.setOnClickListener(v -> {
                cantidad++;
                if(cantidad > 2){
                    cantidad = 3;
                }
                binding.cantidad.setText(String.valueOf(cantidad));
            });

            binding.decrement.setOnClickListener(v -> {
                cantidad--;
                if(cantidad < 0){
                    cantidad = 0;
                }
                binding.cantidad.setText(String.valueOf(cantidad));
            });

            binding.addToCart.setOnClickListener(v -> {
                if (cantidad == 0){
                    Toasty.error(requireActivity(), "¡Selecciona una cantidad!", Toast.LENGTH_LONG).show();
                } else if (cantidad == 1 || cantidad == 2 || cantidad == 3){
                    anadirAlCarrito(user.getUid());
                    mDb.collection("carrito").document(user.getUid()).collection("productoEnCarrito")
                            .add((new ProductoEnCarrito(producto.productoId, producto.nombre, producto.preciod, producto.img, String.valueOf(cantidad))));
                    navController.popBackStack();

                    Toasty.success(requireActivity(), "¡Producto añadido!", Toast.LENGTH_LONG).show();
                }
            });
            /*
            FAVORITO
            cantinaViewModel.isFavorite(userId, productoId).observe(getViewLifecycleOwner(), integer3 -> {
                fav = integer3;

                if (fav == 1){
                    //is favorite
                    binding.corazon.setVisibility(View.VISIBLE);
                }else{
                    //not favorite
                    binding.corazonb.setVisibility(View.VISIBLE);
                    binding.corazonb.setOnClickListener(v -> {
                        //cantinaViewModel.anadirFavorito(userId, producto.productoId);
                        binding.corazon.setVisibility(View.VISIBLE);
                        binding.corazonb.setVisibility(View.GONE);
                        Toasty.success(getActivity(), "Producto añadido a favoritos!",Toast.LENGTH_LONG).show();
                    });
                }
            });

             */
        });
    }

    private void anadirAlCarrito(String uid) {
        mDb.collection("carrito")
                .add(new LineaCarrito(uid));
    }
}