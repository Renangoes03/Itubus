package com.viajet.itubus.activity.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.helper.UsuarioFirebase;
import com.viajet.itubus.activity.model.Usuario;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class RecargaActivity extends AppCompatActivity {
    // Variáveis para os elementos da interface
    private TextView monthTextView, NumeroCartao, creditoValor;
    private Calendar calendar;
    private ImageView Passagem, Cartao, GooglePay, ApplePay;
    private EditText etValorRecarga, etQuantidadeViagem;
    private Button btnRecarregar;
   // private TextView saldoAtualTextView;

    private static final double PRECO_POR_VIAGEM = 5.15;

    private Usuario usuarioLogado;

    private boolean isUpdatingValorRecarga = false;
    private boolean isUpdatingQuantidadeViagem = false;
    private double saldoAtual = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga);

          // Define a cor da barra de status para #2196F3
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

        // Inicializar componentes
        inicializarComponentes();

        // Configura o TextWatcher para o campo de número do cartão com máscara
        NumeroCartao.addTextChangedListener(new MaskWatcher("##.##.########-#"));

        // Recupera dados do usuário logado
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        // Obter o calendário atual e configurar o mês na parte superior
        calendar = Calendar.getInstance();
        String currentMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "BR"));
        monthTextView.setText(currentMonth);

        // Configurar os dias da semana
        configurarDiasSemana();

        // Configurar listeners para os ícones
        configurarListeners();

         configListeners();

        // Configurar número do cartão e crédito
        numeroCartao();
        creditoValor();
        carregarSaldoAtual();
    }

    /**
     * Inicializa os componentes do layout.
     */
    private void inicializarComponentes() {
        monthTextView = findViewById(R.id.monthTextView);
        NumeroCartao = findViewById(R.id.NumeroCartao);
        creditoValor = findViewById(R.id.creditoValor);
        Passagem = findViewById(R.id.Passagem);
        Cartao = findViewById(R.id.Cartao);
        GooglePay = findViewById(R.id.GooglePay);
        ApplePay = findViewById(R.id.ApplePay);
        etValorRecarga = findViewById(R.id.etValorRecarga);
        etQuantidadeViagem = findViewById(R.id.etQuantidadeViagem);
        btnRecarregar = findViewById(R.id.btnRecarregar);
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
        String quantidadeViagemStr = etQuantidadeViagem.getText().toString();

        if (!valorRecargaStr.isEmpty() && !quantidadeViagemStr.isEmpty()) {
            try {
                double valorRecarga = Double.parseDouble(valorRecargaStr);
                int quantidadeViagem = Integer.parseInt(quantidadeViagemStr);
                String userId = usuarioLogado.getId();

                DatabaseReference saldoRef = FirebaseDatabase.getInstance().getReference("usuarios")
                        .child(userId).child("creditoValor");

                saldoAtual += valorRecarga;

                saldoRef.setValue(saldoAtual).addOnSuccessListener(aVoid -> {
                    atualizarSaldoUI();
                    salvarRecargaHistorico(valorRecarga, quantidadeViagem);
                    Toast.makeText(this, "Recarga efetuada com sucesso!", Toast.LENGTH_SHORT).show();
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Erro ao realizar recarga: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Valor inválido", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
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
            dadosRecarga.put("data", new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date()));

            recargaRef.child(recargaId).setValue(dadosRecarga).addOnFailureListener(e ->
                Toast.makeText(this, "Erro ao salvar recarga: " + e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

        private void configListeners() {
        etQuantidadeViagem.addTextChangedListener(new android.text.TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isUpdatingQuantidadeViagem) return;

                isUpdatingValorRecarga = true;
                if (!s.toString().isEmpty()) {
                    try {
                        int quantidade = Integer.parseInt(s.toString());
                        double valorTotal = quantidade * PRECO_POR_VIAGEM;
                        etValorRecarga.setText(String.format("R$ %.2f", valorTotal));
                        btnRecarregar.setText(String.format("Recarregar R$%.2f", valorTotal));
                    } catch (NumberFormatException e) {
                        Toast.makeText(RecargaActivity.this, "Quantidade inválida", Toast.LENGTH_SHORT).show();
                    }
                } else if (etValorRecarga.getText().toString().isEmpty()) {
                    btnRecarregar.setText("Recarregar");
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
                if (!s.toString().isEmpty()) {
                    String valorSemSimbolo = s.toString().replace("R$", "").replace(",", ".").trim();
                    try {
                        double valor = Double.parseDouble(valorSemSimbolo);
                        int quantidade = (int) Math.floor(valor / PRECO_POR_VIAGEM);
                        etQuantidadeViagem.setText(String.valueOf(quantidade));
                        btnRecarregar.setText(String.format("Recarregar R$%.2f", valor));
                    } catch (NumberFormatException e) {
                        Toast.makeText(RecargaActivity.this, "Valor inválido", Toast.LENGTH_SHORT).show();
                    }
                } else if (etQuantidadeViagem.getText().toString().isEmpty()) {
                    btnRecarregar.setText("Recarregar");
                }
                isUpdatingQuantidadeViagem = false;
            }

            @Override
            public void afterTextChanged(android.text.Editable s) {}
        });

        btnRecarregar.setOnClickListener(v -> realizarRecarga());
    }

    private void atualizarSaldoUI() {
        btnRecarregar.setText(String.format(Locale.getDefault(), "Saldo Atual: R$ %.2f", saldoAtual));
    }



    /**
     * Configura os dias da semana no layout.
     */
    private void configurarDiasSemana() {
        int[] dayLayouts = {R.id.day1Layout, R.id.day2Layout, R.id.day3Layout, R.id.day4Layout, R.id.day5Layout, R.id.day6Layout, R.id.day7Layout};
        int[] dayNamesIds = {R.id.day1TextView, R.id.day2TextView, R.id.day3TextView, R.id.day4TextView, R.id.day5TextView, R.id.day6TextView, R.id.day7TextView};
        int[] dayNumbersIds = {R.id.day1NumberTextView, R.id.day2NumberTextView, R.id.day3NumberTextView, R.id.day4NumberTextView, R.id.day5NumberTextView, R.id.day6NumberTextView, R.id.day7NumberTextView};

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        for (int i = 0; i < 7; i++) {
            LinearLayout dayLayout = findViewById(dayLayouts[i]);
            TextView dayName = findViewById(dayNamesIds[i]);
            TextView dayNumber = findViewById(dayNumbersIds[i]);

            dayName.setText(getDayOfWeekShort(calendar.get(Calendar.DAY_OF_WEEK)));
            dayNumber.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    /**
     * Retorna a abreviação do dia da semana.
     */
    private String getDayOfWeekShort(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY:
                return "Dom";
            case Calendar.MONDAY:
                return "Seg";
            case Calendar.TUESDAY:
                return "Ter";
            case Calendar.WEDNESDAY:
                return "Qua";
            case Calendar.THURSDAY:
                return "Qui";
            case Calendar.FRIDAY:
                return "Sex";
            case Calendar.SATURDAY:
                return "Sáb";
            default:
                return "";
        }
    }

    /**
     * Configura os listeners dos ícones.
     */
    private void configurarListeners() {
        Passagem.setOnClickListener(v -> abrirAtividade(RecargaPassagemActivity.class, "Recarga de Passagem"));
        Cartao.setOnClickListener(v -> abrirAtividade(RecargaCartaoActivity.class, "Recarga de Cartão"));
        GooglePay.setOnClickListener(v -> abrirAtividade(GooglePayActivity.class, "Google Pay"));
        ApplePay.setOnClickListener(v -> abrirAtividade(ApplePayActivity.class, "Apple Pay"));
    }

    private void abrirAtividade(Class<?> cls, String mensagem) {
        Toast.makeText(this, "Abrindo " + mensagem, Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, cls));
    }

    /**
     * Recupera o número do cartão do usuário.
     */
    public void numeroCartao() {
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(usuarioLogado.getId());

        usuarioRef.child("numeroCartao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String numero = dataSnapshot.getValue(String.class);
                NumeroCartao.setText(numero != null ? "Número do Cartão: " + numero : "Número do Cartão não disponível.");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RecargaActivity.this, "Erro ao recuperar número do cartão: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Recupera o valor do crédito do usuário.
     */
    public void creditoValor() {
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference()
                .child("usuarios").child(usuarioLogado.getId());

        usuarioRef.child("creditoValor").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Double credito = dataSnapshot.getValue(Double.class);
                creditoValor.setText(credito != null ? String.format("R$ %.2f", credito) : "R$ 0.00");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(RecargaActivity.this, "Erro ao recuperar crédito: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}