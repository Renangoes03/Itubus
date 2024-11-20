package com.viajet.itubus.activity.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viajet.itubus.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CalendarioActivity extends AppCompatActivity {

    private TextView monthTextView;
    private Calendar calendar;
    private DatabaseReference recargaRef;
    private String userId = "4yN8FRU6m4SI2eJ9ZmRgekB89I33"; // Substitua pelo ID do usuário logado.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendario);

        monthTextView = findViewById(R.id.monthTextView);

        // Configurar referência ao Firebase
        recargaRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(userId).child("recargas");

        // Obter o calendário atual e configurar o mês na parte superior
        calendar = Calendar.getInstance();
        String currentMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "BR"));
        monthTextView.setText(currentMonth);

        // Arrays com as referências de IDs dos elementos
        int[] dayLayouts = {R.id.day1Layout, R.id.day2Layout, R.id.day3Layout, R.id.day4Layout, R.id.day5Layout, R.id.day6Layout, R.id.day7Layout};
        int[] dayNamesIds = {R.id.day1TextView, R.id.day2TextView, R.id.day3TextView, R.id.day4TextView, R.id.day5TextView, R.id.day6TextView, R.id.day7TextView};
        int[] dayNumbersIds = {R.id.day1NumberTextView, R.id.day2NumberTextView, R.id.day3NumberTextView, R.id.day4NumberTextView, R.id.day5NumberTextView, R.id.day6NumberTextView, R.id.day7NumberTextView};

        // Configurar o primeiro dia da semana (segunda-feira)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Loop para configurar cada dia da semana
        for (int i = 0; i < 7; i++) {
            LinearLayout dayLayout = findViewById(dayLayouts[i]);
            TextView dayName = findViewById(dayNamesIds[i]);
            TextView dayNumber = findViewById(dayNumbersIds[i]);

            // Configurar nome do dia (abreviado)
            dayName.setText(getDayOfWeekShort(calendar.get(Calendar.DAY_OF_WEEK)));

            // Configurar número do dia
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            dayNumber.setText(String.valueOf(day));

            // Clique no dia para exibir detalhes
            final String selectedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());
            dayLayout.setOnClickListener(v -> fetchDayDetails(selectedDate));

            // Avançar para o próximo dia
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    // Método para obter a abreviação do nome do dia da semana
    private String getDayOfWeekShort(int dayOfWeek) {
        switch (dayOfWeek) {
            case Calendar.SUNDAY: return "Dom";
            case Calendar.MONDAY: return "Seg";
            case Calendar.TUESDAY: return "Ter";
            case Calendar.WEDNESDAY: return "Qua";
            case Calendar.THURSDAY: return "Qui";
            case Calendar.FRIDAY: return "Sex";
            case Calendar.SATURDAY: return "Sáb";
            default: return "";
        }
    }

    // Buscar e exibir detalhes do Firebase
    private void fetchDayDetails(String date) {
        recargaRef.orderByChild("data").equalTo(date).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    StringBuilder details = new StringBuilder();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String valor = snapshot.child("valor").getValue(String.class);
                        String quantidadeViagem = snapshot.child("quantidadeViagem").getValue(String.class);
                        String data = snapshot.child("data").getValue(String.class);

                        details.append("Valor: ").append(valor).append("\n");
                        details.append("Quantidade de Viagens: ").append(quantidadeViagem).append("\n");
                        details.append("Data: ").append(data).append("\n\n");
                    }
                    // Exibir as informações
                    Toast.makeText(CalendarioActivity.this, details.toString(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(CalendarioActivity.this, "Nenhuma recarga encontrada para " + date, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CalendarioActivity.this, "Erro ao buscar dados: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
