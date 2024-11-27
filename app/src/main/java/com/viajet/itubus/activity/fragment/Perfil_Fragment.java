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
import com.viajet.itubus.activity.activity.HistoricoActivity;
import com.viajet.itubus.activity.activity.NotificacaoActivity;
import com.viajet.itubus.activity.activity.RecargaActivity;
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
    private Usuario usuarioLogado;
    private ImageView notificacaoIcon;
    private TextView editNomePerfil;
    private ImageView editIcon;
    private ImageView imageFundo;
    private TextView recarga;
    private Button button_notificacao;
    private Button button_recarga;
    private Button button_historico;

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

        // Configura foto do perfil de fundo
        configurarFotoPerfilFundo();

        // Recupera nome do usuário do Firebase e atualiza o TextView
        recuperarNomeUsuario();

        // Configura os listeners dos ícones de notificação e perfil
        configurarListeners();

         //Recuperar foto do usuário
            String caminhoFoto = usuarioLogado.getCaminhoFoto();
            if( caminhoFoto != null ){
                Uri url = Uri.parse( caminhoFoto );
                Glide.with(getActivity())
                        .load( url )
                        .into( imagePerfil );
            }

        //Abre edição do perfil
        editIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Abre a tela de edição do perfil
                Intent i = new Intent(getActivity(), EditarPerfilActivity.class);
                startActivity(i);
            }

        });

        return view;
    }

  /**
 * Configura a foto do perfil do usuário, se disponível.
 */
private void configurarFotoPerfil() {
    String caminhoFoto = usuarioLogado.getCaminhoFoto();

    if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
        // Carrega a foto do perfil usando o Glide
        Uri url = Uri.parse(caminhoFoto);
        Glide.with(getActivity())
                .load(url)
                .placeholder(R.drawable.profile_picture) // Caso a imagem demore a carregar, mostra a imagem padrão
                .error(R.drawable.profile_picture) // Caso ocorra um erro no carregamento, mostra a imagem padrão
                .into(imagePerfil);
    } else {
        // Se não houver foto de perfil, usa a imagem padrão
        imagePerfil.setImageResource(R.drawable.profile_picture);
    }
}

/**
 * Configura a foto de fundo do perfil do usuário, se disponível.
 */
private void configurarFotoPerfilFundo() {
    String caminhoFotoFundo = usuarioLogado.getCaminhoFoto();

    if (caminhoFotoFundo != null && !caminhoFotoFundo.isEmpty()) {
        // Carrega a foto de fundo do perfil usando o Glide
        Uri url = Uri.parse(caminhoFotoFundo);
        Glide.with(getActivity())
                .load(url)
                .placeholder(R.drawable.profile_picture) // Imagem padrão enquanto carrega
                .error(R.drawable.profile_picture) // Imagem padrão em caso de erro
                .into(imageFundo);
    } else {
        // Se não houver foto de fundo, usa a imagem padrão
        imageFundo.setImageResource(R.drawable.logo);
    }
}


    private void inicializarComponentes(View view) {
        progressBarPerfil = view.findViewById(R.id.progressBarPerfil);
        imagePerfil = view.findViewById(R.id.imagemEditarPerfil);
        notificacaoIcon = view.findViewById(R.id.notificacao);
        editNomePerfil = view.findViewById(R.id.editNomePerfil);
        editIcon = view.findViewById(R.id.edit);
        imageFundo = view.findViewById(R.id.imagemEditarPerfilFundo);
       button_notificacao = view.findViewById(R.id.button_notificacao);
       button_recarga = view.findViewById(R.id.button_recarga);
       button_historico = view.findViewById(R.id.button_historico);
    }
    /**
 * Configura a foto do perfil do usuário, se disponível.
 */



     /**
     * Recupera o nome do usuário do Firebase e atualiza o TextView correspondente.
     */
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

    /**
     * Configura os listeners para os ícones de notificação e perfil.
     */
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

        button_notificacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Notificacao();
            }
        });

         button_recarga.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirRecarga();
            }
        });
          button_historico.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {abrirHistorico();}
        });
    }

    /**
     * Método para abrir a tela de notificações.
     */
    private void abrirNotificacao() {
        Toast.makeText(getContext(), "Notificações!", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), NotificacaoActivity.class);
        startActivity(intent);
    }

    /**
     * Método para abrir a tela de edição de perfil.
     */
    private void abrirFotoPerfil() {
        Toast.makeText(getContext(), "Editar Perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(intent);
    }
        /**
     * Método para abrir a tela de Recargaa.
     */
    private void abrirRecarga() {
        Toast.makeText(getContext(), "Recarga", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), RecargaActivity.class);
        startActivity(intent);
    }

       private void Notificacao() {
        Toast.makeText(getContext(), "Editar Perfil", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), NotificacaoActivity.class);
        startActivity(intent);
    }

     /**
     * Método para abrir a tela de Histórico.
     */
    private void abrirHistorico() {
        Toast.makeText(getContext(), "Histórico", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getActivity(), HistoricoActivity.class);
        startActivity(intent);
    }
}


