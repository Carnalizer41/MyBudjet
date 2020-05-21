package com.carnalizer.mybudjet.entities;

import java.util.Date;

public class Expense {

    private Float amount;
    private String expenseName;
    private String date;

    public Expense(Float amount, String expenseName, String date) {
        this.amount = amount;
        this.expenseName = expenseName;
        this.date = date;
    }

    public Float getAmount() {
        return amount;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public String getExpenseDate() {
        return date;
    }


}
