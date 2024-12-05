package com.viajet.itubus.activity.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.viajet.itubus.R;

public class QRCodeActivity extends AppCompatActivity {

    private LinearLayout containerImagens;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);

        containerImagens = findViewById(R.id.containerImagens);

        // Configurar a Toolbar
        configurarToolbar();

        // Recuperar o número de passagens (máximo 5)
        int numPassagens = getIntent().getIntExtra("NUM_PASSAGENS", 5);  // Default to 5 if not passed
        if (numPassagens > 5) {
            numPassagens = 5; // Limita a 5 QR codes
        }
        generateQRCodes(numPassagens);
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

    // Method to generate QR codes based on the number of tickets (max 5)
    private void generateQRCodes(int numPassagens) {
        containerImagens.removeAllViews(); // Clear previous QR codes

        MultiFormatWriter writer = new MultiFormatWriter();

        for (int i = 0; i < numPassagens; i++) {
            try {
                String text = "QR Code #" + (i + 1);  // Dynamic QR Code content, can be different for each pass
                Bitmap bitmap = generateQRCodeBitmap(text, 300, 300, writer);

                // Create a new ImageView for each QR code
                ImageView ivQrCode = new ImageView(this);
                ivQrCode.setImageBitmap(bitmap);
                ivQrCode.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                ));

                // Set the click listener to enlarge the image when clicked
                ivQrCode.setOnClickListener(v -> enlargeQRCode(bitmap));

                // Add the ImageView to the container
                containerImagens.addView(ivQrCode);
            } catch (WriterException e) {
                Toast.makeText(this, "Error generating QR code", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Method to enlarge the QR code image when clicked
    private void enlargeQRCode(Bitmap bitmap) {
        // Display the image in a larger size
        // For simplicity, you could use a Toast for testing
        ImageView imageView = new ImageView(this);
        imageView.setImageBitmap(bitmap);

        // You can also open a dialog to show the larger image or open a new activity
        // This example is just to show the image in the same activity
        setContentView(imageView);
    }

    private Bitmap generateQRCodeBitmap(String text, int width, int height, MultiFormatWriter writer) throws WriterException {
        // Codifica o texto em uma matriz 2D de booleanos (BitMatrix) que representa o QR Code
        BitMatrix bitMatrix = writer.encode(text, BarcodeFormat.QR_CODE, width, height);

        // Converte o BitMatrix em um Bitmap
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                // Define a cor do pixel com base na presença de um módulo preto ou branco nessa posição
                bitmap.setPixel(x, y, bitMatrix.get(x, y) ? 0xFF000000 : 0xFFFFFFFF); // Cor preta ou branca
            }
        }

        return bitmap;
    }
}
