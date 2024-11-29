package com.viajet.itubus.activity.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
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
    private TextView creditoValor;
    private TextView NumeroCartao;

    // Usuário logado
    private Usuario usuarioLogado;

    public Home_fragment() {
        // Construtor público vazio requerido
    }

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

        // Configura a barra de status
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP && getActivity() != null) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.azul_Login));
        }

        if (getArguments() != null) {
            String mParam1 = getArguments().getString("param1");
            String mParam2 = getArguments().getString("param2");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Inicializa os componentes do layout
        inicializarComponentes(view);

        // Configura máscara para o número do cartão
        NumeroCartao.addTextChangedListener(new MaskWatcher("##.##.########-#"));

        // Recupera dados do usuário logado
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();

        // Configura a foto do perfil
        configurarFotoPerfil();

        // Recupera nome do usuário
        recuperarNomeUsuario();

        // Configura os listeners
        configurarListeners();

        // Recupera e exibe o número do cartão
        numeroCartao();

        // Recupera e exibe o valor do crédito
        creditoValor();

        return view;
    }

    private void inicializarComponentes(View view) {
        imagePerfil = view.findViewById(R.id.FotoPerfil);
        editNomePerfil = view.findViewById(R.id.editNomePerfil);
        notificacaoIcon = view.findViewById(R.id.notificacao);
        gridViewViagens = view.findViewById(R.id.gridViagem);
        NumeroCartao = view.findViewById(R.id.NumeroCartao);
        creditoValor = view.findViewById(R.id.creditoValor);
    }

    private void configurarFotoPerfil() {
        String caminhoFoto = usuarioLogado.getCaminhoFoto();

        if (caminhoFoto != null && !caminhoFoto.isEmpty()) {
            Uri url = Uri.parse(caminhoFoto);
            Glide.with(requireActivity())
                    .load(url)
                    .placeholder(R.drawable.profile_picture)
                    .error(R.drawable.profile_picture)
                    .into(imagePerfil);
        } else {
            imagePerfil.setImageResource(R.drawable.profile_picture);
        }
    }

    public void numeroCartao() {
        if (usuarioLogado.getId() != null) {
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference()
                    .child("usuarios")
                    .child(usuarioLogado.getId());

            usuarioRef.child("numeroCartao").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String numero = dataSnapshot.getValue(String.class);
                    if (numero != null && !numero.isEmpty()) {
                        NumeroCartao.setText("Número do Cartão: " + numero);
                    } else {
                        NumeroCartao.setText("Número do Cartão não disponível.");
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

    public void creditoValor() {
        if (usuarioLogado != null && usuarioLogado.getId() != null) {
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference()
                    .child("usuarios")
                    .child(usuarioLogado.getId());

            usuarioRef.child("creditoValor").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Double credito = dataSnapshot.getValue(Double.class);
                    if (credito != null) {
                        String creditoFormatado = String.format("R$ %.2f", credito);
                        creditoValor.setText(creditoFormatado);
                    } else {
                        creditoValor.setText("R$ 0.00");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Erro ao recuperar crédito: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            creditoValor.setText("Usuário não autenticado.");
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
                    Usuario usuario = dataSnapshot.getValue(Usuario.class);
                    if (usuario != null) {
                        editNomePerfil.setText("Olá, " + usuario.getNome() + "!");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Erro ao recuperar dados do usuário: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void configurarListeners() {
        notificacaoIcon.setOnClickListener(v -> abrirNotificacao());
        imagePerfil.setOnClickListener(v -> abrirFotoPerfil());
    }

    private void abrirNotificacao() {
        Intent intent = new Intent(getActivity(), NotificacaoActivity.class);
        startActivity(intent);
    }

    private void abrirFotoPerfil() {
        Intent intent = new Intent(getActivity(), EditarPerfilActivity.class);
        startActivity(intent);
    }
}
