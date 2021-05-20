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

import com.example.cantina.databinding.FragmentCerrarSesionBinding;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class CerrarSesionFragment extends Fragment {

    private NavController navController;
    FirebaseUser user;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cerrar_sesion, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        user = FirebaseAuth.getInstance().getCurrentUser();
        navController = Navigation.findNavController(view);

        GoogleSignIn.getClient(requireActivity(), new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .build()).signOut();

        FirebaseAuth.getInstance().signOut();
        System.out.println(user.getDisplayName());

        Navigation.findNavController(view).navigate(R.id.iniciarSesionFragment);
    }
}
