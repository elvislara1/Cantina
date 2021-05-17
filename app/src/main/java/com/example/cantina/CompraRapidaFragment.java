package com.example.cantina;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentCompraRapidaBinding;
import com.example.cantina.databinding.ViewholderCompraRapidaBinding;
import com.example.cantina.model.Producto;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class CompraRapidaFragment extends Fragment {

    private FirebaseFirestore mDb;
    private FragmentCompraRapidaBinding binding;
    private CantinaViewModel cantinaViewModel;
    private NavController navController;
    private FirebaseUser user;

    private CompraRapidaAdapter compraRapidaAdapter;
    private List<Producto> productoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_compra_rapida, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(view);

        compraRapidaAdapter = new CompraRapidaAdapter();

        binding.recyclerViewMasVendido.setAdapter(compraRapidaAdapter);
        binding.recyclerViewMasVendido.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        mDb.collection("compraRapida")
                .addSnapshotListener((value, error) -> {
                    for (QueryDocumentSnapshot m: value){
                        productoList.add(new Producto(m));
                    }
                    compraRapidaAdapter.notifyDataSetChanged();
                    binding.recyclerViewMasVendido.scrollToPosition(productoList.size() - 1);
                });
    }

    class CompraRapidaAdapter extends RecyclerView.Adapter<CompraRapidaViewHolder> {

        private String numberFormatter;

        @NonNull
        @Override
        public CompraRapidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CompraRapidaViewHolder(ViewholderCompraRapidaBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull CompraRapidaViewHolder holder, int position) {
            Producto producto = productoList.get(position);

            holder.binding.nombre.setText(producto.nombre);
            String finalresult = String.valueOf(producto.preciod);
            String formattedValue = String.format(finalresult + "€");
            holder.binding.precio.setText(formattedValue);

            Glide.with(CompraRapidaFragment.this)
                    .load(producto.img)
                    .into(holder.binding.foto);
        }

        @Override
        public int getItemCount() {
            return productoList != null ? productoList.size() : 0;
        }
    }

    static class CompraRapidaViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderCompraRapidaBinding binding;

        public CompraRapidaViewHolder(ViewholderCompraRapidaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}