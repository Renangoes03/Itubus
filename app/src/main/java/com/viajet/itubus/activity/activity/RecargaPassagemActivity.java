package com.viajet.itubus.activity.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.viajet.itubus.R;

import java.text.NumberFormat;
import java.util.Locale;

public class RecargaPassagemActivity extends AppCompatActivity {

    private static final double PRECO_POR_VIAGEM = 5.15;

    private Button btnRecarregar;
    private EditText etValorRecarga, etQuantidadeViagem;
    private LinearLayout containerImagens;
    private boolean isUpdatingValorRecarga = false;
    private boolean isUpdatingQuantidadeViagem = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga_passagem);

        // Configurar a barra de status
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.azul_Login));
        }

        // Ajustar padding para as barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();
        configListeners();
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
        etValorRecarga = findViewById(R.id.etValorRecarga);
        etQuantidadeViagem = findViewById(R.id.etQuantidadeViagem);
        btnRecarregar = findViewById(R.id.btnRecarregar);
        containerImagens = findViewById(R.id.containerImagens);
    }

    private void configListeners() {
        etValorRecarga.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingValorRecarga) return;

                isUpdatingQuantidadeViagem = true;
                if (!s.toString().isEmpty()) {
                    String valorSemSimbolo = s.toString().replace("R$", "").replace(",", ".").trim();
                    try {
                        double valor = Double.parseDouble(valorSemSimbolo);
                        int quantidade = (int) Math.floor(valor / PRECO_POR_VIAGEM);
                        etQuantidadeViagem.setText(String.valueOf(quantidade));
                        atualizarImagens(quantidade);
                    } catch (NumberFormatException e) {
                        Toast.makeText(RecargaPassagemActivity.this, "Valor inválido", Toast.LENGTH_SHORT).show();
                    }
                }
                isUpdatingQuantidadeViagem = false;
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        etQuantidadeViagem.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingQuantidadeViagem) return;

                isUpdatingValorRecarga = true;
                if (!s.toString().isEmpty()) {
                    try {
                        int quantidade = Integer.parseInt(s.toString());
                        double valor = quantidade * PRECO_POR_VIAGEM;
                        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
                        etValorRecarga.setText(currencyFormat.format(valor));
                        atualizarImagens(quantidade);
                    } catch (NumberFormatException e) {
                        Toast.makeText(RecargaPassagemActivity.this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
                    }
                }
                isUpdatingValorRecarga = false;
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {
            }
        });

        btnRecarregar.setOnClickListener(v -> realizarRecarga());
    }

    private void realizarRecarga() {
        String valorRecargaStr = etValorRecarga.getText().toString().replace("R$", "").trim();
        String quantidadeViagemStr = etQuantidadeViagem.getText().toString();

        try {
            double valorRecarga = 0;
            int quantidadeViagem = 0;

            if (!quantidadeViagemStr.isEmpty()) {
                quantidadeViagem = Integer.parseInt(quantidadeViagemStr);
                valorRecarga = quantidadeViagem * PRECO_POR_VIAGEM;
            }

            if (!valorRecargaStr.isEmpty() && quantidadeViagem == 0) {
                valorRecarga = Double.parseDouble(valorRecargaStr);
                quantidadeViagem = (int) Math.floor(valorRecarga / PRECO_POR_VIAGEM);
            }

            if (quantidadeViagem > 0 && valorRecarga >= PRECO_POR_VIAGEM) {
                Intent intent = new Intent(RecargaPassagemActivity.this, QRCodeActivity.class);
                startActivity(intent);

                Toast.makeText(this, "Recarga efetuada com sucesso!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Valor insuficiente para recarga.", Toast.LENGTH_SHORT).show();
            }
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Preencha os campos corretamente!", Toast.LENGTH_SHORT).show();
        }
    }

    private void atualizarImagens(int quantidade) {
        containerImagens.removeAllViews();

        for (int i = 0; i < quantidade; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.qrcode);
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.setMargins(dpToPx(8), dpToPx(8), dpToPx(8), dpToPx(8));
            imageView.setLayoutParams(params);

            containerImagens.addView(imageView);
        }
    }

    private int dpToPx(int dp) {
        float density = getResources().getDisplayMetrics().density;
        return Math.round(dp * density);
    }
}
