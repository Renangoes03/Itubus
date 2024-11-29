package com.viajet.itubus.activity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.viajet.itubus.R;
import com.viajet.itubus.activity.activity.LocalizaoActivity;
import com.viajet.itubus.activity.activity.RecargaActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Localizacao_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Localizacao_Fragment extends Fragment {

    private Button acompanharButton;

    private Button passagemButton;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Localizacao_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Localizacao_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Localizacao_Fragment newInstance(String param1, String param2) {
        Localizacao_Fragment fragment = new Localizacao_Fragment();
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

        View view = inflater.inflate(R.layout.fragment_localizacao, container, false);

        acompanharButton = view.findViewById(R.id.acompanhar);

        passagemButton = view.findViewById(R.id.passagem);

        acompanharButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre a tela do OpenStreetMap
                Intent i = new Intent(getActivity(), LocalizaoActivity.class);
                startActivity(i);
            }
        });

        passagemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Abre a tela de recarga
                Intent i = new Intent(getActivity(), RecargaActivity.class);
                startActivity(i);
            }
        });

        return view;
    }
}