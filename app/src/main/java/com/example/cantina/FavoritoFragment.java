package com.example.cantina;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentFavoritoBinding;
import com.example.cantina.databinding.ViewholderFavoritoBinding;
import com.example.cantina.model.ProductoFavorito;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavoritoFragment extends Fragment {

    FragmentFavoritoBinding binding;
    private CantinaViewModel cantinaViewModel;
    AutenticacionViewModel autenticacionViewModel;
    private NavController navController;
    private FirebaseUser user;
    private FirebaseFirestore mDb;

    private List<ProductoFavorito> productoFavorito = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentFavoritoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        navController = NavHostFragment.findNavController(requireParentFragment());

        final FavoritoAdapter favoritoAdapter = new FavoritoAdapter();

        /*autenticacionViewModel.usuarioAutenticado.observe(getViewLifecycleOwner(), usuario -> {
            userId = usuario.id;
            /*
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
        */
        binding.eo.setVisibility(View.GONE);
        binding.recyclerView.setAdapter(favoritoAdapter);

        mDb.collection("favorito").document(user.getUid()).collection("productoFavorito").addSnapshotListener((value, error) -> {
            for (QueryDocumentSnapshot m : value) {
                productoFavorito.add(new ProductoFavorito(m));
            }
            favoritoAdapter.notifyDataSetChanged();
            binding.recyclerView.scrollToPosition(productoFavorito.size() - 1);
            if (productoFavorito.size() < 1) {
                NoHayFav();
            }
        });
    }

    public void NoHayFav(){
        binding.img1.setVisibility(View.VISIBLE);
        binding.text1.setVisibility(View.VISIBLE);
        binding.text2.setVisibility(View.VISIBLE);
        binding.text3.setVisibility(View.VISIBLE);
    }
    public void Hayfav(){
        binding.img1.setVisibility(View.GONE);
        binding.text1.setVisibility(View.GONE);
        binding.text2.setVisibility(View.GONE);
        binding.text3.setVisibility(View.GONE);
    }

    class FavoritoAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderFavoritoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            ProductoFavorito producto = productoFavorito.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(String.format("%.2f €", producto.precio));
            if (productoFavorito.size() >= 1) Hayfav();
            Glide.with(FavoritoFragment.this).load(producto.idDrawable).into(holder.binding.foto);
        }

        @Override
        public int getItemCount() {
            return productoFavorito == null ? 0 : productoFavorito.size();
        }

        public ProductoFavorito obtenerProducto(int posicion) {
            return productoFavorito.get(posicion);
        }
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ViewholderFavoritoBinding binding;

        public ProductoViewHolder(@NonNull ViewholderFavoritoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}