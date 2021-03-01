package com.example.proyectocantina;

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

import com.example.proyectocantina.databinding.FragmentBottomComunidadBinding;
import com.example.proyectocantina.databinding.ViewholderComunidadBinding;

import java.util.List;

public class ComunidadFragment extends Fragment {

    private FragmentBottomComunidadBinding binding;
    private ComunidadViewModel comunidadViewModel;
    private NavController navController;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentBottomComunidadBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        comunidadViewModel = new ViewModelProvider(requireActivity()).get(ComunidadViewModel.class);
        navController = Navigation.findNavController(view);

        binding.irANuevoComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_ComunidadFragment_to_NuevoElementoFragment);
            }
        });

        ComunidadAdapter comunidadAdapter = new ComunidadAdapter();

        binding.recyclerView.setAdapter(comunidadAdapter);

        binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));


        comunidadViewModel.obtener().observe(getViewLifecycleOwner(), new Observer<List<Comunidad>>() {
            @Override
            public void onChanged(List<Comunidad> comunidadList) {
                comunidadAdapter.establecerLista(comunidadList);
                binding.recyclerView.scrollToPosition(comunidadList.size() - 1);
            }
        });
    }
    class ComunidadAdapter extends RecyclerView.Adapter<ComunidadViewHolder> {

        List<Comunidad> comunidadList;

        @NonNull
        @Override
        public ComunidadViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ComunidadViewHolder(ViewholderComunidadBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ComunidadViewHolder holder, int position) {
            Comunidad comunidad = comunidadList.get(position);

            holder.binding.nombre.setText(comunidad.nombre);
            holder.binding.valoracion.setRating(comunidad.valoracion);
            holder.binding.cabecera.setText(comunidad.cabecera);
            holder.binding.comentario.setText(comunidad.comentario);

            /*
            holder.binding.valoracion.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                @Override
                public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                    if(fromUser) {
                        comunidadViewModel.actualizar(comunidad, rating);
                    }
                }
            });
             */
        }

        @Override
        public int getItemCount() {
            return comunidadList != null ? comunidadList.size() : 0;
        }

        public void establecerLista(List<Comunidad> comunidadList){
            this.comunidadList = comunidadList;
            notifyDataSetChanged();
        }

        /*public Mundial obtenerElemento(int posicion){
            return elementos.get(posicion);
        }
         */
    }

    static class ComunidadViewHolder extends RecyclerView.ViewHolder {
        private final ViewholderComunidadBinding binding;

        public ComunidadViewHolder(ViewholderComunidadBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
