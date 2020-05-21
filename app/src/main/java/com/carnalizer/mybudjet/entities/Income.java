package com.carnalizer.mybudjet.entities;

import java.util.Date;

public class Income {

    private float incomeAmount;
    private String date;

    public Income(float incomeAmount, String date) {
        this.incomeAmount = incomeAmount;
        this.date = date;
    }

    public float getIncomeAmount() {
        return incomeAmount;
    }

    public String getIncomeDate() {
        return date;
    }

}
