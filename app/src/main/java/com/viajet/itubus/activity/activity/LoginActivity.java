package com.viajet.itubus.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.helper.ConfiguracaoFirebase;
import com.viajet.itubus.activity.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha, campoConfirmarSenha, campoNumeroCartao, campoTelefone;
    private Button botaoContinuar;
    private ProgressBar progressoLogin;

    private Usuario usuario;
    private FirebaseAuth autenticacao;
    private String NumeroCartao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configuração da transparência e ajuste para o Edge-to-Edge
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarComponentes();
        verificarUsuarioLogado();

        // Configuração do botão de login
        progressoLogin.setVisibility(View.GONE);
        botaoContinuar.setOnClickListener(view -> {
            String textoEmail = campoEmail.getText().toString();
            String textoSenha = campoSenha.getText().toString();
            if (!textoEmail.isEmpty()) {
                if (!textoSenha.isEmpty()) {
                    usuario = new Usuario();
                    usuario.setEmail(textoEmail);
                    usuario.setSenha(textoSenha);
                    usuario.setConfirmarSenha(textoSenha);
                    usuario.setNome(textoEmail);
                    usuario.setNumeroCartao(NumeroCartao);
                    validarLogin(usuario);
                } else {
                    Toast.makeText(LoginActivity.this, "Preencha a senha!", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(LoginActivity.this, "Preencha o email!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void verificarUsuarioLogado() {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        if (autenticacao.getCurrentUser() != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            finish();
        }
    }

    public void validarLogin(Usuario usuario) {
        progressoLogin.setVisibility(View.VISIBLE);
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();

        autenticacao.signInWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressoLogin.setVisibility(View.GONE);
                            startActivity(new Intent(getApplicationContext(), MainActivity.class));
                            finish();
                        } else {
                            Toast.makeText(LoginActivity.this, "Erro ao fazer login", Toast.LENGTH_SHORT).show();
                            progressoLogin.setVisibility(View.GONE);
                        }
                    }
                });
    }

    public void abrirCadastro(View view) {
        Intent intent = new Intent(LoginActivity.this, CadastroActivity.class);
        startActivity(intent);
    }

    public void inicializarComponentes() {
        campoNome = findViewById(R.id.editLoginNome); // Adicionado para campo de nome
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
        campoConfirmarSenha = findViewById(R.id.editCadastroSenhaConfirmar); // Adicionado para campo de confirmação de senha
        campoNumeroCartao = findViewById(R.id.editCadastroNumeroCartão); // Adicionado para campo de número de cartão
        campoTelefone = findViewById(R.id.editCadastroTelefone); // Adicionado para campo de telefone
        progressoLogin = findViewById(R.id.progresso_login);
        botaoContinuar = findViewById(R.id.botao_continuar);

        campoEmail.requestFocus();
    }
}
