package com.viajet.itubus.activity.model;

public class Recarga {
    private String data;
    private String horario;
    private double valor;
    private double credito;
    private int quantidadeViagem;

    // Construtor vazio necessário para o Firebase
    public Recarga() {}

    public Recarga(String data, String horario, double valor, double credito, int quantidadeViagem) {
        this.data = data;
        this.horario = horario;
        this.valor = valor;
        this.credito = credito;
        this.quantidadeViagem = quantidadeViagem;
    }

    // Getters e Setters
    public String getData() { return data; }
    public void setData(String data) { this.data = data; }

    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }

    public double getValor() { return valor; }
    public void setValor(double valor) { this.valor = valor; }

    public double getCredito() { return credito; }
    public void setCredito(double credito) { this.credito = credito; }

    public int getQuantidadeViagem() { return quantidadeViagem; }
    public void setQuantidadeViagem(int quantidadeViagem) { this.quantidadeViagem = quantidadeViagem; }
}
