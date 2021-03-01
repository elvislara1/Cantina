package com.example.cantina;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.databinding.FragmentCompraFinalizadaBinding;
import com.example.cantina.databinding.ViewholderCompraFinalizadaBinding;
import com.example.cantina.model.ProductoEnCarrito;
import com.example.cantina.model.Usuario;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.github.jinatonic.confetti.CommonConfetti;

import java.util.List;

public class CompraFinalizadaFragment extends Fragment {

    FragmentCompraFinalizadaBinding binding;
    private CantinaViewModel cantinaViewModel;
    private NavController navController;
    public AutenticacionViewModel autenticacionViewModel;
    private Usuario usuario;
    private int userId;

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
        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        navController = NavHostFragment.findNavController(requireParentFragment());

        autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);
        navController = Navigation.findNavController(view);

        usuario = autenticacionViewModel.usuarioAutenticado.getValue();
        userId = usuario.id;

        final CarritoAdapter carritoAdapter = new CarritoAdapter();
        
        binding.recyclerView.setAdapter(carritoAdapter);
        cantinaViewModel.carrito(userId).observe(getViewLifecycleOwner(), producto -> carritoAdapter.establecerLista(producto));

        //logo textConfirmada textGracias numPedido importante volverAlMenu
        faltaAceptarLaCompra();

        binding.confirmarCompra.setOnClickListener(view1 -> {
            compraAceptada();
            CommonConfetti.rainingConfetti((ViewGroup) view, new int[] { Color.BLUE, Color.YELLOW , Color.RED })
                    .stream(3000);
        });

        binding.volverAlMenu.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_compraFinalizadaFragment_to_homeFragment);
            cantinaViewModel.eliminarCarrito(userId);
            //borrar cararito cuando vuelva
            //cantinaViewModel.carrito(userId).clear();
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

        List<ProductoEnCarrito> productoEnCarrito;

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderCompraFinalizadaBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            ProductoEnCarrito producto = productoEnCarrito.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(producto.precio);
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

    class ProductoViewHolder extends RecyclerView.ViewHolder {
        ViewholderCompraFinalizadaBinding binding;

        public ProductoViewHolder(@NonNull ViewholderCompraFinalizadaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
