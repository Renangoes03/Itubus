package com.viajet.itubus.activity.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.activity.EditarPerfilActivity;
import com.viajet.itubus.activity.activity.MaskWatcher;
import com.viajet.itubus.activity.activity.NotificacaoActivity;
import com.viajet.itubus.activity.helper.UsuarioFirebase;
import com.viajet.itubus.activity.model.Usuario;

/**
 * Fragment responsável pela tela inicial do aplicativo.
 */
public class Home_fragment extends Fragment {

    // Componentes do layout
    private GridView gridViewViagens;
    private ImageView notificacaoIcon;
    private ImageView imagePerfil;
    private TextView editNomePerfil;
    private TextView credito;
    private TextView NumeroCartao;

    // Usuário logado
    private Usuario usuarioLogado;

    public Home_fragment() {
        // Construtor público vazio requerido
    }

    /**
     * Método estático para criar uma nova instância do fragment com argumentos.
     *
     * @param param1 Parâmetro 1.
     * @param param2 Parâmetro 2.
     * @return Nova instância de Home_fragment.
     */
    public static Home_fragment newInstance(String param1, String param2) {
        Home_fragment fragment = new Home_fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            // Recupera os parâmetros se necessário
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla o layout do fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializa os componentes do layout
        inicializarComponentes(view);

          // Configura o TextWatcher para o campo de número do cartão com máscara
    NumeroCartao.addTextChangedListener(new MaskWatcher("##.##.########-#"));

        // Recupera dados do usuário logado
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        // Configura foto do perfil
        configurarFotoPerfil();

        // Recupera nome do usuário do Firebase e atualiza o TextView
        recuperarNomeUsuario();

        // Configura os listeners dos ícones de notificação e perfil
        configurarListeners();

        // Configura o número do cartão
         numeroCartao(); // Chama o método aqui

        return view;
    }

    /**
     * Inicializa os componentes do layout.
     */
    private void inicializarComponentes(View view) {
        imagePerfil = view.findViewById(R.id.FotoPerfil);
        editNomePerfil = view.findViewById(R.id.editNomePerfil);
        notificacaoIcon = view.findViewById(R.id.notificacao);
        gridViewViagens = view.findViewById(R.id.gridViagem);
        NumeroCartao = view.findViewById(R.id.NumeroCartao);
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
 * Método para configurar o número do cartão do usuário.
 */
public void numeroCartao() {
    if (usuarioLogado.getId() != null) {
        DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference()
                .child("usuarios")
                .child(usuarioLogado.getId());

        usuarioRef.child("numeroCartao").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String numero = dataSnapshot.getValue(String.class);
                    if (numero != null && !numero.isEmpty()) {
                        // Atualiza o TextView com o número do cartão
                        NumeroCartao.setText("Número do Cartão: " + numero);
                    } else {
                        NumeroCartao.setText("Número do Cartão não disponível.");
                    }
                } else {
                    NumeroCartao.setText("Usuário não encontrado.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Erro ao recuperar número do cartão: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    } else {
        NumeroCartao.setText("ID do usuário inválido.");
    }
}

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
    }

    /**
     * Método para abrir a tela de notificações.
     */
    private void abrirNotificacao() {
        Toast.makeText(getContext(), "Notificação clicada!", Toast.LENGTH_SHORT).show();
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
}
