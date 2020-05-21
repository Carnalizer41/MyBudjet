package com.carnalizer.mybudjet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carnalizer.mybudjet.adapters.ReportAdapter;
import com.carnalizer.mybudjet.db.DB;
import com.carnalizer.mybudjet.entities.Report;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class ReportActivity extends AppCompatActivity {

    DB dbHelper;
    private Button addReportBtn;
    private ListView allReports;
    private ReportAdapter adapter;
    private Calendar dateAndTime1=new GregorianCalendar();
    private Calendar dateAndTime2=new GregorianCalendar();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        dbHelper = new DB(this);
        addReportBtn = findViewById(R.id.addReportBth);
        allReports = findViewById(R.id.reportList);

        loadAllTasks();
    }

    private void loadAllTasks()
    {
        ArrayList<Report> taskList = dbHelper.getReport();
        if(adapter==null)
        {
            adapter = new ReportAdapter(this,R.layout.report_row, taskList);
            allReports.setAdapter(adapter);
        }
        else
        {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickReportInfo(View view){
        View parent = (View) view.getParent();
        TextView textDate = (TextView) parent.findViewById(R.id.reportDate);
        final String date = String.valueOf(textDate.getText());
        TextView textName = (TextView) parent.findViewById(R.id.reportName);
        final String name = String.valueOf(textName.getText());
        final Report report = dbHelper.getOneReport(date, name);

        final AlertDialog dialog = new AlertDialog.Builder(ReportActivity.this)
                .setTitle("Отчет '"+name+"' от "+date)
                .setMessage(report.getText())
                .setPositiveButton("ОК", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNeutralButton("Скачать",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        generateNoteOnSD(ReportActivity.this, name+"_"+date, report.getText());

                    }
                })
                .create();
        dialog.show();
    }

    private void generateNoteOnSD(Context context, String sFileName, String sBody) {
        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Reports");
            if (!root.exists()) {
                root.mkdirs();
            }
            File gpxfile = new File(root, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
            System.out.println(gpxfile.getAbsolutePath());
            Toast.makeText(context, "Saved", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("AAAAAAAAAAAAAAA");
        }
    }

    public void onClickDeleteReport(final View view)
    {

        final AlertDialog dialog = new AlertDialog.Builder(ReportActivity.this)
                .setTitle("Удалить отчет?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View parent = (View) view.getParent();
                        TextView textDate = (TextView) parent.findViewById(R.id.reportDate);
                        String date = String.valueOf(textDate.getText());
                        TextView textName = (TextView) parent.findViewById(R.id.reportName);
                        String name = String.valueOf(textName.getText());
                        dbHelper.deleteReportData(date, name);
                        loadAllTasks();
                    }
                })
                .setNegativeButton("ГАЛЯ, ОТМЕНА!",null)
                .create();
        dialog.show();
    }

    public void onClickAddReport(View view)
    {
        Intent intent = new Intent(ReportActivity.this, AddReportActivity.class);
        startActivity(intent);
    }

    public void onClickMainMenu(View view)
    {
        Intent intent = new Intent(ReportActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
