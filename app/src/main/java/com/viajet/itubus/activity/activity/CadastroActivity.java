package com.viajet.itubus.activity.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.viajet.itubus.R;
import com.viajet.itubus.activity.helper.ConfiguracaoFirebase;
import com.viajet.itubus.activity.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText campoNome, campoEmail, campoSenha, campoConfirmarSenha, campoNumeroCartao, campoTelefone;
    private Button botaoCadastrar;
    private ProgressBar progressoCadastro;

    private Usuario usuario;
    private FirebaseAuth autenticacao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();

        // Configura a Toolbar
        Toolbar toolbar = findViewById(R.id.toolbarPrincipal);
        toolbar.setTitle("Criar Conta");
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_flecha_esquerda);
        }

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Configura o botão de cadastro
        progressoCadastro.setVisibility(View.GONE);
        botaoCadastrar.setOnClickListener(view -> {
            progressoCadastro.setVisibility(View.VISIBLE);
            String textoNome = campoNome.getText().toString();
            String textoEmail = campoEmail.getText().toString();
            String textoSenha = campoSenha.getText().toString();
            String textoConfirmarSenha = campoConfirmarSenha.getText().toString();
            String textoNumeroCartao = campoNumeroCartao.getText().toString().replaceAll("\\D", ""); // Remove caracteres não numéricos
            String textoTelefone = campoTelefone.getText().toString().replaceAll("\\D", ""); // Remove caracteres não numéricos

            // Verificações e validações
            if (!textoNome.isEmpty()) {
                if (!textoEmail.isEmpty()) {
                    if (!textoSenha.isEmpty()) {
                        if (textoSenha.equals(textoConfirmarSenha)) {
                            if (!textoNumeroCartao.isEmpty() && !textoTelefone.isEmpty()) {
                                if (validarNumeroCartao(textoNumeroCartao)) {
                                    if (validarTelefone(textoTelefone)) {
                                        usuario = new Usuario();
                                        usuario.setNome(textoNome);
                                        usuario.setEmail(textoEmail);
                                        usuario.setSenha(textoSenha);
                                        usuario.setNumeroCartao(textoNumeroCartao);
                                        usuario.setTelefone(textoTelefone);
                                        cadastrar(usuario);
                                    } else {
                                        mostrarMensagem("Telefone inválido!");
                                    }
                                } else {
                                    mostrarMensagem("Número do cartão inválido!");
                                }
                            } else {
                                mostrarMensagem("Preencha todos os campos obrigatórios!");
                            }
                        } else {
                            mostrarMensagem("As senhas não coincidem!");
                        }
                    } else {
                        mostrarMensagem("Preencha a senha!");
                    }
                } else {
                    mostrarMensagem("Preencha o e-mail!");
                }
            } else {
                mostrarMensagem("Preencha o nome!");
            }
        });
    }

    private void mostrarMensagem(String mensagem) {
        Toast.makeText(CadastroActivity.this, mensagem, Toast.LENGTH_SHORT).show();
        progressoCadastro.setVisibility(View.GONE);
    }

    /**
     * Valida o número do cartão (exemplo de validação simples).
     */
    private boolean validarNumeroCartao(String numeroCartao) {
        // Exemplo de validação simples (deve ser ajustado conforme o formato esperado)
        return numeroCartao.matches("\\d{13}"); // Exemplo de número de cartão com 16 dígitos
    }

    /**
     * Valida o formato do telefone (exemplo de validação simples).
     */
    private boolean validarTelefone(String telefone) {
        // Exemplo de validação simples (deve ser ajustado conforme o formato esperado)
        return telefone.matches("\\d{10,11}"); // Exemplo de telefone com 10 ou 11 dígitos
    }

    /**
     * Método responsável por cadastrar usuário com e-mail e senha
     * e fazer validações ao fazer o cadastro
     */
    private void cadastrar(Usuario usuario) {
        autenticacao = ConfiguracaoFirebase.getFirebaseAutenticacao();
        autenticacao.createUserWithEmailAndPassword(usuario.getEmail(), usuario.getSenha())
            .addOnCompleteListener(this, task -> {
                if (task.isSuccessful()) {
                    try {
                        progressoCadastro.setVisibility(View.GONE);

                        // Salvar dados no Firebase Realtime Database
                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId(idUsuario);
                        usuario.salvar();

                        Toast.makeText(CadastroActivity.this, "Cadastrado com sucesso!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte!";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Por favor, digite um e-mail válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esta conta já foi cadastrada";
                    } catch (Exception e) {
                        erroExcecao = "Erro ao cadastrar usuário: " + e.getMessage();
                        e.printStackTrace();
                    }
                    mostrarMensagem("Erro: " + erroExcecao);
                }
            });
    }

    private void inicializarComponentes() {
        campoNome = findViewById(R.id.editLoginNome);
        campoEmail = findViewById(R.id.editLoginEmail);
        campoSenha = findViewById(R.id.editLoginSenha);
        campoConfirmarSenha = findViewById(R.id.editCadastroSenhaConfirmar);
        progressoCadastro = findViewById(R.id.progresso_cadastro);
        botaoCadastrar = findViewById(R.id.botao_cadastrar);
        campoNumeroCartao = findViewById(R.id.editCadastroNumeroCartão);
        campoTelefone = findViewById(R.id.editCadastroTelefone);
        campoNome.requestFocus();

        // Aplicar máscaras
        campoNumeroCartao.addTextChangedListener(new MaskWatcher("##.##.########-#")); // Máscara para número do cartão (13 dígitos)
        campoTelefone.addTextChangedListener(new MaskWatcher("(##) #####-####")); // Máscara para telefone
    }
}
