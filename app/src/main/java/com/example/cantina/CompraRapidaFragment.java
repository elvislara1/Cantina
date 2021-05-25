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

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentCompraRapidaBinding;
import com.example.cantina.databinding.ViewholderCompraRapidaBinding;
import com.example.cantina.model.LoMasVendido;
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
    private String foto = "";
    private List<LoMasVendido> productoList = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return (binding = FragmentCompraRapidaBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(view);

        final CompraRapidaAdapter compraRapidaAdapter = new CompraRapidaAdapter();

        binding.recyclerViewMasVendido.setAdapter(compraRapidaAdapter);

        mDb.collection("loMasVendido").addSnapshotListener((value, error) -> {
                    for (QueryDocumentSnapshot m: value){
                        productoList.add(new LoMasVendido(m));
                    }
                    compraRapidaAdapter.notifyDataSetChanged();
                    binding.recyclerViewMasVendido.scrollToPosition(productoList.size());
        });
    }

    class CompraRapidaAdapter extends RecyclerView.Adapter<CompraRapidaViewHolder> {

        @NonNull
        @Override
        public CompraRapidaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new CompraRapidaViewHolder(ViewholderCompraRapidaBinding.inflate(getLayoutInflater(), parent, false));
        }

        @SuppressLint("DefaultLocale")
        @Override
        public void onBindViewHolder(@NonNull CompraRapidaViewHolder holder, int position) {
            LoMasVendido producto = productoList.get(position);

            holder.binding.nombre.setText(producto.nombre);
            holder.binding.precio.setText(String.format("%.2f €", producto.precio));
//            foto = holder.binding.foto.setBackgroundResource(R.drawable.button_decrement);

            Glide.with(CompraRapidaFragment.this)
                    .load(producto.idDrawable)
                    .placeholder(R.drawable.frame_image_view)
                    .into(holder.binding.foto);
        }


        @Override
        public int getItemCount() {
            return productoList != null ? productoList.size() : 0;
        }
    }

    static class CompraRapidaViewHolder extends RecyclerView.ViewHolder {
        ViewholderCompraRapidaBinding binding;

        public CompraRapidaViewHolder(@NonNull ViewholderCompraRapidaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}