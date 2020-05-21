package com.carnalizer.mybudjet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.carnalizer.mybudjet.R;
import com.carnalizer.mybudjet.entities.Expense;

import java.util.List;

public class ExpenseAdapter extends ArrayAdapter<Expense> {

    private LayoutInflater inflater;
    private int layout;
    private List<Expense> expenses;

    public ExpenseAdapter(Context context, int resource, List<Expense> expenses) {
        super(context, resource, expenses);
        this.expenses = expenses;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView dateView = (TextView) view.findViewById(R.id.expenseDate);
        TextView nameView = (TextView) view.findViewById(R.id.expenseCategory);
        TextView moneyView = (TextView) view.findViewById(R.id.expenseAmount);

        Expense expense = expenses.get(position);

        dateView.setText(expense.getExpenseDate());
        nameView.setText(expense.getExpenseName());
        moneyView.setText(String.valueOf(expense.getAmount()) + " ₴");

        return view;
    }

}
