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
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentCarritoBinding;
import com.example.cantina.databinding.ViewholderCarritoBinding;
import com.example.cantina.model.ProductoEnCarrito;
import com.example.cantina.model.Usuario;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class CarritoFragment extends Fragment {

    FragmentCarritoBinding binding;
    private CantinaViewModel cantinaViewModel;
    private NavController navController;
    public AutenticacionViewModel autenticacionViewModel;
    private Usuario usuario;
    private int userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentCarritoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        navController = NavHostFragment.findNavController(requireParentFragment());

        final CarritoAdapter carritoAdapter = new CarritoAdapter();

        carritoVacio();

        //NavController navController = Navigation.findNavController(view);
        binding.volver.setOnClickListener(v -> {
            navController.popBackStack();
        });

        binding.checkout.setOnClickListener(v -> {
            navController.navigate(R.id.action_checkoutFragment);
        });

        binding.recyclerView.setAdapter(carritoAdapter);

        //autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);
        navController = Navigation.findNavController(view);

        //usuario = autenticacionViewModel.usuarioAutenticado.getValue();
        //userId = usuario.id;

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
                ProductoEnCarrito producto = carritoAdapter.obtenerProducto(posicion);
                cantinaViewModel.eliminarProductoDelCarrito(userId, producto.productoId);
                Toasty.info(getActivity(), "Producto eliminado", Toast.LENGTH_LONG).show();
                if (carritoAdapter.productoEnCarrito.size() < 1) {
                    carritoVacio();
                }
            }
        }).attachToRecyclerView(binding.recyclerView);
        cantinaViewModel.carrito(userId).observe(getViewLifecycleOwner(), producto -> carritoAdapter.establecerLista(producto));
    }

    public void carritoVacio(){
        binding.img.setVisibility(View.VISIBLE);
        binding.text1.setVisibility(View.VISIBLE);
        binding.text2.setVisibility(View.VISIBLE);
        binding.volver.setVisibility(View.VISIBLE);
        binding.checkout.setVisibility(View.GONE);
    }
    public void carritoConProdutos(){
        binding.img.setVisibility(View.GONE);
        binding.text1.setVisibility(View.GONE);
        binding.text2.setVisibility(View.GONE);
        binding.volver.setVisibility(View.GONE);
        binding.checkout.setVisibility(View.VISIBLE);
    }

    class CarritoAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        List<ProductoEnCarrito> productoEnCarrito;

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderCarritoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            ProductoEnCarrito producto = productoEnCarrito.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(producto.precio);
            if (productoEnCarrito.size() >= 1) carritoConProdutos();
            Glide.with(CarritoFragment.this).load(producto.idDrawable).into(holder.binding.imagen);
        }

        @Override
        public int getItemCount() {
            return productoEnCarrito == null ? 0 : productoEnCarrito.size();
        }

        public void establecerLista(List<ProductoEnCarrito> productoEnCarrito) {
            this.productoEnCarrito = productoEnCarrito;
            notifyDataSetChanged();
        }

        public ProductoEnCarrito obtenerProducto(int posicion) {
            return productoEnCarrito.get(posicion);
        }
    }

    static class ProductoViewHolder extends RecyclerView.ViewHolder {
        ViewholderCarritoBinding binding;

        public ProductoViewHolder(@NonNull ViewholderCarritoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}