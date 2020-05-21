package com.carnalizer.mybudjet.entities;

public class Report {

    private String date;
    private String name;
    private String text;

    public Report(String date, String name, String text) {
        this.date = date;
        this.name = name;
        this.text = text;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getText() {
        return text;
    }
}
