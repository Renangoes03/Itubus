package com.viajet.itubus.activity.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.viajet.itubus.R;

public class AddBusLineActivity extends AppCompatActivity {

    private EditText editTextLine, editTextFrom, editTextTo, editTextPrice;
    private Button btnSave;
    private DatabaseReference busLinesRef; // Referência para o Firebase
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bus_line);

        // Inicializando os campos
        editTextLine = findViewById(R.id.editTextLine);
        editTextFrom = findViewById(R.id.editTextFrom);
        editTextTo = findViewById(R.id.editTextTo);
        editTextPrice = findViewById(R.id.editTextPrice);
        btnSave = findViewById(R.id.btnSave);

        // Definindo valor fixo para o campo editTextPrice
        editTextPrice.setText("R$ 5,15");
        editTextPrice.setEnabled(false); // Desativa a edição

        // Obtendo o usuário logado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid(); // Obtém o ID do usuário
        } else {
            Toast.makeText(this, "Usuário não autenticado.", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializando a referência do Firebase
        busLinesRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(userId).child("bus_lines");

        // Configurando o clique do botão salvar
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBusLine();
            }
        });
    }

    private void saveBusLine() {
        // Obtendo os valores dos campos
        String line = editTextLine.getText().toString().trim();
        String from = editTextFrom.getText().toString().trim();
        String to = editTextTo.getText().toString().trim();
        String price = "R$ 5,15"; // Valor fixo

        // Verificando se os campos estão preenchidos
        if (line.isEmpty() || from.isEmpty() || to.isEmpty()) {
            Toast.makeText(this, "Por favor, preencha todos os campos.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Criando um objeto para armazenar os dados
        BusLine busLine = new BusLine(line, from, to, price);

        // Salvando no Firebase
        busLinesRef.push().setValue(busLine).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(AddBusLineActivity.this, "Linha salva com sucesso!", Toast.LENGTH_SHORT).show();
                finish(); // Fecha a atividade
            } else {
                Toast.makeText(AddBusLineActivity.this, "Erro ao salvar a linha.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Classe para representar a linha de ônibus
    public static class BusLine {
        public String line;
        public String from;
        public String to;
        public String price;

        public BusLine() {
            // Construtor vazio necessário para o Firebase
        }

        public BusLine(String line, String from, String to, String price) {
            this.line = line;
            this.from = from;
            this.to = to;
            this.price = price;
        }
    }
}
