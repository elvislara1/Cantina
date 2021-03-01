package com.example.cantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentProductosBinding;
import com.example.cantina.databinding.ViewholderProductoBinding;
import com.example.cantina.model.Producto;
import com.example.cantina.viewmodel.CantinaViewModel;

import java.util.List;

public abstract class ProductosFragment extends Fragment {

    private FragmentProductosBinding binding;
    NavController navController;
    CantinaViewModel cantinaViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentProductosBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        navController = NavHostFragment.findNavController(requireParentFragment());

        final ProductosAdapter productosAdapter = new ProductosAdapter();

        binding.recyclerView.setAdapter(productosAdapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        binding.irAInsertarProducto.setOnClickListener(v -> {
            navController.navigate(R.id.action_nuevoProductoFragment);
        });

        obtenerProductos().observe(getViewLifecycleOwner(), productos -> productosAdapter.setProductoList(productos));
    }
    abstract LiveData<List<Producto>> obtenerProductos();

    class ProductosAdapter extends RecyclerView.Adapter<ProductosViewHolder> {

        List<Producto> productoList;

        @NonNull
        @Override
        public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductosViewHolder(ViewholderProductoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductosViewHolder holder, int position) {
            Producto producto = productoList.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(producto.precio);
            Glide.with(ProductosFragment.this)
                    .load(producto.idDrawable)
                    .into(holder.binding.foto);

            holder.binding.add.setOnClickListener(v -> {
                cantinaViewModel.seleccionar(producto);
                navController.navigate(R.id.action_mostrarFragment);
            });


        }

        @Override
        public int getItemCount() {
            return productoList == null ? 0 : productoList.size();
        }

        public void setProductoList(List<Producto> productoList) {
            this.productoList = productoList;
            notifyDataSetChanged();
        }
    }

    static class ProductosViewHolder extends RecyclerView.ViewHolder {
        ViewholderProductoBinding binding;

        public ProductosViewHolder(@NonNull ViewholderProductoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}