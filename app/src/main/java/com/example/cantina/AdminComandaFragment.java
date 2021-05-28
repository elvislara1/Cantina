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

import com.example.cantina.databinding.FragmentAdminComandaBinding;
import com.example.cantina.databinding.ViewholderAdminComandaBinding;
import com.example.cantina.model.CompraEnEspera;
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

    private List<CompraEnEspera> compraEnEspera = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentAdminComandaBinding.inflate(inflater, container, false)).getRoot();
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mDb = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        cantinaViewModel = new ViewModelProvider(requireActivity()).get(CantinaViewModel.class);
        navController = Navigation.findNavController(view);

        final AdminComandaAdapter adminComandaAdapter = new AdminComandaAdapter();

        binding.recyclerView.setAdapter(adminComandaAdapter);
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));

        mDb.collection("enEspera")
                .document()
                .collection("compraPendiente")
                .addSnapshotListener((value, error) -> {
                    for (QueryDocumentSnapshot m : value) {
                        compraEnEspera.add(new CompraEnEspera(m));
                    }
                    adminComandaAdapter.notifyDataSetChanged();
                    binding.recyclerView.scrollToPosition(compraEnEspera.size() + 2);
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
            CompraEnEspera comanda = compraEnEspera.get(position);

            holder.binding.nombre.setText(comanda.nombre);
            holder.binding.precio.setText(String.format("%.2f €", comanda.precio));
            holder.binding.cantidad.setText("x" + comanda.cantidad);
        }

        @Override
        public int getItemCount() {
            return compraEnEspera != null ? compraEnEspera.size() : 0;
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