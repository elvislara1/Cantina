package com.example.cantina;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentComunidadBinding;
import com.example.cantina.databinding.ViewholderComunidadBinding;
import com.example.cantina.model.Comentario;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class ComunidadFragment extends Fragment {

    private FirebaseFirestore mDb;
    private FragmentComunidadBinding binding;
    private CantinaViewModel cantinaViewModel;
    private NavController navController;
    private FirebaseUser user;

    private List<Comentario> comentarios = new ArrayList<>();

    private ComunidadAdapter comunidadAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentComunidadBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        navController = Navigation.findNavController(view);

        binding.irANuevoComentario.setOnClickListener(v -> navController.navigate(R.id.action_nuevoComentarioFragment));

        comunidadAdapter = new ComunidadAdapter();

        binding.recyclerView.setAdapter(comunidadAdapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));


        mDb.collection("comunidad")
                .orderBy("fecha")
                .addSnapshotListener((value, error) -> {
                    for (QueryDocumentSnapshot m: value){
                        comentarios.add(new Comentario(m));
                    }
                    comunidadAdapter.notifyDataSetChanged();
                    binding.recyclerView.scrollToPosition(comentarios.size() - 1);
                });
    }

    class ComunidadAdapter extends RecyclerView.Adapter<ComunidadViewHolder> {

        @NonNull
        @Override
        public ComunidadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ComunidadViewHolder(ViewholderComunidadBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ComunidadViewHolder holder, int position) {
            Comentario comentario = comentarios.get(position);

            holder.binding.nombre.setText(comentario.usuario);

            holder.binding.valoracion.setRating(Float.parseFloat(comentario.valoracion));
            holder.binding.cabecera.setText(comentario.cabecera);
            holder.binding.comentario.setText(comentario.comentario);
            holder.binding.fecha.setText(comentario.fecha);

            Glide.with(ComunidadFragment.this)
                    .load(comentario.autorFoto)
                    .into(holder.binding.foto);
        }

        @Override
        public int getItemCount() {
            return comentarios != null ? comentarios.size() : 0;
        }
    }

    static class ComunidadViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderComunidadBinding binding;

        public ComunidadViewHolder(ViewholderComunidadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}