package com.carnalizer.mybudjet.entities;

import java.util.Date;

public class Income {

    private float incomeAmount;
    private Date date;

    public Income(float incomeAmount, Date date) {
        this.incomeAmount = incomeAmount;
        this.date = date;
    }

    public float getIncomeAmount() {
        return incomeAmount;
    }

    public Date getDate() {
        return date;
    }

}
