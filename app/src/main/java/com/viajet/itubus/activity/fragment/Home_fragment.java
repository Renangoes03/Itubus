package com.viajet.itubus.activity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.viajet.itubus.R;
import com.viajet.itubus.activity.activity.NotificacaoActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Home_fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home_fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

        private GridView gridViewViagens;
         private ImageView notificacaoIcon;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Home_fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home_fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Home_fragment newInstance(String param1, String param2) {
        Home_fragment fragment = new Home_fragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_home, container, false);

        //Configurações dos Componentes
        gridViewViagens = view.findViewById(R.id.gridViagem);
        notificacaoIcon = view.findViewById(R.id.notificacao);

   // Encontrar o ícone de notificação no layout
        notificacaoIcon = view.findViewById(R.id.notificacao);

        // Definir o listener de clique para o ícone de notificação
        notificacaoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ação ao clicar no ícone de notificação
                abrirNotificacao();
            }
        });

        return view;
    }

    // Método para abrir a notificação
    private void abrirNotificacao() {
        // Exemplo de ação: mostrar um Toast
        Toast.makeText(getContext(), "Notificação clicada!", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(getActivity(), NotificacaoActivity.class);
                startActivity(i);

        // Aqui você pode adicionar a lógica para abrir uma nova Activity, Dialog, etc.
    }
}
