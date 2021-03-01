package com.example.proyectocantina;

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
import com.example.proyectocantina.databinding.FragmentBottomFavoritoBinding;
import com.example.proyectocantina.databinding.ViewholderFavoritoBinding;

import java.util.List;

public class FavoritoFragment extends Fragment {

    FragmentBottomFavoritoBinding binding;
    private ProductosViewModel productosViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentBottomFavoritoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productosViewModel = new ViewModelProvider(requireActivity()).get(ProductosViewModel.class);
        navController = NavHostFragment.findNavController(requireParentFragment());

        final FavoritoAdapter favoritoAdapter = new FavoritoAdapter();

        //NavController navController = Navigation.findNavController(view);

        if (favoritoAdapter.productoList == null){
            binding.img1.setVisibility(View.VISIBLE);
            binding.text1.setVisibility(View.VISIBLE);
            binding.text2.setVisibility(View.VISIBLE);
            binding.text3.setVisibility(View.VISIBLE);
        }

        binding.recyclerView.setAdapter(favoritoAdapter);
        favoritoAdapter.establecerLista(productosViewModel.favorito());
    }

    class FavoritoAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        List<Producto> productoList;

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderFavoritoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            Producto producto = productoList.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(producto.precio);

            if (productoList.size() >= 1) {
                binding.img1.setVisibility(View.GONE);
                binding.text1.setVisibility(View.GONE);
                binding.text2.setVisibility(View.GONE);
                binding.text3.setVisibility(View.GONE);
            }

            Glide.with(FavoritoFragment.this).load(producto.idDrawable).into(holder.binding.foto);
        }

        @Override
        public int getItemCount() {
            return productoList == null ? 0 : productoList.size();
        }

        public void establecerLista(List<Producto> carrito) {
            this.productoList = carrito;
            notifyDataSetChanged();
        }

        public Producto obtenerProducto(int posicion) {
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
