package com.viajet.itubus.activity.helper;

import android.net.Uri;
import android.util.Log;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.viajet.itubus.activity.model.Usuario;

public class UsuarioFirebase {

    // Retorna o usuário atualmente autenticado
    public static FirebaseUser getUsuarioAtual() {
        FirebaseAuth usuario = ConfiguracaoFirebase.getFirebaseAutenticacao();
        return usuario.getCurrentUser();
    }

    public static String getIdentificadorUsuario() {
        return getUsuarioAtual().getUid();
    }
    // Atualiza o nome do usuário no perfil
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
                    }
                }
            });
        }
    }
    public static void atualizarFotoUsuario(Uri url){

        try {

            //Usuario logado no App
            FirebaseUser usuarioLogado = getUsuarioAtual();

            //Configurar objeto para alteração do perfil
            UserProfileChangeRequest profile = new UserProfileChangeRequest
                    .Builder()
                    .setPhotoUri( url )
                    .build();
            usuarioLogado.updateProfile( profile ).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if( !task.isSuccessful() ){
                        Log.d("Perfil","Erro ao atualizar a foto de perfil." );
                    }
                }
            });

        }catch (Exception e){
            e.printStackTrace();
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

        return usuario;
    }
}
