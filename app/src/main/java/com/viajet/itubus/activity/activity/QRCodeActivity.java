package com.viajet.itubus.activity.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.viajet.itubus.R;

import java.io.File;

public class QRCodeActivity extends AppCompatActivity {

    private ImageView ivQrCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        ivQrCode = findViewById(R.id.ivQrCode);

        // Configurar a Toolbar
        configurarToolbar();

        // Recuperar o caminho do arquivo do Intent
        String filePath = getIntent().getStringExtra("QR_CODE_PATH");

        if (filePath != null) {
            // Carregar o QR Code do caminho do arquivo
            File file = new File(filePath);
            if (file.exists()) {
                Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
                ivQrCode.setImageBitmap(bitmap);
            } else {
                Toast.makeText(this, "Arquivo de QR Code não encontrado", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Erro ao receber o caminho do QR Code", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarToolbar() {
        // Configura a Toolbar corretamente
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Passagem");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_esquerda);
        }
    }
}
