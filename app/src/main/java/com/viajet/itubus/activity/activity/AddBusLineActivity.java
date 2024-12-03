package com.viajet.itubus.activity.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import com.viajet.itubus.R;

public class AddBusLineActivity extends AppCompatActivity {

    private EditText editTextLine, editTextFrom, editTextTo, editTextPrice;
    private Button btnSave;

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

        // Configurando o clique do botão salvar
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Lógica para salvar os dados (não implementada aqui)
                // Você pode retornar os dados para o fragmento ou salvar em um banco de dados
                finish(); // Fecha a atividade
            }
        });
    }
}
