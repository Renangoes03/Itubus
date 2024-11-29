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
    private String caminhoFotoFundo;
    private String confirmarSenha;
    private String telefone;
    private String numeroCartao;
    private String tipoCartao;
    private String credito;

    // Método para salvar o usuário no Firebase
    public void salvar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getId()); // Certifique-se de salvar o usuário com base no seu ID
        usuariosRef.setValue(this);
    }

    // Método para atualizar o usuário no Firebase
    public void atualizar() {
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getId()); // Atualiza com base no ID do usuário

        Map<String, Object> valoresUsuario = converterParaMap();
        usuariosRef.updateChildren(valoresUsuario);
    }

    // Método para converter os dados do usuário para um mapa
    public Map<String, Object> converterParaMap() {
        HashMap<String, Object> usuarioMap = new HashMap<>();
        usuarioMap.put("id", getId());
        usuarioMap.put("nome", getNome());
        usuarioMap.put("email", getEmail());
        usuarioMap.put("telefone", getTelefone());
        usuarioMap.put("numeroCartao", getNumeroCartao());
        usuarioMap.put("tipoCartao", getTipoCartao());
        usuarioMap.put("caminhoFoto", getCaminhoFoto());
        usuarioMap.put("caminhoFotoFundo", getCaminhoFotoFundo());
        usuarioMap.put("credito", getCredito());
        return usuarioMap;
    }

    // Getters e Setters
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public String getCaminhoFoto() {
        return caminhoFoto;
    }

    public void setCaminhoFoto(String caminhoFoto) {
        this.caminhoFoto = caminhoFoto;
    }

    public String getCaminhoFotoFundo() {
        return caminhoFotoFundo;
    }

    public void setCaminhoFotoFundo(String caminhoFotoFundo) {
        this.caminhoFotoFundo = caminhoFotoFundo;
    }

    public String getCredito() {
        return credito;
    }

    public void setCredito(String credito) {
        this.credito = credito;
    }

    // Exclui senha e confirmação de senha ao enviar para o Firebase
    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    @Exclude
    public String getConfirmarSenha() {
        return confirmarSenha;
    }

    public void setConfirmarSenha(String confirmarSenha) {
        this.confirmarSenha = confirmarSenha;
    }
}

