package com.carnalizer.mybudjet.entities;

import java.util.Date;

public class Expense {

    private Category category;
    private String expenseName;
    private Date date;

    public Expense(Category category, String expenseName, Date date) {
        this.category = category;
        this.expenseName = expenseName;
        this.date = date;
    }

    public Category getCategory() {
        return category;
    }

    public String getExpenseName() {
        return expenseName;
    }

    public Date getDate() {
        return date;
    }


}
