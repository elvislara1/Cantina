package com.example.proyectocantina;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.proyectocantina.databinding.ActivityMainBinding;
import com.example.proyectocantina.databinding.FragmentCompraFinalizadaBinding;
import com.example.proyectocantina.databinding.ViewholderCompraFinalizadaBinding;
import com.github.jinatonic.confetti.CommonConfetti;

import java.util.List;

import static com.example.proyectocantina.R.drawable.ic_add;

public class CompraFinalizadaFragment extends Fragment {

    FragmentCompraFinalizadaBinding binding;
    private ProductosViewModel productosViewModel;
    private NavController navController;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentCompraFinalizadaBinding.inflate(inflater, container, false)).getRoot();
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        productosViewModel = new ViewModelProvider(requireActivity()).get(ProductosViewModel.class);
        navController = NavHostFragment.findNavController(requireParentFragment());

        final CarritoAdapter carritoAdapter = new CarritoAdapter();

        //NavController navController = Navigation.findNavController(view);

        binding.recyclerView.setAdapter(carritoAdapter);
        carritoAdapter.establecerLista(productosViewModel.carrito());

        //logo textConfirmada textGracias numPedido importante volverAlMenu
        faltaAceptarLaCompra();


        binding.confirmarCompra.setOnClickListener(view1 -> {
            compraAceptada();
            CommonConfetti.rainingConfetti((ViewGroup) view, new int[] { Color.BLUE, Color.YELLOW , Color.RED })
                    .stream(3000);
        });
        binding.volverAlMenu.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_CompraFinalizadaFragment_to_TabbedFragment);
            productosViewModel.carrito().clear();
        });
    }
    public void faltaAceptarLaCompra(){
        //logo textConfirmada textGracias numPedido importante volverAlMenu
        binding.logo.setVisibility(View.GONE);
        binding.textConfirmada.setVisibility(View.GONE);
        binding.textGracias.setVisibility(View.GONE);
        binding.importante.setVisibility(View.GONE);
        binding.volverAlMenu.setVisibility(View.GONE);
    }

    public void compraAceptada() {
        //logo textConfirmada textGracias numPedido importante volverAlMenu
        binding.logo.setVisibility(View.VISIBLE);
        binding.textConfirmada.setVisibility(View.VISIBLE);
        binding.textGracias.setVisibility(View.VISIBLE);
        binding.importante.setVisibility(View.VISIBLE);
        binding.volverAlMenu.setVisibility(View.VISIBLE);
        binding.confirmarCompra.setVisibility(View.GONE);
    }
    class CarritoAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        List<Producto> productoList;

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderCompraFinalizadaBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            Producto producto = productoList.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(producto.precio);
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
        ViewholderCompraFinalizadaBinding binding;

        public ProductoViewHolder(@NonNull ViewholderCompraFinalizadaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}