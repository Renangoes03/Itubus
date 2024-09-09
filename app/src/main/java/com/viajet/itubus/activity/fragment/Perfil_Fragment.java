package com.viajet.itubus.activity.fragment;



import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;

import com.viajet.itubus.R;
import com.viajet.itubus.activity.activity.EditarPerfilActivity;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Perfil_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Perfil_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private ProgressBar progressBarPerfil;
    private CircleImageView imagePerfil;
    private Button buttonEditarPerfil;
    private GridView gridViagem;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Perfil_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Perfil_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Perfil_Fragment newInstance(String param1, String param2) {
        Perfil_Fragment fragment = new Perfil_Fragment();
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
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        //Configurações dos Componentes
        gridViagem = view.findViewById(R.id.gridViagem);
        progressBarPerfil = view.findViewById(R.id.progressBarPerfil);
        imagePerfil = view.findViewById(R.id.imagemEditarPerfil);
        buttonEditarPerfil = view.findViewById(R.id.buttonEditarPerfil);




        //Abre edição do perfil
        buttonEditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abre a tela de edição do perfil
                Intent i = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }
        });
        return view;
    }
}