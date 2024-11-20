package com.viajet.itubus.activity.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.viajet.itubus.R;

public class RecargaPassagemActivity extends AppCompatActivity {

    private static final double PRECO_POR_VIAGEM = 5.15;

    private TextView btnRecarregar;
    private EditText etValorRecarga, etQuantidadeViagem;
    private LinearLayout containerImagens; // Novo componente para exibir imagens duplicadas
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

        // Configuração de padding para ajuste com as barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();
        configListeners();
    }

    private void inicializarComponentes() {
        // Inicializando os componentes
        etValorRecarga = findViewById(R.id.et_valor_recarga);
        etQuantidadeViagem = findViewById(R.id.et_quantidade_viagem);
        btnRecarregar = findViewById(R.id.btn_recarregar);
         // Inicializando o container de imagens
    }

    private void configListeners() {
        // Listener para o campo de valor de recarga
        if (etValorRecarga != null) {
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
                            atualizarImagens(quantidade); // Atualizar as imagens dinamicamente
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
        }

        // Listener para o campo de quantidade de viagens
        if (etQuantidadeViagem != null) {
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
                            etValorRecarga.setText(String.format("R$%.2f", valor));
                            atualizarImagens(quantidade); // Atualizar as imagens dinamicamente
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
        }

        // Listener para o botão de recarga
        if (btnRecarregar != null) {
            btnRecarregar.setOnClickListener(v -> realizarRecarga());
        }
    }

    private void realizarRecarga() {
        String valorRecargaStr = etValorRecarga.getText().toString().replace("R$", "").trim();
        String quantidadeViagemStr = etQuantidadeViagem.getText().toString();

        try {
            double valorRecarga = 0;
            int quantidadeViagem = 0;

            // Verifica se a quantidade de viagens foi preenchida
            if (!quantidadeViagemStr.isEmpty()) {
                quantidadeViagem = Integer.parseInt(quantidadeViagemStr);
                valorRecarga = quantidadeViagem * PRECO_POR_VIAGEM;
            }

            // Verifica se o valor de recarga foi preenchido
            if (!valorRecargaStr.isEmpty() && quantidadeViagem == 0) {
                valorRecarga = Double.parseDouble(valorRecargaStr);
                quantidadeViagem = (int) Math.floor(valorRecarga / PRECO_POR_VIAGEM);
            }

            // Validar os valores calculados
            if (quantidadeViagem > 0 && valorRecarga >= PRECO_POR_VIAGEM) {
                // Navegar para a QRCodeActivity
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

    /**
     * Atualiza o layout de imagens com base na quantidade informada.
     */
    private void atualizarImagens(int quantidade) {
        containerImagens.removeAllViews(); // Limpa as imagens anteriores

        for (int i = 0; i < quantidade; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.qrcode); // Substitua pelo recurso de imagem desejado
            imageView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));

            // Adicione margens, se necessário
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
            params.setMargins(8, 8, 8, 8); // Margens (em pixels)
            imageView.setLayoutParams(params);

            containerImagens.addView(imageView); // Adiciona a imagem ao container
        }
    }
}
