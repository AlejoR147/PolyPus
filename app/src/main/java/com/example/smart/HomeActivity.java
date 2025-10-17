package com.example.smart;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.smart.ui.DatosFragment;
import com.example.smart.ui.HomeFragment;
import com.example.smart.ui.DatosFragment;
import com.example.smart.ui.AddTransactionFragment;
import com.example.smart.ui.comunidadFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);

        //Firebase
        mAuth = FirebaseAuth.getInstance();

        //Nav_view
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        TextView emailTextView = headerView.findViewById(R.id.emailTextView);
        Bundle bundle = getIntent().getExtras();
        String provider = getIntent().getStringExtra("provider");
        if (provider == null) {
            provider = "Basic";
        }
        String email = getIntent().getStringExtra("email");
        if (email == null) {
            email = "Invitado";
        }
        emailTextView.setText(email);
        //guardado de datos


        SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("email", email);
        editor.putString("provider", provider);
        editor.apply();

        // Inicializar vistas
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        toolbar = findViewById(R.id.toolbar);

        // Configurar toolbar con el drawer
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar,
                R.string.open_nav,
                R.string.close_nav
        );
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Listener para el BottomNavigationView (usamos if-else en vez de switch)
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new HomeFragment())
                            .commit();
                    return true;

                } else if (id == R.id.shorts) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new DatosFragment())
                            .commit();
                    return true;

                } else if (id == R.id.library) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new comunidadFragment())
                            .commit();
                    return true;

                } else if (id == R.id.subscriptions) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new AddTransactionFragment())
                            .commit();
                    return true;
                }

                return false;
            }
        });

        // Listener para el NavigationView (Drawer)
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_logout) {
                    // Cerrar sesión en Firebase
                    mAuth.signOut();
                    Toast.makeText(HomeActivity.this, "Sesión cerrada", Toast.LENGTH_SHORT).show();
                    //borarr datos
                    SharedPreferences prefs = getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.clear();
                    editor.apply();

                    // Redirigir a LoginActivity
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                } else if (id == R.id.nav_about) {
                // Abrir enlace en navegador
                String url = "https://github.com/AlejoR147/PolyPus";
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(browserIntent);
            }else if (id == R.id.nav_home) {
                    // Abrir enlace en navegador
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new HomeFragment())
                            .commit();
                    return true;
                }
                // Cerrar el drawer después de seleccionar
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }

        });

        // Fragmento por defecto al abrir la app
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_layout, new HomeFragment())
                    .commit();
            bottomNavigationView.setSelectedItemId(R.id.home);
        }
    }

    @Override
    public void onBackPressed() {
        // Si el drawer está abierto, lo cierra en lugar de salir
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}