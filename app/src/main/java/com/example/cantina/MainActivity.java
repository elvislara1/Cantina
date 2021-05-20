package com.example.cantina;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.example.cantina.databinding.ActivityMainBinding;
import com.example.cantina.databinding.DrawerHeaderBinding;
import com.example.cantina.model.Usuario;
import com.example.cantina.viewmodel.AutenticacionViewModel;
import com.example.cantina.viewmodel.CantinaViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.luseen.spacenavigation.SpaceItem;
import com.luseen.spacenavigation.SpaceNavigationView;
import com.luseen.spacenavigation.SpaceOnClickListener;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;
    DrawerHeaderBinding drawerHeaderBinding;
    private CantinaViewModel cantinaViewModel;

    MenuItem SearchFragmentItem;
    MenuItem FilterFragmentItem;
    AutenticacionViewModel autenticacionViewModel;
    ProductosFragment productosFragment;
    //FirebaseRecyclerAdapter<Producto, ProductosFragment.ProductosViewHolder> adapter;
    DatabaseReference mProductRef;
    private SpaceNavigationView spaceNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());

        signInClient.launch(GoogleSignIn.getClient(this, new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).build()).getSignInIntent());

        drawerHeaderBinding = DrawerHeaderBinding.bind(binding.navView.getHeaderView(0));
        autenticacionViewModel = new ViewModelProvider(this).get(AutenticacionViewModel.class);
        cantinaViewModel = new ViewModelProvider(this).get(CantinaViewModel.class);


        //TODO - NAV_BOTTOM_BAR
        spaceNavigationView = (SpaceNavigationView) findViewById(R.id.bottom_nav_view);
        spaceNavigationView.initWithSaveInstanceState(savedInstanceState);
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_home_24));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_baseline_shopping_cart_24));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_favorite2));
        spaceNavigationView.addSpaceItem(new SpaceItem("", R.drawable.ic_comunidad));

        spaceNavigationView.shouldShowFullBadgeText(true);
        spaceNavigationView.setCentreButtonIconColorFilterEnabled(false);

        setSupportActionBar(binding.toolbar);

        spaceNavigationView.setSpaceOnClickListener(new SpaceOnClickListener() {
            @Override
            public void onCentreButtonClick() {
                Log.d("onCentreButtonClick ", "onCentreButtonClick");
                spaceNavigationView.shouldShowFullBadgeText(true);
                navController.navigate(R.id.compraRapidaFragment);
            }

            @Override
            public void onItemClick(int itemIndex, String itemName) {
                switch (itemIndex) {
                    case 0:
                        navController.navigate(R.id.homeFragment);
                        return;
                    case 1:
                        navController.navigate(R.id.carritoFragment);
                        return;
                    case 2:
                        navController.navigate(R.id.favoritoFragment);
                        return;
                    case 3:
                        navController.navigate(R.id.comunidadFragment);
                        return;
                    default:
                        navController.navigate(R.id.homeFragment);
                }
                Log.d("onItemClick ", "" + itemIndex + " " + itemName);
            }

            @Override
            public void onItemReselected(int itemIndex, String itemName) {
                Log.d("onItemReselected ", "" + itemIndex + " " + itemName);
                navController.navigate(R.id.homeFragment);
            }
        });
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                // Top-level destinations:
                R.id.homeFragment
        )
                .setOpenableLayout(binding.drawerLayout)
                .build();

        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        NavigationView navigationView = findViewById(R.id.nav_view);

        /* Load user info in drawer header*/
        View header = navigationView.getHeaderView(0);
        final ImageView photo = header.findViewById(R.id.photoImageView);
        final TextView name = header.findViewById(R.id.displayNameTextView);
        final TextView email = header.findViewById(R.id.emailTextView);

        FirebaseAuth.getInstance().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();

                if(user != null){
                    Glide.with(MainActivity.this)
                            .load(FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString())
                            .circleCrop()
                            .into(photo);
                    name.setText(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
                    email.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                }
            }
        });

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
                        || destination.getId() == R.id.alfabetFragment
                        || destination.getId() == R.id.productoDelDiaFragment
                        || destination.getId() == R.id.adminFragment
                        || destination.getId() == R.id.nuevoProductoFragment
                        || destination.getId() == R.id.metodoPagoFragment
                        || destination.getId() == R.id.checkoutFragment
                        || destination.getId() == R.id.splashFragment
                        || destination.getId() == R.id.tarjetaCreditoFragment
                        || destination.getId() == R.id.compraRapidaFragment
                        || destination.getId() == R.id.compraFinalizadaFragment){
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
                        || destination.getId() == R.id.searchFragment
                        || destination.getId() == R.id.compraRapidaFragment
                        || destination.getId() == R.id.nuevoComentarioFragment
                        || destination.getId() == R.id.adminFragment
                        || destination.getId() == R.id.nuevoProductoFragment
                        || destination.getId() == R.id.metodoPagoFragment
                        || destination.getId() == R.id.checkoutFragment
                        || destination.getId() == R.id.splashFragment
                        || destination.getId() == R.id.tarjetaCreditoFragment
                        || destination.getId() == R.id.productoDelDiaFragment
                        || destination.getId() == R.id.compraFinalizadaFragment) {
                    if (SearchFragmentItem != null) SearchFragmentItem.setVisible(false);
                    if (FilterFragmentItem != null) FilterFragmentItem.setVisible(false);
                } else {
                    if (SearchFragmentItem != null) SearchFragmentItem.setVisible(true);
                    if (FilterFragmentItem != null) FilterFragmentItem.setVisible(true);
                }

                if(destination.getId() == R.id.iniciarSesionFragment
                        || destination.getId() == R.id.registroFragment){
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
                } else{
                    binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
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
                    drawerHeaderBinding.displayNameTextView.setText(usuario.username);
                }
            }
        });
    }

    //TODO Buscar producto
    private void firebaseSearch(String searchText) {
        cantinaViewModel.establecerTerminoBusqueda(searchText);
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