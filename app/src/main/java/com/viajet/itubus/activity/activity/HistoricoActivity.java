package com.viajet.itubus.activity.activity;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.adapter.RecargaAdapter;
import com.viajet.itubus.activity.helper.UsuarioFirebase;
import com.viajet.itubus.activity.model.Recarga;
import com.viajet.itubus.activity.model.Usuario;

import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class HistoricoActivity extends AppCompatActivity {

    private TextView monthTextView;
    private RecyclerView recyclerViewRecargas;
    private List<Recarga> listaRecargas = new ArrayList<>();
    private RecargaAdapter recargaAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico);

        // Inicializa os componentes da interface
        monthTextView = findViewById(R.id.monthTextView);
        recyclerViewRecargas = findViewById(R.id.recyclerViewRecargas);

        // Configura o RecyclerView
        recyclerViewRecargas.setLayoutManager(new LinearLayoutManager(this));
        recargaAdapter = new RecargaAdapter(listaRecargas);
        recyclerViewRecargas.setAdapter(recargaAdapter);

        // Configura a StatusBar
        configurarStatusBar();

        // Configura a Toolbar
        //configurarToolbar();

        // Configura o mês atual no TextView
        configurarMesAtual();

        // Carregar histórico de recargas do Firebase
        carregarHistoricoRecargas();
    }

    private void configurarStatusBar() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.setStatusBarColor(getResources().getColor(R.color.azul_Login));
        }
    }

    /**private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Historico de Recarga");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_esquerda);
        }
    }*/

    private void configurarMesAtual() {
        // Obtém o mês atual e define no TextView
        Calendar calendar = Calendar.getInstance();
        int mes = calendar.get(Calendar.MONTH); // Retorna um índice de 0 a 11
        int ano = calendar.get(Calendar.YEAR);
        String mesAtual = new DateFormatSymbols().getMonths()[mes] + " " + ano;

        monthTextView.setText(mesAtual);
    }

    private void carregarHistoricoRecargas() {
        Usuario usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        if (usuarioLogado != null) {
            String userId = usuarioLogado.getId();

            DatabaseReference recargasRef = FirebaseDatabase.getInstance().getReference("usuarios")
                    .child(userId).child("recargas");

            recargasRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    listaRecargas.clear(); // Garante que os dados anteriores sejam limpos
                    if (snapshot.exists()) {
                        for (DataSnapshot recargaSnapshot : snapshot.getChildren()) {
                            Recarga recarga = recargaSnapshot.getValue(Recarga.class);

                            // Verifica se a recarga foi recuperada corretamente
                            if (recarga != null) {
                                listaRecargas.add(recarga);
                            }
                        }
                        recargaAdapter.notifyDataSetChanged(); // Atualiza o adaptador
                    }
                }

                @Override
                public void onCancelled(DatabaseError error) {
                    // Tratar erros aqui, como exibir uma mensagem de erro
                    // Por exemplo: Log.e("Firebase", "Erro ao carregar recargas", error.toException());
                }
            });
        }
    }
}
