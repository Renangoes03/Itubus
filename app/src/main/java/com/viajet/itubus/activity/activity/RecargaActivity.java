package com.viajet.itubus.activity.activity;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.viajet.itubus.R;

import java.util.Calendar;
import java.util.Locale;

public class RecargaActivity extends AppCompatActivity {
    // Variáveis para os elementos da interface
    private TextView monthTextView;
    private Calendar calendar;
    private TextView dateTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recarga);

        // Configuração de padding para ajuste com as barras de sistema
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        monthTextView = findViewById(R.id.monthTextView);

        // Obter o calendário atual e configurar o mês na parte superior
        calendar = Calendar.getInstance();
        String currentMonth = calendar.getDisplayName(Calendar.MONTH, Calendar.LONG, new Locale("pt", "BR"));
        monthTextView.setText(currentMonth);

        // Arrays com as referências de IDs dos elementos
        int[] dayLayouts = {R.id.day1Layout, R.id.day2Layout, R.id.day3Layout, R.id.day4Layout, R.id.day5Layout, R.id.day6Layout, R.id.day7Layout};
        int[] dayNamesIds = {R.id.day1TextView, R.id.day2TextView, R.id.day3TextView, R.id.day4TextView, R.id.day5TextView, R.id.day6TextView, R.id.day7TextView};
        int[] dayNumbersIds = {R.id.day1NumberTextView, R.id.day2NumberTextView, R.id.day3NumberTextView, R.id.day4NumberTextView, R.id.day5NumberTextView, R.id.day6NumberTextView, R.id.day7NumberTextView};

        // Definir o primeiro dia da semana (segunda-feira)
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        // Loop para configurar cada dia da semana
        for (int i = 0; i < 7; i++) {
            LinearLayout dayLayout = findViewById(dayLayouts[i]);
            TextView dayName = findViewById(dayNamesIds[i]);
            TextView dayNumber = findViewById(dayNumbersIds[i]);

            // Configurar o nome do dia (abreviado)
            dayName.setText(getDayOfWeekShort(calendar.get(Calendar.DAY_OF_WEEK)));

            // Configurar o número do dia
            dayNumber.setText(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH)));


            // Avançar para o próximo dia
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

    // Método para obter a abreviação do nome do dia da semana
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

}
