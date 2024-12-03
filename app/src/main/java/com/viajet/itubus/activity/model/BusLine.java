package com.viajet.itubus.activity.model;

public class BusLine {

    private String title;
    private String from;
    private String to;
    private String price;

    public BusLine() {
        // Construtor vazio necessário para Firebase
    }

    public BusLine(String title, String from, String to, String price) {
        this.title = title;
        this.from = from;
        this.to = to;
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getPrice() {
        return price;
    }
}
