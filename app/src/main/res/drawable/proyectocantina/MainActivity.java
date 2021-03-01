package com.example.proyectocantina;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.proyectocantina.databinding.ActivityMainBinding;
import com.example.proyectocantina.databinding.FragmentMostrarProductoBinding;

import me.toptas.fancyshowcase.FancyShowCaseQueue;
import me.toptas.fancyshowcase.FancyShowCaseView;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    NavController navController;
    MenuItem SearchFragmentItem;
    MenuItem FilterFragmentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView((binding = ActivityMainBinding.inflate(getLayoutInflater())).getRoot());

        setSupportActionBar(binding.toolbar);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                // Top-level destinations:
               R.id.TabbedFragment
        )
                .setOpenableLayout(binding.drawerLayout)
                .build();

        navController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment)).getNavController();
        NavigationUI.setupWithNavController(binding.navView, navController);
        NavigationUI.setupWithNavController(binding.bottomNavView, navController);
        NavigationUI.setupWithNavController(binding.toolbar, navController, appBarConfiguration);

        navController.addOnDestinationChangedListener((controller, destination, arguments) -> {

            if (destination.getId() == R.id.PerfilFragment
                    || destination.getId() == R.id.AyudaFragment
                    || destination.getId() == R.id.SettingsFragment
                    || destination.getId() == R.id.FilterFragment
                    || destination.getId() == R.id.MostrarProductoFragment
                    || destination.getId() == R.id.BaratoFragment
                    || destination.getId() == R.id.CaroFragment
                    || destination.getId() == R.id.AlfabetFragment
                    || destination.getId() == R.id.IniciarSesionFragment
                    || destination.getId() == R.id.RegistroFragment
                    || destination.getId() == R.id.CheckoutFragment
                    || destination.getId() == R.id.MetodoPagoFragment
                    || destination.getId() == R.id.CompraFinalizadaFragment
                    || destination.getId() == R.id.NuevoComentarioFragment) {
                binding.bottomNavView.setVisibility(View.GONE);
            } else {
                binding.bottomNavView.setVisibility(View.VISIBLE);
            }
            if (destination.getId() == R.id.IniciarSesionFragment
                    || destination.getId() == R.id.RegistroFragment){
                binding.toolbar.setVisibility(View.GONE);
            } else {
                binding.toolbar.setVisibility(View.VISIBLE);
            }

            if (destination.getId() == R.id.CarritoFragment
                    || destination.getId() == R.id.FavoritoFragment
                    || destination.getId() == R.id.CompraRapidaFragment
                    || destination.getId() == R.id.ComunidadFragment
                    || destination.getId() == R.id.PerfilFragment
                    || destination.getId() == R.id.AyudaFragment
                    || destination.getId() == R.id.SettingsFragment
                    || destination.getId() == R.id.FilterFragment
                    || destination.getId() == R.id.MostrarProductoFragment
                    || destination.getId() == R.id.BaratoFragment
                    || destination.getId() == R.id.CaroFragment
                    || destination.getId() == R.id.AlfabetFragment
                    || destination.getId() == R.id.CheckoutFragment
                    || destination.getId() == R.id.MetodoPagoFragment
                    || destination.getId() == R.id.CompraFinalizadaFragment
                    || destination.getId() == R.id.NuevoComentarioFragment) {
                if (SearchFragmentItem != null) SearchFragmentItem.setVisible(false);
                if (FilterFragmentItem != null) FilterFragmentItem.setVisible(false);
            } else {
                if (SearchFragmentItem != null) SearchFragmentItem.setVisible(true);
                if (FilterFragmentItem != null) FilterFragmentItem.setVisible(true);
            }

            //TODO
            //Cuando haga click en el search NO se muestre la hamburgesa
            if (destination.getId() == R.id.SearchFragment) {
                binding.navView.setVisibility(View.GONE);
            } else {
                binding.navView.setVisibility(View.VISIBLE);
            }
        });
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        SearchFragmentItem = menu.findItem(R.id.SearchFragment);
        FilterFragmentItem = menu.findItem(R.id.FilterFragment);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        return NavigationUI.onNavDestinationSelected(item, navController)
                || super.onOptionsItemSelected(item);

    }
}