package com.viajet.itubus.activity.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.viajet.itubus.R;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.activity.LoginActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Configuracao_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Configuracao_Fragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public Configuracao_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Configuracao_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static Configuracao_Fragment newInstance(String param1, String param2) {
        Configuracao_Fragment fragment = new Configuracao_Fragment();
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
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_configuracao, container, false);

        // Obter referência ao botão
        Button deleteAccountButton = view.findViewById(R.id.button_delete_account);

        // Configurar listener para exclusão de conta
        deleteAccountButton.setOnClickListener(v -> deleteUserAccount());

        return view;
    }

     private void deleteUserAccount() {
        // Obter o ID do usuário logado
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (userId == null) {
            Toast.makeText(getContext(), "Erro: Usuário não autenticado.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Referência ao nó "usuarios" no Firebase
        DatabaseReference usuariosRef = FirebaseDatabase.getInstance().getReference("usuarios");

        // Remover o usuário pelo ID
        usuariosRef.child(userId).removeValue()
            .addOnSuccessListener(aVoid -> {
                // Excluir dados no Firebase Authentication
                FirebaseAuth.getInstance().getCurrentUser().delete()
                    .addOnSuccessListener(unused -> {
                        // Deslogar o usuário
                        FirebaseAuth.getInstance().signOut();

                        // Mostrar mensagem de sucesso
                        Toast.makeText(getContext(), "Conta excluída com sucesso!", Toast.LENGTH_SHORT).show();

                        // Redirecionar para a tela de login
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // Remove o histórico
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), "Erro ao excluir conta do Firebase Auth: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
            })
            .addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Erro ao excluir conta do banco de dados: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
    }
}