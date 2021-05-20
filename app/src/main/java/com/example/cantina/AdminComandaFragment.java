package com.example.cantina;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentAdminBinding;
import com.example.cantina.databinding.FragmentAdminComandaBinding;
import com.example.cantina.databinding.ViewholderAdminComandaBinding;
import com.example.cantina.databinding.ViewholderComunidadBinding;
import com.example.cantina.model.Comentario;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;


public class AdminComandaFragment extends Fragment {

    private FirebaseFirestore mDb;
    private FragmentAdminComandaBinding binding;
    private CantinaViewModel cantinaViewModel;
    private NavController navController;
    private FirebaseUser user;

    private List<Comentario> comentarios = new ArrayList<>();

    private AdminComandaFragment.AdminComandaAdapter adminComandaAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_admin_comanda, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        navController = Navigation.findNavController(view);


        adminComandaAdapter = new AdminComandaFragment.AdminComandaAdapter();

        binding.comandas.setAdapter(adminComandaAdapter);
        binding.comandas.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));


        mDb.collection("comunidad")
                .orderBy("fecha")
                .addSnapshotListener((value, error) -> {
                    for (QueryDocumentSnapshot m: value){
                        comentarios.add(new Comentario(m));
                    }
                    adminComandaAdapter.notifyDataSetChanged();
                    binding.comandas.scrollToPosition(comentarios.size() - 1);
                });
    }

    class AdminComandaAdapter extends RecyclerView.Adapter<AdminComandaFragment.AdminComandaViewHolder> {

        @NonNull
        @Override
        public AdminComandaFragment.AdminComandaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new AdminComandaViewHolder(ViewholderAdminComandaBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull AdminComandaFragment.AdminComandaViewHolder holder, int position) {
            Comentario comentario = comentarios.get(position);

            holder.binding.nombre.setText(comentario.usuario);

            Glide.with(AdminComandaFragment.this)
                    .load(comentario.autorFoto);
        }

        @Override
        public int getItemCount() {
            return comentarios != null ? comentarios.size() : 0;
        }
    }

    static class AdminComandaViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderAdminComandaBinding binding;

        public AdminComandaViewHolder(ViewholderAdminComandaBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}