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
        etValorRecarga = findViewById(R.id.et_valor_recarga);
        etQuantidadeViagem = findViewById(R.id.et_quantidade_viagem);
        btnRecarregar = findViewById(R.id.btn_recarregar);
        containerImagens = findViewById(R.id.containerImagens);
    }

    private void configListeners() {
        // Listener para o campo de valor da recarga
        etValorRecarga.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingValorRecarga) return;

                // Limpa as imagens de QR code ao alterar o campo de valor
                containerImagens.removeAllViews();

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
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Listener para o campo de quantidade de viagens
        etQuantidadeViagem.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingQuantidadeViagem) return;

                // Limpa as imagens de QR code ao alterar o campo de quantidade
                containerImagens.removeAllViews();

                isUpdatingValorRecarga = true;
                if (!s.toString().isEmpty()) {
                    try {
                        int quantidade = Integer.parseInt(s.toString());
                        double valor = quantidade * PRECO_POR_VIAGEM;
                        etValorRecarga.setText(NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(valor));
                        atualizarImagens(quantidade);
                    } catch (NumberFormatException e) {
                        Toast.makeText(RecargaPassagemActivity.this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
                    }
                }
                isUpdatingValorRecarga = false;
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        // Listener para o botão de recarga
        btnRecarregar.setOnClickListener(v -> {
            // Obter a quantidade de QR codes a ser exibida
            String quantidadeText = etQuantidadeViagem.getText().toString();
            int quantidade = 0;
            try {
                quantidade = Integer.parseInt(quantidadeText);
            } catch (NumberFormatException e) {
                Toast.makeText(RecargaPassagemActivity.this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
            }

            if (quantidade > 0) {
                // Passar a quantidade para a próxima activity
                Intent intent = new Intent(RecargaPassagemActivity.this, VisualizarQRCodeActivity.class);
                intent.putExtra("quantidade_qrcodes", quantidade);
                startActivity(intent);
            } else {
                Toast.makeText(RecargaPassagemActivity.this, "Informe uma quantidade válida", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Método para atualizar as imagens de QR codes
    private void atualizarImagens(int quantidade) {
        containerImagens.removeAllViews();
        for (int i = 0; i < quantidade; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.qrcode); // Substitua com a imagem do QR code
            containerImagens.addView(imageView);
        }
    }
}
