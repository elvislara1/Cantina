package com.example.cantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentFavoritoBinding;
import com.example.cantina.databinding.ViewholderFavoritoBinding;
import com.example.cantina.model.ProductoFavorito;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

public class FavoritoFragment extends Fragment {

    FragmentFavoritoBinding binding;
    private CantinaViewModel cantinaViewModel;
    AutenticacionViewModel autenticacionViewModel;
    private NavController navController;
    private int userId;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentFavoritoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);

        navController = NavHostFragment.findNavController(requireParentFragment());

        final FavoritoAdapter favoritoAdapter = new FavoritoAdapter();

        //NavController navController = Navigation.findNavController(view);

        autenticacionViewModel.usuarioAutenticado.observe(getViewLifecycleOwner(), usuario -> {
            userId = usuario.id;

            cantinaViewModel.productosFavoritos(usuario.id).observe(getViewLifecycleOwner(), productosList -> {
                if (productosList == null || productosList.size() == 0) {
                    // Log.e("ZERO", "RESULTS");
                    binding.img1.setVisibility(View.VISIBLE);
                    binding.text1.setVisibility(View.VISIBLE);
                    binding.text2.setVisibility(View.VISIBLE);
                    binding.text3.setVisibility(View.VISIBLE);
                } else {
                    binding.img1.setVisibility(View.GONE);
                    binding.text1.setVisibility(View.GONE);
                    binding.text2.setVisibility(View.GONE);
                    binding.text3.setVisibility(View.GONE);
                }
                favoritoAdapter.establecerLista(productosList);
            });
        });

        binding.recyclerView.setAdapter(favoritoAdapter);
        favoritoAdapter.establecerLista(favoritoAdapter.productoList);
    }

    class FavoritoAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        List<ProductoFavorito> productoList;

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderFavoritoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            ProductoFavorito producto = productoList.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(producto.precio);

            Glide.with(FavoritoFragment.this).load(producto.idDrawable).into(holder.binding.foto);
        }

        @Override
        public int getItemCount() {
            return productoList == null ? 0 : productoList.size();
        }

        public void establecerLista(List<ProductoFavorito> producto) {
            this.productoList = producto;
            notifyDataSetChanged();
        }

        public ProductoFavorito obtenerProducto(int posicion) {
            return productoList.get(posicion);
        }
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {
        ViewholderFavoritoBinding binding;

        public ProductoViewHolder(@NonNull ViewholderFavoritoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}