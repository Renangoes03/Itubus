package com.viajet.itubus.activity.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.helper.UsuarioFirebase;
import com.viajet.itubus.activity.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RecargaCartaoActivity extends AppCompatActivity {

    private static final double PRECO_POR_VIAGEM = 5.15;

    private EditText etValorRecarga;
    private EditText etQuantidadeViagem;
    private Button btnRecarregar;

    private boolean isUpdatingValorRecarga = false;
    private boolean isUpdatingQuantidadeViagem = false;
    private double saldoAtual = 0.0;
    private Usuario usuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga_cartao);

        configurarStatusBar();
        configurarToolbar();

        // Inicializar os componentes
        etValorRecarga = findViewById(R.id.et_valor_recarga);
        etQuantidadeViagem = findViewById(R.id.et_quantidade_viagem);
        btnRecarregar = findViewById(R.id.btn_recarregar);

        configurarListeners();

        carregarSaldoAtual();
    }

    private void configurarStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.azul_Login));
        }
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Pagamento");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_esquerda);
        }
    }

    private void configurarListeners() {
        etQuantidadeViagem.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingQuantidadeViagem) return;

                isUpdatingValorRecarga = true;
                try {
                    int quantidade = Integer.parseInt(s.toString());
                    double valorTotal = quantidade * PRECO_POR_VIAGEM;
                    etValorRecarga.setText(String.format("R$ %.2f", valorTotal));
                    btnRecarregar.setText(String.format("Recarregar R$%.2f", valorTotal));
                } catch (NumberFormatException e) {
                    etValorRecarga.setText("");
                }
                isUpdatingValorRecarga = false;
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        etValorRecarga.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingValorRecarga) return;

                isUpdatingQuantidadeViagem = true;
                try {
                    double valor = Double.parseDouble(s.toString().replace("R$", "").replace(",", ".").trim());
                    int quantidade = (int) Math.floor(valor / PRECO_POR_VIAGEM);
                    etQuantidadeViagem.setText(String.valueOf(quantidade));
                    btnRecarregar.setText(String.format("Recarregar R$%.2f", valor));
                } catch (NumberFormatException e) {
                    etQuantidadeViagem.setText("");
                }
                isUpdatingQuantidadeViagem = false;
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        btnRecarregar.setOnClickListener(v -> realizarRecarga());
    }

    private void carregarSaldoAtual() {
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        String userId = usuarioLogado.getId();

        DatabaseReference saldoRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(userId).child("creditoValor");

        saldoRef.get().addOnSuccessListener(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                saldoAtual = dataSnapshot.getValue(Double.class);
                atualizarSaldoUI();
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Erro ao carregar saldo: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

   private void realizarRecarga() {
    String valorRecargaStr = etValorRecarga.getText().toString().replace("R$", "").trim();
    String quantidadeViagemStr = etQuantidadeViagem.getText().toString().trim();

    final double[] valorRecarga = {0.0};
    final int[] quantidadeViagem = {0};

    try {
        // Tenta processar o campo de valor
        if (!valorRecargaStr.isEmpty()) {
            valorRecarga[0] = Double.parseDouble(valorRecargaStr.replace(",", "."));
        }

        // Tenta processar o campo de quantidade
        if (!quantidadeViagemStr.isEmpty()) {
            quantidadeViagem[0] = Integer.parseInt(quantidadeViagemStr);
        }

        // Valida se pelo menos um campo foi preenchido
        if (valorRecarga[0] == 0.0 && quantidadeViagem[0] == 0) {
            Toast.makeText(this, "Preencha pelo menos um dos campos!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Completa os valores faltantes, caso necessário
        if (valorRecarga[0] == 0.0) {
            valorRecarga[0] = quantidadeViagem[0] * PRECO_POR_VIAGEM;
        } else if (quantidadeViagem[0] == 0) {
            quantidadeViagem[0] = (int) Math.floor(valorRecarga[0] / PRECO_POR_VIAGEM);
        }

        // Atualiza o saldo do usuário
        String userId = usuarioLogado.getId();
        DatabaseReference saldoRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(userId).child("creditoValor");

        saldoAtual += valorRecarga[0];

        saldoRef.setValue(saldoAtual).addOnSuccessListener(aVoid -> {
            atualizarSaldoUI();
            // Agora chamamos salvarRecargaHistorico aqui dentro da lambda
            salvarRecargaHistorico(valorRecarga[0], quantidadeViagem[0]);
            Toast.makeText(this, "Recarga efetuada com sucesso!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, "Erro ao realizar recarga: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    } catch (NumberFormatException e) {
        Toast.makeText(this, "Valor inválido! Verifique os campos.", Toast.LENGTH_SHORT).show();
    }
}



    private void salvarRecargaHistorico(double valor, int quantidade) {
        String userId = usuarioLogado.getId();
        DatabaseReference recargaRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(userId).child("recargas");

        String recargaId = recargaRef.push().getKey();
        if (recargaId != null) {
            HashMap<String, Object> dadosRecarga = new HashMap<>();
            dadosRecarga.put("valor", valor);
            dadosRecarga.put("quantidadeViagem", quantidade);
            dadosRecarga.put("horario", new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date()));
            dadosRecarga.put("data", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

            recargaRef.child(recargaId).setValue(dadosRecarga).addOnFailureListener(e ->
                Toast.makeText(this, "Erro ao salvar recarga: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void atualizarSaldoUI() {
        btnRecarregar.setText(String.format("Saldo Atual: R$%.2f", saldoAtual));
    }
}
