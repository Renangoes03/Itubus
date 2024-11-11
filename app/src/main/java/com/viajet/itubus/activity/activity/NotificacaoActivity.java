package com.viajet.itubus.activity.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.viajet.itubus.R;

import java.util.Calendar;

public class NotificacaoActivity extends AppCompatActivity {

    private TextView dateTextView;  // TextView para exibir as datas dos dias da semana

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notificacao);

        // Configurar a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        if (toolbar != null) {
            toolbar.setTitle("Notificações");
            setSupportActionBar(toolbar);
            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_esquerda);
            }
        } else {
            Toast.makeText(this, "Toolbar não encontrada", Toast.LENGTH_SHORT).show();
        }

    }

    // Configurar a ação de voltar na Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Fecha a atividade e retorna para a anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
