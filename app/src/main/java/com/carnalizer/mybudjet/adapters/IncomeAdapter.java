package com.carnalizer.mybudjet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.carnalizer.mybudjet.R;
import com.carnalizer.mybudjet.entities.Income;

import java.util.List;

public class IncomeAdapter extends ArrayAdapter<Income> {

    private LayoutInflater inflater;
    private int layout;
    private List<Income> incomes;

    public IncomeAdapter(Context context, int resource, List<Income> incomes) {
        super(context, resource, incomes);
        this.incomes = incomes;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView dateView = (TextView) view.findViewById(R.id.incomeDate);
        TextView moneyView = (TextView) view.findViewById(R.id.incomeAmount);

        Income income = incomes.get(position);

        dateView.setText(income.getIncomeDate());
        moneyView.setText(String.valueOf(income.getIncomeAmount())+" ₴");

        return view;
    }

}
