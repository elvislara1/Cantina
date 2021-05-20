package com.example.cantina;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cantina.databinding.FragmentCompraFinalizadaBinding;
import com.example.cantina.databinding.FragmentCompraPendienteBinding;
import com.example.cantina.databinding.FragmentComunidadBinding;
import com.example.cantina.databinding.ViewholderCompraFinalizadaBinding;
import com.example.cantina.databinding.ViewholderCompraPendienteBinding;
import com.example.cantina.model.ProductoEnCarrito;
import com.example.cantina.model.Usuario;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;


public class CompraPendienteFragment extends Fragment {
    FragmentCompraPendienteBinding binding;
    private CantinaViewModel cantinaViewModel;
    private NavController navController;
    public AutenticacionViewModel autenticacionViewModel;
    private Usuario usuario;
    private int userId;
    private FirebaseFirestore mDb;
    private FirebaseUser user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentCompraPendienteBinding.inflate(inflater, container, false)).getRoot();
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

        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);
        navController = Navigation.findNavController(view);

        usuario = autenticacionViewModel.usuarioAutenticado.getValue();
        userId = usuario.id;

        final CompraPendienteFragment.CarritoAdapter carritoAdapter = new CompraPendienteFragment.CarritoAdapter();

        binding.recyclerView.setAdapter(carritoAdapter);
        cantinaViewModel.carrito(userId).observe(getViewLifecycleOwner(), producto -> carritoAdapter.establecerLista(producto));


        binding.volverAlMenu.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_compraFinalizadaFragment_to_homeFragment);
            cantinaViewModel.eliminarCarrito(userId);
            //borrar cararito cuando vuelva
            //cantinaViewModel.carrito(userId).clear();
        });
    }

    class CarritoAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        List<ProductoEnCarrito> productoEnCarrito;

        @NonNull
        @Override
        public CompraPendienteFragment.ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CompraPendienteFragment.ProductoViewHolder(ViewholderCompraPendienteBinding.inflate(getLayoutInflater(), parent, false));
        }



        @Override
        public void onBindViewHolder(@NonNull CompraPendienteFragment.ProductoViewHolder holder, int position) {
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
        ViewholderCompraPendienteBinding binding;
        public ProductoViewHolder(@NonNull ViewholderCompraPendienteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
