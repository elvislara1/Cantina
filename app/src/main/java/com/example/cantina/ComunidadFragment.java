package com.example.cantina;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cantina.databinding.FragmentComunidadBinding;
import com.example.cantina.databinding.ViewholderComunidadBinding;
import com.example.cantina.model.Comentario;
import com.example.cantina.viewmodel.CantinaViewModel;


import java.util.List;

public class ComunidadFragment extends Fragment {

    private FragmentComunidadBinding binding;
    private CantinaViewModel cantinaViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentComunidadBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        navController = Navigation.findNavController(view);

        binding.irANuevoComentario.setOnClickListener(v -> navController.navigate(R.id.action_nuevoComentarioFragment));

        ComunidadAdapter comunidadAdapter = new ComunidadAdapter();
        binding.recyclerView.setAdapter(comunidadAdapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        cantinaViewModel.obtenerComunidad().observe(getViewLifecycleOwner(), new Observer<List<Comentario>>() {
            @Override
            public void onChanged(List<Comentario> comentarioList) {
                comunidadAdapter.establecerLista(comentarioList);
                binding.recyclerView.scrollToPosition(comentarioList.size() - 1);
            }
        });

    }
    class ComunidadAdapter extends RecyclerView.Adapter<ComunidadViewHolder> {

        List<Comentario> comentarioList;

        @NonNull
        @Override
        public ComunidadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ComunidadViewHolder(ViewholderComunidadBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ComunidadViewHolder holder, int position) {
            Comentario comentario = comentarioList.get(position);

            holder.binding.nombre.setText(comentario.usuario);
            holder.binding.valoracion.setRating(comentario.valoracion);
            holder.binding.cabecera.setText(comentario.cabecera);
            holder.binding.comentario.setText(comentario.comentario);
        }

        @Override
        public int getItemCount() {
            return comentarioList != null ? comentarioList.size() : 0;
        }

        public void establecerLista(List<Comentario> comentarioList){
            this.comentarioList = comentarioList;
            notifyDataSetChanged();
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