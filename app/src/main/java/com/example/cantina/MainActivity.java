package com.example.cantina;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.cantina.databinding.ActivityMainBinding;
import com.example.cantina.databinding.DrawerHeaderBinding;
import com.example.cantina.model.Usuario;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;
    DrawerHeaderBinding drawerHeaderBinding;
    private CantinaViewModel cantinaViewModel;

    MenuItem SearchFragmentItem;
    MenuItem FilterFragmentItem;

    AutenticacionViewModel autenticacionViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());

        signInClient.launch(GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).build()).getSignInIntent());

        drawerHeaderBinding = DrawerHeaderBinding.bind(binding.navView.getHeaderView(0));
        autenticacionViewModel = new ViewModelProvider(this).get(AutenticacionViewModel.class);
        cantinaViewModel = new ViewModelProvider(this).get(CantinaViewModel.class);

        setSupportActionBar(binding.toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                // Top-level destinations:
                R.id.homeFragment
        )
                .setOpenableLayout(binding.drawerLayout)
                .build();

        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {

                Log.e("Navegando hacia: ", (String) destination.getLabel());

                if (destination.getId() == R.id.iniciarSesionFragment
                        || destination.getId() == R.id.registroFragment
                        || destination.getId() == R.id.splashFragment) {
                    binding.toolbar.setVisibility(View.GONE);
                    binding.navView.setVisibility(View.GONE);
                    binding.bottomNavView.setVisibility(View.GONE);
                } else {
                    binding.toolbar.setVisibility(View.VISIBLE);
                    binding.navView.setVisibility(View.VISIBLE);
                    binding.bottomNavView.setVisibility(View.GONE);
                }

                if (destination.getId() == R.id.perfilFragment
                        || destination.getId() == R.id.ayudaFragment
                        || destination.getId() == R.id.settingsFragment
                        || destination.getId() == R.id.filterFragment
                        || destination.getId() == R.id.iniciarSesionFragment
                        || destination.getId() == R.id.registroFragment
                        || destination.getId() == R.id.baratoFragment
                        || destination.getId() == R.id.caroFragment
                        || destination.getId() == R.id.alfabetFragment) {
                    binding.bottomNavView.setVisibility(View.GONE);
                } else {
                    binding.bottomNavView.setVisibility(View.VISIBLE);
                }

                if (destination.getId() == R.id.searchFragment){
                    binding.searchView.setVisibility(View.VISIBLE);
                    binding.searchView.setIconified(false);
                    binding.searchView.requestFocusFromTouch();
                } else {
                    binding.searchView.setVisibility(View.GONE);
                }
                if (destination.getId() == R.id.carritoFragment
                        || destination.getId() == R.id.favoritoFragment
                        || destination.getId() == R.id.comunidadFragment
                        || destination.getId() == R.id.perfilFragment
                        || destination.getId() == R.id.ayudaFragment
                        || destination.getId() == R.id.settingsFragment
                        || destination.getId() == R.id.filterFragment
                        || destination.getId() == R.id.baratoFragment
                        || destination.getId() == R.id.caroFragment
                        || destination.getId() == R.id.alfabetFragment
                        || destination.getId() == R.id.searchFragment) {
                    if (SearchFragmentItem != null) SearchFragmentItem.setVisible(false);
                    if (FilterFragmentItem != null) FilterFragmentItem.setVisible(false);
                } else {
                    if (SearchFragmentItem != null) SearchFragmentItem.setVisible(true);
                    if (FilterFragmentItem != null) FilterFragmentItem.setVisible(true);
                }
            }
        });

        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }

            @Override
            public boolean onQueryTextChange(String newText) {
                cantinaViewModel.establecerTerminoBusqueda(newText);
                return false;
            }
        });

        autenticacionViewModel.usuarioAutenticado.observe(this, new Observer<Usuario>() {
            @Override
            public void onChanged(Usuario usuario) {
                if (usuario != null){
                    drawerHeaderBinding.username.setText(usuario.username);
                }
            }
        });
    }

    ActivityResultLauncher<Intent> signInClient = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
        try {
            FirebaseAuth.getInstance().signInWithCredential(GoogleAuthProvider.getCredential(GoogleSignIn.getSignedInAccountFromIntent(result.getData()).getResult(ApiException.class).getIdToken(), null));
        } catch (ApiException e) {}
    });

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        SearchFragmentItem = menu.findItem(R.id.searchFragment);
        FilterFragmentItem = menu.findItem(R.id.filterFragment);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);
    }
}