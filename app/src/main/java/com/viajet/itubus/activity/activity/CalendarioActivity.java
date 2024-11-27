package com.viajet.itubus.activity.activity;

import android.content.Intent;
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
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CalendarioActivity extends AppCompatActivity {

    private TextView monthTextView;
    private Calendar calendar;
    private DatabaseReference recargaRef;
    private String userId = "4yN8FRU6m4SI2eJ9ZmRgekB89I33"; // Substitua pelo ID do usuário logado.

    private final Map<String, Boolean> recargasMap = new HashMap<>();

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

        // Buscar recargas do Firebase
        fetchRecargasFromFirebase(() -> configureDaysOfWeek());
    }

    private void fetchRecargasFromFirebase(Runnable onComplete) {
        recargaRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot recargaSnapshot : dataSnapshot.getChildren()) {
                    String data = recargaSnapshot.child("data").getValue(String.class);
                    if (data != null) {
                        recargasMap.put(data, true);
                    }
                }
                onComplete.run();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(CalendarioActivity.this, "Erro ao buscar recargas: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void configureDaysOfWeek() {
        int[] dayLayouts = {R.id.day1Layout, R.id.day2Layout, R.id.day3Layout, R.id.day4Layout, R.id.day5Layout, R.id.day6Layout, R.id.day7Layout};
        int[] dayNamesIds = {R.id.day1TextView, R.id.day2TextView, R.id.day3TextView, R.id.day4TextView, R.id.day5TextView, R.id.day6TextView, R.id.day7TextView};
        int[] dayNumbersIds = {R.id.day1NumberTextView, R.id.day2NumberTextView, R.id.day3NumberTextView, R.id.day4NumberTextView, R.id.day5NumberTextView, R.id.day6NumberTextView, R.id.day7NumberTextView};

        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);

        String today = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Calendar.getInstance().getTime());

        for (int i = 0; i < 7; i++) {
            LinearLayout dayLayout = findViewById(dayLayouts[i]);
            TextView dayName = findViewById(dayNamesIds[i]);
            TextView dayNumber = findViewById(dayNumbersIds[i]);

            dayName.setText(getDayOfWeekShort(calendar.get(Calendar.DAY_OF_WEEK)));

            int day = calendar.get(Calendar.DAY_OF_MONTH);
            dayNumber.setText(String.valueOf(day));

            String selectedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(calendar.getTime());

            dayLayout.setOnClickListener(v -> {
                if (selectedDate.equals(today)) {
                    navigateToHistorico(selectedDate);
                } else if (recargasMap.containsKey(selectedDate)) {
                    navigateToHistorico(selectedDate);
                } else {
                    Toast.makeText(this, "Nenhuma recarga para " + selectedDate, Toast.LENGTH_SHORT).show();
                }
            });

            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
    }

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

    private void navigateToHistorico(String date) {
        Intent intent = new Intent(CalendarioActivity.this, CalendarioHistoricoActivity.class);
        intent.putExtra("selectedDate", date);
        startActivity(intent);
    }
}
