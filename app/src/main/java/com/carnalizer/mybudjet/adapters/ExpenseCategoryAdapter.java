package com.carnalizer.mybudjet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.carnalizer.mybudjet.R;

import java.util.List;

public class ExpenseCategoryAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private int layout;
    private List<String> categories;

    public ExpenseCategoryAdapter(Context context, int resource, List<String> categories) {
        super(context, resource, categories);
        this.categories = categories;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(this.layout, parent, false);

        TextView categoryView = (TextView) view.findViewById(R.id.category);

        String category = categories.get(position);

        categoryView.setText(category);

        return view;
    }

}
