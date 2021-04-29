package com.example.cantina;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.FragmentRegistroBinding;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;

import java.util.UUID;

public class RegistroFragment extends Fragment {

    public static class SignUpViewModel extends ViewModel {
        Uri fotoUri;
    }

    private FragmentRegistroBinding binding;
    private FirebaseAuth mAuth;
    private AutenticacionViewModel autenticacionViewModel;
    private NavController navController;
    private SignUpViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentRegistroBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = new ViewModelProvider(this).get(SignUpViewModel.class);

        autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);
        autenticacionViewModel.iniciarRegistro();

        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();

        binding.registrar.setOnClickListener(v -> {
            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();
            String name = binding.name.getText().toString();

            boolean valid = true;

            if (email.isEmpty()) {
                binding.email.setError("Required");
                valid = false;
            }
            if (password.isEmpty()) {
                binding.password.setError("Required");
                valid = false;
            }
            if (name.isEmpty()) {
                binding.name.setError("Required");
                valid = false;
            }
            if (viewModel.fotoUri == null) {
                Toast.makeText(requireContext(), "Seleccione una foto", Toast.LENGTH_SHORT).show();
                valid = false;
            }

            if (valid) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                FirebaseStorage.getInstance()
                                        .getReference("avatars/" + UUID.randomUUID())
                                        .putFile(viewModel.fotoUri)
                                        .continueWithTask(task2 -> task2.getResult().getStorage().getDownloadUrl())
                                        .addOnSuccessListener(url -> {
                                            mAuth.getCurrentUser().updateProfile(
                                                    new UserProfileChangeRequest.Builder()
                                                            .setDisplayName(name)
                                                            .setPhotoUri(url)
                                                            .build()
                                            );
                                            navController.navigate(R.id.action_registroFragment_to_inicioFragment);
                                        });
                            } else {
                                Toast.makeText(requireContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }

            autenticacionViewModel.crearCuentaEIniciarSesion(name, password, email);
        });

        binding.foto.setOnClickListener(v -> {
            galeria.launch("image/*");
        });

        if(viewModel.fotoUri != null) Glide.with(this).load(viewModel.fotoUri).circleCrop().into(binding.foto);
    }

    private final ActivityResultLauncher<String> galeria = registerForActivityResult(new ActivityResultContracts.GetContent(), uri -> {
        viewModel.fotoUri = uri;
        Glide.with(this).load(uri).circleCrop().into(binding.foto);
    });
}