package com.example.cantina;

import android.content.Intent;
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
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.cantina.databinding.FragmentIniciarSesionBinding;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class IniciarSesionFragment extends Fragment {

    private FragmentIniciarSesionBinding binding;
    private AutenticacionViewModel autenticacionViewModel;
    private NavController navController;
    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return (binding = FragmentIniciarSesionBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        autenticacionViewModel = new ViewModelProvider(requireActivity()).get(AutenticacionViewModel.class);
        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();

        binding.irAlRegistro.setOnClickListener(v -> navController.navigate(R.id.action_iniciarSesionFragment_to_registroFragment));

        binding.iniciarSesion.setOnClickListener(v -> {
            String email = binding.email.getText().toString();
            String password = binding.password.getText().toString();

            boolean valid = true;

            if (email.isEmpty()) {
                binding.email.setError("Required");
                valid = false;
            }
            if (password.isEmpty()) {
                binding.password.setError("Required");
                valid = false;
            }



            if (valid) {
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                navController.navigate(R.id.action_iniciarSesionFragment_to_inicioFragment);
                            } else {
                                Toast.makeText(requireContext(), task.getException().getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        binding.googleSignIn.setOnClickListener(v -> {
            signInClient.launch(GoogleSignIn.getClient(requireContext(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).build()).getSignInIntent());
        });
    }
        ActivityResultLauncher<Intent> signInClient = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            try {
                FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult(ApiException.class).getIdToken(), null));
                navController.navigate(R.id.action_iniciarSesionFragment_to_inicioFragment);
            } catch (ApiException e) {}
    });
}
