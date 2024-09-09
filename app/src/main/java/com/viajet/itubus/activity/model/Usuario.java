package com.viajet.itubus.activity.model;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.viajet.itubus.activity.helper.ConfiguracaoFirebase;

import java.util.HashMap;
import java.util.Map;


public class Usuario {

    private String id;
    private String nome;
    private String email;
    private String senha;
    private String caminhoFoto;
    private String confirmarSenha;
    private String telefone;
    private String numeroCartao;
    private String tipoCartao;

    // Método para salvar o usuário no Firebase
    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getId());
        usuariosRef.setValue(this);
    }

     // Método para atualizar o usuário no Firebase
    public void atualizar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getId());
        Map<String, Object> valoresUsuario = converterParaMap();
        usuariosRef.updateChildren(valoresUsuario);
    }

 // Método para converter os dados do usuário para um mapa
    public Map<String, Object> converterParaMap() {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("email", getEmail());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("telefone", getTelefone());
        usuarioMap.put("numeroCartao", getNumeroCartao());
        usuarioMap.put("tipoCartao", getTipoCartao());
        usuarioMap.put("id", getId());
        usuarioMap.put("caminhoFoto", getCaminhoFoto());
        // Campos que não serão enviados ao Firebase, pois estão marcados com @Exclude
        usuarioMap.put("senha", getSenha());
        usuarioMap.put("confirmarSenha", getConfirmarSenha());
        return usuarioMap;
    }
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    @Exclude // Excluir este campo da serialização para o Firebase
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Exclude // Excluir este campo da serialização para o Firebase
    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getNumeroCartao() {
        return numeroCartao;
    }

    public void setNumeroCartao(String numeroCartao) {
        this.numeroCartao = numeroCartao;
    }

    public String getTipoCartao() {
        return tipoCartao;
    }

    public void setTipoCartao(String tipoCartao) {
        this.tipoCartao = tipoCartao;
    }
}



