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

import com.example.cantina.databinding.FragmentCompraPendienteBinding;
import com.example.cantina.databinding.ViewholderCompraPendienteBinding;
import com.example.cantina.model.ProductoEnCarrito;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class CompraPendienteFragment extends Fragment {
    FragmentCompraPendienteBinding binding;
    private NavController navController;
    private FirebaseFirestore mDb;
    private FirebaseUser user;

    private List<ProductoEnCarrito> productoEnCarrito = new ArrayList<>();

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
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(view);

        final CarritoAdapter carritoAdapter = new CarritoAdapter();

        binding.recyclerView.setAdapter(carritoAdapter);

        mDb.collection("carrito").document(user.getUid()).collection("productoEnCarrito").addSnapshotListener((value, error) -> {
            for (QueryDocumentSnapshot m: value){
                productoEnCarrito.add(new ProductoEnCarrito(m));
            }
            carritoAdapter.notifyDataSetChanged();
            binding.recyclerView.scrollToPosition(productoEnCarrito.size() - 1);
        });

        binding.volverAlMenu.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_compraPendienteFragment_to_homeFragment);

            productoEnCarrito.forEach(producto -> {
                mDb.collection("enEspera").document().collection("compraPendiente").add(producto).addOnSuccessListener(documentReference -> {
                    mDb.collection("carrito").document(user.getUid()).collection("productoEnCarrito").document().delete();
                });
            });
            //cantinaViewModel.eliminarCarrito(userId);
            //borrar cararito cuando vuelva
            //cantinaViewModel.carrito(userId).clear();
        });
    }

    class CarritoAdapter extends  RecyclerView.Adapter<ProductoViewHolder>{

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderCompraPendienteBinding.inflate(getLayoutInflater(), parent, false));
        }


        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull ProductoViewHolder holder, int position) {
            ProductoEnCarrito producto = productoEnCarrito.get(position);

            holder.binding.nombre.setText(producto.nombre + "\n");
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
        ViewholderCompraPendienteBinding binding;

        public ProductoViewHolder(@NonNull ViewholderCompraPendienteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
