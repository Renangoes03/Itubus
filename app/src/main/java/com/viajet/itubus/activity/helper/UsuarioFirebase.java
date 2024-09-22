package com.viajet.itubus.activity.helper;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.viajet.itubus.activity.model.Usuario;

public class UsuarioFirebase {

    // Retorna o usuário atualmente autenticado
    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static String getIdentificadorUsuario() {
        FirebaseUser usuario = getUsuarioAtual();
        return (usuario != null) ? usuario.getUid() : null;
    }

    // Atualiza o nome do usuário no perfil e no banco de dados
    public static void atualizarNomeUsuario(String nome) {
        FirebaseUser usuarioLogado = getUsuarioAtual();

        if (usuarioLogado != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(nome)
                    .build();

            usuarioLogado.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar nome de perfil");
                    } else {
                        // Atualiza o nome no banco de dados
                        atualizarNomeNoBancoDeDados(nome);
                    }
                }
            });
        }
    }

    // Atualiza o nome do usuário no banco de dados Firebase
    private static void atualizarNomeNoBancoDeDados(String nome) {
        String userId = getIdentificadorUsuario();
        if (userId != null) {
            DatabaseReference usuarioRef = FirebaseDatabase.getInstance().getReference("usuarios").child(userId).child("nome");
            usuarioRef.setValue(nome).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("Perfil", "Nome atualizado no banco de dados com sucesso");
                    } else {
                        Log.d("Perfil", "Erro ao atualizar o nome no banco de dados");
                    }
                }
            });
        }
    }

    // Atualiza a foto do usuário no perfil
    public static void atualizarFotoUsuario(Uri url) {
        FirebaseUser usuarioLogado = getUsuarioAtual();

        if (usuarioLogado != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            usuarioLogado.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (!task.isSuccessful()) {
                        Log.d("Perfil", "Erro ao atualizar a foto de perfil.");
                    } else {
                        Log.d("Perfil", "Foto de perfil atualizada com sucesso.");
                    }
                }
            });
        }
    }

    // Atualiza a foto de fundo do usuário
    public static void atualizarFotoUsuarioFundo(Uri url) {
        FirebaseUser usuarioLogado = getUsuarioAtual();

        if (usuarioLogado != null) {
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(url)
                    .build();

            usuarioLogado.updateProfile(profile).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Log.d("Perfil de Fundo", "Foto de fundo atualizada no banco de dados com sucesso.");
                    } else {
                        Log.d("Perfil de Fundo", "Erro ao atualizar a foto de fundo no banco de dados.");
                    }
                }
            });
        }
    }

    // Recupera os dados do usuário logado
    public static Usuario getDadosUsuarioLogado() {
        FirebaseUser firebaseUser = getUsuarioAtual();

        if (firebaseUser == null) {
            return null;
        }

        Usuario usuario = new Usuario();
        usuario.setId(firebaseUser.getUid());
        usuario.setNome(firebaseUser.getDisplayName());
        usuario.setEmail(firebaseUser.getEmail());
        usuario.setCaminhoFoto(firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : "");
        usuario.setCaminhoFotoFundo(firebaseUser.getPhotoUrl() != null ? firebaseUser.getPhotoUrl().toString() : "");

        return usuario;
    }
}
