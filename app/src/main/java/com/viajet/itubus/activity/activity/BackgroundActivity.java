package com.viajet.itubus.activity.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class BackgroundActivity extends AppCompatActivity {

    private static final int SELECAO_GALERIA = 200;
    private ImageView imagemEditarPerfilFundo;
    private TextView textAlterarFoto;
    private Button buttonSalvarAlteracoes;
    private Usuario usuarioLogado;
    private StorageReference storageRef;
    private String identificadorUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background);

        // Configuração do EdgeToEdge
        configurarEdgeToEdge();

        // Configurações iniciais
        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado();
        storageRef = ConfiguracaoFirebase.getFirebaseStorage();
        identificadorUsuario = UsuarioFirebase.getIdentificadorUsuario();

        // Configura toolbar
        configurarToolbar();

        // Inicializar componentes
        inicializarComponentes();

        // Recuperar dados do usuário e configurar a imagem de fundo
        configurarImagemPerfilFundo();

        // Configurar evento de clique para alterar a foto de fundo
        configurarCliqueAlterarFoto();
    }

    private void configurarEdgeToEdge() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void configurarToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Editar Fundo de Perfil");
        setSupportActionBar(toolbar);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_esquerda);
        }
    }

    private void inicializarComponentes() {
        imagemEditarPerfilFundo = findViewById(R.id.imagemEditarPerfilFundo);
        textAlterarFoto = findViewById(R.id.textAlterarFoto);
        buttonSalvarAlteracoes = findViewById(R.id.buttonSalvarAlteracoes);
    }

    private void configurarImagemPerfilFundo() {
        String caminhoFotoFundo = usuarioLogado.getCaminhoFotoFundo(); // Novo campo para foto de fundo

        if (caminhoFotoFundo != null && !caminhoFotoFundo.isEmpty()) {
            Glide.with(BackgroundActivity.this)
                    .load(caminhoFotoFundo)
                    .into(imagemEditarPerfilFundo);
        } else {
            imagemEditarPerfilFundo.setImageResource(R.drawable.logo);
        }

        Toast.makeText(BackgroundActivity.this, "Dados carregados com sucesso!", Toast.LENGTH_SHORT).show();
    }

    private void configurarCliqueAlterarFoto() {
        textAlterarFoto.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivityForResult(intent, SELECAO_GALERIA);
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
                        imagemEditarPerfilFundo.setImageBitmap(imagem);

                        // Recuperar dados da imagem para o Firebase
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        imagem.compress(Bitmap.CompressFormat.JPEG, 70, baos);
                        byte[] dadosImagem = baos.toByteArray();

                        // Salvar imagem de fundo no Firebase
                        atualizarFotoUsuarioFundo(dadosImagem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void atualizarFotoUsuarioFundo(byte[] dadosImagem) {
        StorageReference imagemFundoRef = storageRef
                .child("imagens")
                .child("perfil_fundo")
                .child(identificadorUsuario + "_fundo.jpeg");

        UploadTask uploadTask = imagemFundoRef.putBytes(dadosImagem);
        uploadTask.addOnFailureListener(e -> {
            Toast.makeText(BackgroundActivity.this,
                    "Erro ao fazer upload da imagem de fundo",
                    Toast.LENGTH_SHORT).show();
        }).addOnSuccessListener(taskSnapshot -> {
            taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(url -> {
                atualizarFotoFundoUsuario(url);
                Toast.makeText(BackgroundActivity.this,
                        "Sucesso ao fazer upload da imagem de fundo",
                        Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(BackgroundActivity.this,
                        "Erro ao recuperar URL da imagem de fundo",
                        Toast.LENGTH_SHORT).show();
            });
        });
    }

    private void atualizarFotoFundoUsuario(Uri url) {
        // Atualizar a foto de fundo no modelo de usuário
        usuarioLogado.setCaminhoFotoFundo(url.toString());
        usuarioLogado.atualizar();

        Toast.makeText(BackgroundActivity.this,
                "Sua foto de fundo foi atualizada!",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return false;
    }
}
