package com.example.proyectocantina;

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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.proyectocantina.databinding.FragmentBottomCarritoBinding;
import com.example.proyectocantina.databinding.ViewholderCarritoBinding;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CarritoFragment extends Fragment {

    FragmentBottomCarritoBinding binding;
    private ProductosViewModel productosViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentBottomCarritoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productosViewModel = new ViewModelProvider(requireActivity()).get(ProductosViewModel.class);
        navController = NavHostFragment.findNavController(requireParentFragment());

        final CarritoAdapter carritoAdapter = new CarritoAdapter();

        //NavController navController = Navigation.findNavController(view);
        binding.volver.setOnClickListener(v -> {
            navController.navigate(R.id.action_CarritoFragment_to_TabbedFragment);
        });

        carritoVacio();

        binding.checkout.setOnClickListener(v -> {
            navController.navigate(R.id.action_CarritoFragment_to_CheckoutFragment);
        });

        binding.recyclerView.setAdapter(carritoAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT  | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int posicion = viewHolder.getAdapterPosition();
                Producto producto = carritoAdapter.obtenerProducto(posicion);
                productosViewModel.eliminarProductoDelCarrito(producto);
                Toasty.info(getActivity(), "Producto eliminado", Toast.LENGTH_LONG).show();

                if (carritoAdapter.productoList.size() == 0) carritoVacio();
            }
        }).attachToRecyclerView(binding.recyclerView);
        carritoAdapter.establecerLista(productosViewModel.carrito());
    }

    public void carritoVacio(){
        final CarritoAdapter carritoAdapter = new CarritoAdapter();

        binding.img.setVisibility(View.VISIBLE);
        binding.text1.setVisibility(View.VISIBLE);
        binding.text2.setVisibility(View.VISIBLE);
        binding.volver.setVisibility(View.VISIBLE);
        binding.checkout.setVisibility(View.GONE);
    }
    public void carritoConProdutos(){
        final CarritoAdapter carritoAdapter = new CarritoAdapter();

        binding.img.setVisibility(View.GONE);
        binding.text1.setVisibility(View.GONE);
        binding.text2.setVisibility(View.GONE);
        binding.volver.setVisibility(View.GONE);
        binding.checkout.setVisibility(View.VISIBLE);
    }
    class CarritoAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        List<Producto> productoList;

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderCarritoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            Producto producto = productoList.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(producto.precio);
            // != null
            if (productoList.size() >= 1) carritoConProdutos();
            Glide.with(CarritoFragment.this).load(producto.idDrawable).into(holder.binding.imagen);
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
        ViewholderCarritoBinding binding;

        public ProductoViewHolder(@NonNull ViewholderCarritoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
