package com.viajet.itubus.activity.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.viajet.itubus.R;

public class VisualizarQRCodeActivity extends AppCompatActivity {

    private LinearLayout containerQRCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_visualizar_qrcode);

        // Configurar a barra de status
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.azul_Login));
        }

        // Ajustar padding para as barras do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializar componentes e configurar a toolbar
        inicializarComponentes();
        configurarToolbar();
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Passagem");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_esquerda);
        }
    }

    private void inicializarComponentes() {
        containerQRCode = findViewById(R.id.containerQRCode);

        // Recuperar o número de QR codes a serem exibidos
        int quantidade = getIntent().getIntExtra("quantidade_qrcodes", 0);

        // Limitar a quantidade de QR codes a 5
        if (quantidade > 5) {
            quantidade = 5;  // Limita a 5 QR codes
        }

        // Exibir os QR codes
        for (int i = 0; i < quantidade; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.qrcode); // A imagem do QR code
            containerQRCode.addView(imageView);
        }
    }
}
