package com.viajet.itubus.activity.activity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.fragment.Favorito_Fragment;

public class FavoritosActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favoritos); // Layout da atividade

        // Carregando o fragmento Favorito_Fragment
        getSupportFragmentManager()
            .beginTransaction()
            .replace(R.id.fragmentContainer, new Favorito_Fragment()) // Substitui o fragmento
            .addToBackStack(null) // Adiciona à pilha de retrocesso
            .commit();
    }
}
