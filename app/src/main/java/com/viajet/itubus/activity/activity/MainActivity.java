package com.viajet.itubus.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.fragment.Configuracao_Fragment;
import com.viajet.itubus.activity.fragment.Favorito_Fragment;
import com.viajet.itubus.activity.fragment.Home_fragment;
import com.viajet.itubus.activity.fragment.Localizacao_Fragment;
import com.viajet.itubus.activity.fragment.Perfil_Fragment;
import com.viajet.itubus.activity.helper.ConfiguracaoFirebase;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Itubus");
        setSupportActionBar(toolbar);

        // Configurações de objetos
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        // Configura BottomNavigationView
        configuraBottomNavigationView();

        // Configura fragment inicial
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.viewPager, new Home_fragment()).commit();

        // Aplica os insets de sistema para ajustar o layout
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Método responsável por criar a BottomNavigationView
     */
    private void configuraBottomNavigationView() {
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Faz configurações iniciais do Bottom Navigation
        // bottomNavigationView.enableAnimation(true);
        // bottomNavigationView.enableItemShiftingMode(false);
        // bottomNavigationView.enableShiftingMode(false);
        // bottomNavigationView.setTextVisibility(false);

        // Habilitar navegação
        habilitarNavegacao(bottomNavigationView);

        // Configura item selecionado inicialmente
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
    }

    /**
     * Método responsável por tratar eventos de click na BottomNavigation
     *
     * @param view
     */
    private void habilitarNavegacao(BottomNavigationView view) {
        view.setOnNavigationItemSelectedListener(item -> {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            int itemId = item.getItemId();
            if (itemId == R.id.navigation_favorite) {
                fragmentTransaction.replace(R.id.viewPager, new Favorito_Fragment()).commit();
                return true;
            } else if (itemId == R.id.navigation_location) {
                fragmentTransaction.replace(R.id.viewPager, new Localizacao_Fragment()).commit();
                return true;
            } else if (itemId == R.id.navigation_home) {
                fragmentTransaction.replace(R.id.viewPager, new Home_fragment()).commit();
                return true;
            } else if (itemId == R.id.navigation_profile) {
                fragmentTransaction.replace(R.id.viewPager, new Perfil_Fragment()).commit();
                return true;
            } else if (itemId == R.id.navigation_settings) {
                fragmentTransaction.replace(R.id.viewPager, new Configuracao_Fragment()).commit();
                return true;
            } else {
                return false;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_sair) {
            deslogarUsuario();
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void deslogarUsuario() {
        try {
            autenticacao.signOut();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
