package com.example.proyectocantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectocantina.databinding.FragmentTabbedProductosBinding;
import com.example.proyectocantina.databinding.ViewholderProductoBinding;

import java.util.List;

import static androidx.navigation.Navigation.findNavController;

public abstract class ProductosFragment extends Fragment {

    private FragmentTabbedProductosBinding binding;
    public ProductosViewModel productosViewModel;
    public NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentTabbedProductosBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        productosViewModel = new ViewModelProvider(requireActivity()).get(ProductosViewModel.class);
        navController = NavHostFragment.findNavController(requireParentFragment());

        final ProductosAdapter productosAdapter = new ProductosAdapter();

        binding.recyclerView.setAdapter(productosAdapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

       obtenerProductos().observe(getViewLifecycleOwner(), new Observer<List<Producto>>() {
            @Override
            public void onChanged(List<Producto> productos) {
                productosAdapter.setProductoList(productos);
            }
        });
    }

    abstract LiveData<List<Producto>> obtenerProductos();

    class ProductosAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        List<Producto> productoList;

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderProductoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            Producto producto = productoList.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(producto.precio);

            Glide.with(ProductosFragment.this).load(producto.idDrawable).into(holder.binding.foto);

            holder.binding.add.setOnClickListener(v -> {
                productosViewModel.productoSeleccionado(producto);
                navController.navigate(R.id.action_TabbedFragment_to_mostrarProductoFragment);
            });
        }

        @Override
        public int getItemCount() {
            return productoList == null ? 0 : productoList.size();
        }

        void setProductoList(List<Producto> productoList){
            this.productoList = productoList;
            notifyDataSetChanged();
        }
    }

    class ProductoViewHolder extends RecyclerView.ViewHolder {
        ViewholderProductoBinding binding;

        public ProductoViewHolder(@NonNull ViewholderProductoBinding binding) {
            super(binding.getRoot());

            this.binding = binding;
        }
    }
}
