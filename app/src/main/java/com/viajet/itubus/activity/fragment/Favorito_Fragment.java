package com.viajet.itubus.activity.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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
    private Button btnAddBusLine; // Declaração do botão

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
        btnAddBusLine = view.findViewById(R.id.btnAddBusLine); // Referência do botão

        // Configurando o clique do botão
        btnAddBusLine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir a atividade para adicionar nova linha
                Intent intent = new Intent(getActivity(), AddBusLineActivity.class);
startActivity(intent);

            }
        });

        // Dados fictícios para o RecyclerView
        busLineList.add(new BusLine("Linha 0747", "Centro", "FATEC Itu - Dom", "10,00"));
        busLineList.add(new BusLine("Linha 1234", "Terminal Rodoviário", "FATEC Itu - Dom", "8,50"));

        // Inicializando o Adapter e atribuindo ao RecyclerView
        busLineAdapter = new BusLineAdapter(busLineList);
        recyclerView.setAdapter(busLineAdapter);

        return view;
    }
}
