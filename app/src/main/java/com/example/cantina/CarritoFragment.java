package com.example.cantina;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentCarritoBinding;
import com.example.cantina.databinding.ViewholderCarritoBinding;
import com.example.cantina.model.ProductoEnCarrito;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CarritoFragment extends Fragment {

    FragmentCarritoBinding binding;
    private NavController navController;
    private FirebaseUser user;
    private FirebaseFirestore mDb = FirebaseFirestore.getInstance();
    private CollectionReference ref = mDb.collection("carrito");
    private CarritoAdapter carritoAdapter;
    private List<ProductoEnCarrito> productoEnCarrito = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentCarritoBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        navController = NavHostFragment.findNavController(requireParentFragment());

        carritoConProdutos();

        Query query = ref.document(user.getUid()).collection("productoEnCarrito");

        FirestoreRecyclerOptions<ProductoEnCarrito> options = new FirestoreRecyclerOptions.Builder<ProductoEnCarrito>()
                .setQuery(query, ProductoEnCarrito.class)
                .build();

        carritoAdapter = new CarritoAdapter(options);

        binding.volver.setOnClickListener(v -> {
            navController.popBackStack();
        });

        binding.checkout.setOnClickListener(v -> {
            navController.navigate(R.id.action_checkoutFragment);
        });

        binding.recyclerView.setAdapter(carritoAdapter);
        navController = Navigation.findNavController(view);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(
                ItemTouchHelper.UP | ItemTouchHelper.DOWN,
                ItemTouchHelper.RIGHT  | ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //int posicion = viewHolder.getAdapterPosition();
                carritoAdapter.deleteItem(viewHolder.getAdapterPosition());
                Toasty.info(getActivity(), "Producto eliminado", Toast.LENGTH_LONG).show();
                //ProductoEnCarrito producto = carritoAdapter.obtenerProducto(posicion);

            }
        }).attachToRecyclerView(binding.recyclerView);

        System.out.println(productoEnCarrito);
        mDb.collection("carrito").document(user.getUid()).collection("productoEnCarrito").addSnapshotListener((value, error) -> {
            for (QueryDocumentSnapshot m: value){
                productoEnCarrito.add(new ProductoEnCarrito(m));
            }
            carritoAdapter.notifyDataSetChanged();
            binding.recyclerView.scrollToPosition(productoEnCarrito.size() - 1);
        });

       //cantinaViewModel.carrito(userId).observe(getViewLifecycleOwner(), producto -> carritoAdapter.establecerLista(producto));
    }

    private void setUpRecyclerView() {
        Query query = ref.document(user.getUid()).collection("productoEnCarrito");

        FirestoreRecyclerOptions<ProductoEnCarrito> options = new FirestoreRecyclerOptions.Builder<ProductoEnCarrito>()
                .setQuery(query, ProductoEnCarrito.class)
                .build();
        carritoAdapter = new CarritoAdapter(options);
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

    public class CarritoAdapter extends FirestoreRecyclerAdapter<ProductoEnCarrito, ProductoViewHolder>{

        public CarritoAdapter(@NonNull FirestoreRecyclerOptions<ProductoEnCarrito> options) {
            super(options);
        }

        @NonNull
        @Override
        public ProductoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ProductoViewHolder(ViewholderCarritoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @SuppressLint("DefaultLocale")
        @Override
        protected void onBindViewHolder(@NonNull ProductoViewHolder holder, int position, @NonNull ProductoEnCarrito producto) {
            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(String.format("%.2f €", producto.precio));
            holder.binding.cantidad.setText("x" + producto.cantidad);
            if (productoEnCarrito.size() >= 1) carritoConProdutos();
            Glide.with(CarritoFragment.this).load(producto.idDrawable).into(holder.binding.imagen);
        }

        @Override
        public int getItemCount() {
            return productoEnCarrito == null ? 0 : productoEnCarrito.size();
        }

        public void deleteItem(int position) {
            getSnapshots().getSnapshot(position).getReference().delete();
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