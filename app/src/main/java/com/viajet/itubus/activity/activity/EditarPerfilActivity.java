package com.viajet.itubus.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.helper.ConfiguracaoFirebase;
import com.viajet.itubus.activity.helper.UsuarioFirebase;
import com.viajet.itubus.activity.model.Usuario;

import java.io.ByteArrayOutputStream;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditarPerfilActivity extends AppCompatActivity {
    private CircleImageView imagemEditarPerfil;
    private TextView editNomePerfil, editEmailPerfil, textAlterarFoto;
    private Button buttonSalvarAlteracoes;
    private Usuario usuarioLogado;
    private static final int SELECAO_GALERIA = 200;
    private StorageReference storageRef;
    private String identificadorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_editar_perfil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configurações iniciais
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        // Configura toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar perfil");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_esquerda);
        }

        // Inicializar componentes
        inicializarComponentes();

        // Recuperar dados do usuário
        FirebaseUser usuarioPerfil = UsuarioFirebase.getUsuarioAtual();
        editNomePerfil.setText(usuarioPerfil.getDisplayName());
        editEmailPerfil.setText(usuarioPerfil.getEmail());

        Uri url = usuarioPerfil.getPhotoUrl();
        if (url != null) {
            imagemEditarPerfil.setImageURI(url);
            Glide.with(EditarPerfilActivity.this)
                    .load(url)
                    .into(imagemEditarPerfil);
        }else {
            imagemEditarPerfil.setImageResource(R.drawable.profile_picture);
        }

        // Salvar alterações do Nome
        buttonSalvarAlteracoes.setOnClickListener(v -> {
            String nomeAtualizado = editNomePerfil.getText().toString();

            // Atualizar nome no Firebase
            UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado);

            // Atualizar nome no banco de dados
            usuarioLogado.setNome(nomeAtualizado);
            usuarioLogado.atualizar();

            Toast.makeText(EditarPerfilActivity.this, "Dados alterados com sucesso!",
                    Toast.LENGTH_SHORT).show();
        });

        // Alterar foto do usuário
        textAlterarFoto.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (i.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(i, SELECAO_GALERIA);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == SELECAO_GALERIA) {
            Uri localImagemSelecionada = data.getData();
            if (localImagemSelecionada != null) {
                try {
                    Bitmap imagem = MediaStore.Images.Media.getBitmap(getContentResolver(), localImagemSelecionada);
                    if (imagem != null) {
                        // Configura imagem na tela
                        imagemEditarPerfil.setImageBitmap(imagem);

                        // Recuperar dados da imagem para o Firebase
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                        byte[] dadosImagem = baos.toByteArray();

                        // Salvar imagem no Firebase
                        StorageReference imagemRef = storageRef
                                .child("imagens")
                                .child("perfil")
                                .child(identificadorUsuario + ".jpeg");

                        UploadTask uploadTask = imagemRef.putBytes(dadosImagem);
                        uploadTask.addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(EditarPerfilActivity.this,
                                        "Erro ao fazer upload da imagem",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }).addOnSuccessListener(taskSnapshot -> {
                            // Após o upload da imagem com sucesso
                            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(url -> {
                                // Atualizar a foto do usuário
                                atualizarFotoUsuario(url);

                                Toast.makeText(EditarPerfilActivity.this,
                                        "Sucesso ao fazer upload da imagem",
                                        Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(e -> {
                                Toast.makeText(EditarPerfilActivity.this,
                                        "Erro ao recuperar URL da imagem",
                                        Toast.LENGTH_SHORT).show();
                            });
                        });
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void atualizarFotoUsuario(Uri url) {
        // Atualizar foto no perfil
        UsuarioFirebase.atualizarFotoUsuario(url);

        // Atualizar foto no Firebase
        usuarioLogado.setCaminhoFoto(url.toString());
        usuarioLogado.atualizar();

        Toast.makeText(EditarPerfilActivity.this,
                "Sua foto foi atualizada!",
                Toast.LENGTH_SHORT).show();
    }

    public void inicializarComponentes() {
        imagemEditarPerfil = findViewById(R.id.imagemEditarPerfil);
        textAlterarFoto = findViewById(R.id.textAlterarFoto);
        editNomePerfil = findViewById(R.id.editNomePerfil);
        editEmailPerfil = findViewById(R.id.editEmailPerfil);
        buttonSalvarAlteracoes = findViewById(R.id.buttonSalvarAlteracoes);
        editEmailPerfil.setFocusable(false);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
