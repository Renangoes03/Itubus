package com.viajet.itubus.activity.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.viajet.itubus.R;

public class CalendarioHistoricoActivity extends AppCompatActivity {

    private TextView DiaSelecionado;
    private TextView Data;
    private TextView Horario;
    private TextView Valor;
    private TextView Credito;
    private TextView ViagemDisponivel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_calendario_historico);

        // Configura a View para ajustar os insets do sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inicializa os TextViews
        DiaSelecionado = findViewById(R.id.DiaSelecionado);
        Data = findViewById(R.id.Data);
        Horario = findViewById(R.id.Horario);
        Valor = findViewById(R.id.creditoValor); // Corrigido para o ID correto
        Credito = findViewById(R.id.Credito);
        ViagemDisponivel = findViewById(R.id.ViagemDisponivel);

        // Obtenha a data passada da outra atividade
        String selectedDate = getIntent().getStringExtra("selectedDate");

        // Verifica se a data está disponível e define o texto
        if (selectedDate != null) {
            DiaSelecionado.setText("Data selecionada: " + selectedDate);
        }
    }
}
