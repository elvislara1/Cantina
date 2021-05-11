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
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentProductosBinding;
import com.example.cantina.databinding.ViewholderProductoBinding;
import com.example.cantina.model.Producto;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public abstract class ProductosFragment extends Fragment {

    FirebaseFirestore db;
    private FragmentProductosBinding binding;
    NavController navController;
    CantinaViewModel cantinaViewModel;
    private ProductosAdapter productosAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentProductosBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        db = FirebaseFirestore.getInstance();
        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        navController = NavHostFragment.findNavController(requireParentFragment());

        productosAdapter = new ProductosAdapter();

        binding.recyclerView.setAdapter(productosAdapter);
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));



        //obtenerProductos().observe(getViewLifecycleOwner(), productos -> productosAdapter.setProductoList(productos));
       obtenerProductos().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Producto> productos = new ArrayList<>();

            for(QueryDocumentSnapshot qds:queryDocumentSnapshots){
                String img = qds.getString("img");
                String nombre = qds.getString("nombre");
                double precio = qds.getDouble("preciod");
                String categoria = qds.getString("categoria");

                productos.add(new Producto(nombre, precio, img, categoria));
            }
            productosAdapter.setProductoList(productos);
        });

       cargarProductos();
    }

    void cargarProductos(){
        obtenerProductos().addOnSuccessListener(queryDocumentSnapshots -> {
            List<Producto> productos = new ArrayList<>();

            for(QueryDocumentSnapshot qds:queryDocumentSnapshots){
                String img = qds.getString("img");
                String nombre = qds.getString("nombre");
                double precio = qds.getDouble("preciod");
                String categoria = qds.getString("categoria");

                productos.add(new Producto(nombre, precio, img, categoria));
            }
            productosAdapter.setProductoList(productos);
        });
    }

    abstract Task<QuerySnapshot> obtenerProductos();

    class ProductosAdapter extends RecyclerView.Adapter<ProductosViewHolder> {

        List<Producto> productoList;
        private String numberFormatter;

        @NonNull
        @Override
        public ProductosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductosViewHolder(ViewholderProductoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductosViewHolder holder, int position) {
            Producto producto = productoList.get(position);

            holder.binding.nombre.setText(producto.nombre);
            String finalresult = String.valueOf(producto.preciod);
            String formattedValue = numberFormatter.format(finalresult + "€");
            holder.binding.precio.setText(formattedValue);

            Glide.with(ProductosFragment.this)
                    .load(producto.img)
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