package com.carnalizer.mybudjet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.carnalizer.mybudjet.R;
import com.carnalizer.mybudjet.entities.Report;

import java.util.List;

public class ReportAdapter extends ArrayAdapter<Report> {

    private LayoutInflater inflater;
    private int layout;
    private List<Report> reports;

    public ReportAdapter(Context context, int resource, List<Report> reports) {
        super(context, resource, reports);
        this.reports = reports;
        this.layout = resource;
        this.inflater = LayoutInflater.from(context);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View view=inflater.inflate(this.layout, parent, false);

        TextView dateView = (TextView) view.findViewById(R.id.reportDate);
        TextView nameView = (TextView) view.findViewById(R.id.reportName);

        Report report = reports.get(position);

        dateView.setText(report.getDate());
        nameView.setText(String.valueOf(report.getName()));

        return view;
    }
}
