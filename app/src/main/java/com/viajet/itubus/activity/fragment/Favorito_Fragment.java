package com.viajet.itubus.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.activity.AddBusLineActivity;
import com.viajet.itubus.activity.adapter.BusLineAdapter;
import com.viajet.itubus.activity.model.BusLine;

import java.util.ArrayList;
import java.util.List;

public class Favorito_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private BusLineAdapter busLineAdapter;
    private List<BusLine> busLineList = new ArrayList<>();
    private Button btnAddBusLine;
    private DatabaseReference busLinesRef; // Referência ao banco de dados
    private String userId; // ID do usuário autenticado

    public Favorito_Fragment() {
        // Required empty public constructor
    }

    public static Favorito_Fragment newInstance(String param1, String param2) {
        Favorito_Fragment fragment = new Favorito_Fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorito_, container, false);

        // Inicializando o RecyclerView
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Inicializando o botão de adicionar linha
        btnAddBusLine = view.findViewById(R.id.btnAddBusLine);

        // Configurando o clique do botão
        btnAddBusLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddBusLineActivity.class);
                startActivity(intent);
            }
        });

        // Obtendo o ID do usuário autenticado
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            userId = user.getUid();
        } else {
            Toast.makeText(getContext(), "Usuário não autenticado.", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Inicializando a referência ao banco de dados
        busLinesRef = FirebaseDatabase.getInstance().getReference("usuarios")
                .child(userId).child("bus_lines");

        // Configurando o adaptador
        busLineAdapter = new BusLineAdapter(busLineList);
        recyclerView.setAdapter(busLineAdapter);

        // Carregar os dados do Firebase
        carregarBusLines();

        return view;
    }

    private void carregarBusLines() {
        busLinesRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                busLineList.clear(); // Limpa a lista antes de adicionar novos dados
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    BusLine busLine = dataSnapshot.getValue(BusLine.class);
                    if (busLine != null) {
                        busLineList.add(busLine);
                    }
                }
                busLineAdapter.notifyDataSetChanged(); // Atualiza o adaptador
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(), "Erro ao carregar dados: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
