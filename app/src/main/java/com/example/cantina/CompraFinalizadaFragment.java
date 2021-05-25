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
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cantina.databinding.FragmentCompraFinalizadaBinding;
import com.example.cantina.databinding.ViewholderCompraFinalizadaBinding;
import com.example.cantina.model.ProductoEnCarrito;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CompraFinalizadaFragment extends Fragment {

    FragmentCompraFinalizadaBinding binding;
    private NavController navController;
    private FirebaseUser user;
    private FirebaseFirestore mDb;

    private List<ProductoEnCarrito> productoEnCarrito = new ArrayList<>();

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
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(view);

        final CarritoAdapter carritoAdapter = new CarritoAdapter();
        
        binding.recyclerView.setAdapter(carritoAdapter);
        //cantinaViewModel.carrito(userId).observe(getViewLifecycleOwner(), producto -> carritoAdapter.establecerLista(producto));

        mDb.collection("carrito").document(user.getUid()).collection("productoEnCarrito").addSnapshotListener((value, error) -> {
            for (QueryDocumentSnapshot m: value){
                productoEnCarrito.add(new ProductoEnCarrito(m));
            }
            carritoAdapter.notifyDataSetChanged();
            binding.recyclerView.scrollToPosition(productoEnCarrito.size() - 1);
        });

        binding.volverAlMenu.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_compraFinalizadaFragment_to_homeFragment);

            //cantinaViewModel.eliminarCarrito(userId);
            //borrar cararito cuando vuelva
            //cantinaViewModel.carrito(userId).clear();
        });
    }

    class CarritoAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderCompraFinalizadaBinding.inflate(getLayoutInflater(), parent, false));
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            ProductoEnCarrito producto = productoEnCarrito.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(String.format("%.2f €", producto.precio));
            holder.binding.cantidad.setText("x" + producto.cantidad);
        }

        @Override
        public int getItemCount() {
            return productoEnCarrito == null ? 0 : productoEnCarrito.size();
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
