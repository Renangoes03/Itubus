package com.viajet.itubus.activity.activity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.viajet.itubus.R;

public class AcessibilidadeActivity extends AppCompatActivity {

    private TextView messageText1;
    private TextView messageText2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_acessibilidade);

        // Referência às TextViews
        messageText1 = findViewById(R.id.messageText1);
        messageText2 = findViewById(R.id.messageText2);

        // Configuração da StatusBar e Toolbar
        configurarStatusBar();
        configurarToolbar();

        // Inicializa a segunda mensagem como invisível
        messageText2.setVisibility(TextView.INVISIBLE);

        // Configuração do padding para suportar o design EdgeToEdge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Usando Handler para controlar o tempo
        new Handler().postDelayed(() -> {
            // Primeiro esconde a primeira mensagem
            messageText1.setVisibility(TextView.INVISIBLE);
            // Depois exibe a segunda mensagem
            messageText2.setVisibility(TextView.VISIBLE);
        }, 4500); // Atraso de 4,5 segundos (4500 ms)
    }

    // Método para configurar a cor da StatusBar
    private void configurarStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.azul_Login));
        }
    }

    // Método para configurar a Toolbar
    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Acessibilidade");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_esquerda);
        }
    }
}
