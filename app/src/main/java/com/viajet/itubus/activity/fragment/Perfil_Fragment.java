package com.viajet.itubus.activity.fragment;



import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.activity.BackgroundActivity;
import com.viajet.itubus.activity.activity.EditarPerfilActivity;
import com.viajet.itubus.activity.activity.NotificacaoActivity;
import com.viajet.itubus.activity.helper.UsuarioFirebase;
import com.viajet.itubus.activity.model.Usuario;

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
    private Usuario usuarioLogado;
    private ImageView notificacaoIcon;
    private TextView editNomePerfil;
    private ImageView editIcon;
    private ImageView editFundoIcon;




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

         //Configurações iniciais
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        //Configurações dos Componentes
        inicializarComponentes(view);

        // Configura foto do perfil
        configurarFotoPerfil();

        // Recupera nome do usuário do Firebase e atualiza o TextView
        recuperarNomeUsuario();

        // Configura listeners para ícones e ações
        configurarListeners();

        return view;
    }

    private void inicializarComponentes(View view) {
        progressBarPerfil = view.findViewById(R.id.progressBarPerfil);
        imagePerfil = view.findViewById(R.id.imagemEditarPerfil);
        editFundoIcon = view.findViewById(R.id.editPerfilFundo);
        notificacaoIcon = view.findViewById(R.id.notificacao);
        editNomePerfil = view.findViewById(R.id.editNomePerfil);
        editIcon = view.findViewById(R.id.edit);
    }

    private void configurarFotoPerfil() {
        String caminhoFoto = usuarioLogado.getCaminhoFoto();
        if (caminhoFoto != null) {
            Uri url = Uri.parse(caminhoFoto);
            Glide.with(getActivity())
                    .load(url)
                    .into(imagePerfil);
        }
    }

    private void recuperarNomeUsuario() {
        if (usuarioLogado.getId() != null) {
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference()
                    .child("usuarios")
                    .child(usuarioLogado.getId());

            usuarioRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()) {
                        Usuario usuario = dataSnapshot.getValue(Usuario.class);
                        if (usuario != null) {
                            // Atualiza o TextView com o nome do usuário
                            editNomePerfil.setText("Olá, " + usuario.getNome() + "!");
                        }
                    } else {
                        Toast.makeText(getContext(), "Usuário não encontrado.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Erro ao recuperar dados do usuário: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(getContext(), "ID do usuário inválido.", Toast.LENGTH_SHORT).show();
        }
    }

    private void configurarListeners() {
        notificacaoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirNotificacao();
            }
        });

        imagePerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirFotoPerfil();
            }
        });

        editFundoIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirBackground();
            }
        });

        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                abrirFotoPerfil();
            }
        });
    }

    private void abrirNotificacao() {
        Toast.makeText(getContext(), "Notificações!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), NotificacaoActivity.class);
        startActivity(intent);
    }

    private void abrirFotoPerfil() {
        Toast.makeText(getContext(), "Editar Perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(intent);
    }

    private void abrirBackground() {
        Toast.makeText(getContext(), "Editar Perfil de fundo", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), BackgroundActivity.class);
        startActivity(intent);
    }
}