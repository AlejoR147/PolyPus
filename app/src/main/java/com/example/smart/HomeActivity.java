package com.example.smart;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsControllerCompat;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;

import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private BottomNavigationView bottomNavigationView;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;
    private TextView emailTextView;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        NavigationView navigationView = findViewById(R.id.nav_view);

        // Obtén la vista del header (el primer header, índice 0)
        View headerView = navigationView.getHeaderView(0);

        // Busca el TextView dentro del header
        TextView emailTextView = headerView.findViewById(R.id.emailTextView);

        // Obtén el email del intent o usa "Invitado" por defecto
        String email = getIntent().getStringExtra("email");
        if (email == null) {
            email = "Invitado";
        }

        // Asigna el email al TextView del header
        emailTextView.setText(email);
        // Inicializar Firebase
        mAuth = FirebaseAuth.getInstance();

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
                            .replace(R.id.fragment_layout, new ShortsFragment())
                            .commit();
                    return true;

                } else if (id == R.id.library) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new libraryFragment())
                            .commit();
                    return true;

                } else if (id == R.id.subscriptions) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_layout, new SubscripcionsFragment())
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

                    // Redirigir a LoginActivity
                    Intent intent = new Intent(HomeActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
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