package com.example.cantina;

import android.annotation.SuppressLint;
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
import com.example.cantina.model.ProductoFavorito;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class MostrarFragment extends Fragment {

    public FragmentMostrarBinding binding;
    public AutenticacionViewModel autenticacionViewModel;
    public CantinaViewModel cantinaViewModel;

    private NavController navController;
    private FirebaseUser user;
    private FirebaseFirestore mDb;
    private int cantidad = 0;

    private List<ProductoFavorito> productoFavorito = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentMostrarBinding.inflate(inflater, container, false)).getRoot();
    }

    @SuppressLint({"UseCompatLoadingForDrawables", "DefaultLocale"})
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
            binding.precio.setText(String.format("%.2f €", producto.preciod));


            binding.increment.setOnClickListener(v -> {
                cantidad++;
                if(cantidad > 2){
                    cantidad = 3;
                    binding.decrement.setBackgroundResource(R.drawable.button_rounded);
                    binding.increment.setBackgroundResource(R.drawable.button_decrement);
                } else if (cantidad == 1 || cantidad == 2){
                    binding.decrement.setBackgroundResource(R.drawable.button_rounded);
                    binding.increment.setBackgroundResource(R.drawable.button_rounded);
                }
                binding.cantidad.setText(String.valueOf(cantidad));
            });

            binding.decrement.setOnClickListener(v -> {
                cantidad--;
                if(cantidad < 1){
                    cantidad = 0;
                    binding.decrement.setBackgroundResource(R.drawable.button_decrement);
                    binding.increment.setBackgroundResource(R.drawable.button_rounded);
                } else if (cantidad == 1 || cantidad == 2){
                binding.decrement.setBackgroundResource(R.drawable.button_rounded);
                binding.increment.setBackgroundResource(R.drawable.button_rounded);
            }
                binding.cantidad.setText(String.valueOf(cantidad));
            });

            binding.addToCart.setOnClickListener(v -> {
                if (cantidad == 0){
                    Toasty.error(requireActivity(), "¡Selecciona una cantidad!", Toast.LENGTH_LONG).show();
                } else if (cantidad == 1 || cantidad == 2 || cantidad == 3){
                    anadirAlCarrito(user.getUid());
                    mDb.collection("carrito").document(user.getUid()).collection("productoEnCarrito")
                            .document(producto.productoId).set((new ProductoEnCarrito(producto.productoId, producto.nombre, producto.preciod, producto.img, String.valueOf(cantidad))));
                    navController.popBackStack();

                    Toasty.success(requireActivity(), "¡Producto añadido!", Toast.LENGTH_LONG).show();
                }
            });

            binding.corazonb.setOnClickListener(v -> {
                binding.corazonb.setVisibility(View.GONE);
                binding.corazon.setVisibility(View.VISIBLE);
                mDb.collection("favorito").document(user.getUid()).collection("productoFavorito")
                        .document(producto.productoId).set((new ProductoFavorito(producto.productoId, producto.nombre, producto.preciod, producto.img)));
                Toasty.success(getActivity(), "¡Producto añadido a favoritos!",Toast.LENGTH_LONG).show();
            });

            binding.corazon.setOnClickListener(v -> {
                binding.corazon.setVisibility(View.GONE);
                binding.corazonb.setVisibility(View.VISIBLE);
                mDb.collection("favorito").document(user.getUid()).collection("productoFavorito").document(producto.productoId).delete();
                Toasty.success(getActivity(), "Producto quitado de favorito",Toast.LENGTH_LONG).show();
            });

            CollectionReference reference = mDb.collection("favorito").document(user.getUid()).collection("productoFavorito");
            reference.whereEqualTo("productoId",producto.productoId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if (document.exists()) {
                            binding.corazon.setVisibility(View.VISIBLE);
                            binding.corazonb.setVisibility(View.GONE);
                        }
                    }
                } else{
                    binding.corazonb.setVisibility(View.VISIBLE);
                }
            });
        });
    }


    private void anadirAlCarrito(String uid) {
        mDb.collection("carrito")
                .add(new LineaCarrito(uid));
    }
}