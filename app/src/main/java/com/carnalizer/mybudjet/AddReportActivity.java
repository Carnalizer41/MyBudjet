package com.carnalizer.mybudjet;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.carnalizer.mybudjet.db.DB;
import com.carnalizer.mybudjet.entities.Report;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class AddReportActivity extends AppCompatActivity {

    DB dbHelper;
    private Calendar dateAndTime1=new GregorianCalendar();
    private Calendar dateAndTime2=new GregorianCalendar();
    private EditText reportName;
    private ImageButton calendar1;
    private ImageButton calendar2;
    private TextView date1;
    private TextView date2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_report);
        dbHelper = new DB(this);
        reportName = findViewById(R.id.reportName);
        calendar1 = findViewById(R.id.calendar1);
        calendar2 = findViewById(R.id.calendar2);
        date1 = findViewById(R.id.dateAndTime1);
        date2 = findViewById(R.id.dateAndTime2);
        setInitialStartDate();
    }

    private void setInitialStartDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

        if(dateAndTime1.get(Calendar.MONTH)!=0)
            dateAndTime1.set(dateAndTime1.get(Calendar.YEAR),dateAndTime1.get(Calendar.MONTH)-1,dateAndTime1.get(Calendar.DATE));
        else
            dateAndTime1.set(dateAndTime1.get(Calendar.YEAR)-1,dateAndTime1.get(Calendar.MONTH)-1,dateAndTime1.get(Calendar.DATE));

        date1.setText(sdf.format(dateAndTime1.getTime()));
        date2.setText(sdf.format(dateAndTime2.getTime()));
        dbHelper.insertReportDates(sdf.format(dateAndTime1.getTime()),sdf.format(dateAndTime2.getTime()));

    }

    public void onClickCreate(View view){

        Calendar dateToSafe = new GregorianCalendar();
        if(String.valueOf(reportName.getText()).isEmpty())
        {
            Toast.makeText(AddReportActivity.this, "Введите название!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            Report report = new Report(sdf.format(dateToSafe.getTime()),
                    String.valueOf(reportName.getText()),
                    getNewReportText(String.valueOf(reportName.getText())));
            dbHelper.insertReport(report.getDate(),report.getName(),report.getText());
            Intent intent = new Intent(AddReportActivity.this, ReportActivity.class);
            startActivity(intent);
        }

    }

    private String getNewReportText(String name){
        String reportText = name+"\n";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String date1 = sdf.format(dateAndTime1.getTime());
        String date2 = sdf.format(dateAndTime2.getTime());

        reportText+="Отчет за "+date1+" - "+date2+"\n\n";

        float totalIncome = dbHelper.getDateIncomeSum();
        float totalExpense = dbHelper.getDateExpenseSum();
        float regular = dbHelper.getDateCategorySum("regular", date1, date2);
        float gifts = dbHelper.getDateCategorySum("gifts", date1, date2);
        float entertainment = dbHelper.getDateCategorySum("entertainment", date1, date2);
        float self = dbHelper.getDateCategorySum("self", date1, date2);
        float big = dbHelper.getDateCategorySum("big", date1, date2);
        float safement = dbHelper.getDateCategorySum("safement", date1, date2);
        reportText+="Общий доход: "+totalIncome+"\n";
        reportText+="Общий расход: "+totalExpense+"\n\n";
        reportText+="Прогресс по категориям:\n" +
                "-Регулрные расходы: "+regular+"₴ / "+totalIncome*0.6+"₴\n" +
                "-Образование: "+self+"₴ / "+totalIncome*0.1+"₴\n" +
                "-Развлечения: "+entertainment+"₴ / "+totalIncome*0.1+"₴\n" +
                "-Большие покупки: "+big+"₴ / "+totalIncome*0.05+"₴\n" +
                "-Подарки и благотворительность: "+gifts+"₴ / "+totalIncome*0.05+"₴\n" +
                "-Накопления: "+safement+"₴ / "+totalIncome*0.1+"₴\n\n";

        List<String> lessCat = new ArrayList<>();
        List<String> overCat = new ArrayList<>();

        if(regular<totalIncome*0.6/2)
            lessCat.add("Регулярные расходы");
        else if(regular>totalIncome*0.6)
            overCat.add("Регулярные расходы");

        if(self<totalIncome*0.1/2)
            lessCat.add("Образование");
        else if(self>totalIncome*0.1)
            overCat.add("Образование");

        if(entertainment<totalIncome*0.1/2)
            lessCat.add("Развлечения");
        else if(entertainment>totalIncome*0.1)
            overCat.add("Развлечения");

        if(big<totalIncome*0.05/2)
            lessCat.add("Большие покупки");
        else if(big>totalIncome*0.05)
            overCat.add("Большие покупки");

        if(gifts<totalIncome*0.05/2)
            lessCat.add("Подарки и благотворительность");
        else if(gifts>totalIncome*0.05)
            overCat.add("Подарки и благотворительность");

        if(safement<totalIncome*0.1/2)
            lessCat.add("Накопления");
        else if(safement>totalIncome*0.1)
            overCat.add("Накопления");

        if(!lessCat.isEmpty()) {
            reportText += "Вы слишком мало уделяете внимания следующим категориям:\n";
            for (String s : lessCat) {
                reportText+="-"+s+"\n";
            }
            reportText+="\n";
        }
        if(!overCat.isEmpty()) {
            reportText += "Есть перерасход в категориях:\n";
            for (String s : overCat) {
                reportText+="-"+s+"\n";
            }
            reportText+="Пересмотрите свои растраты.\n";
        }
        if(lessCat.isEmpty()&&overCat.isEmpty()){
            reportText+="Продолжайте в том же духе! :)";
        }

        return reportText;
    }

    public void onClickCalendar1(View view){

        new DatePickerDialog(AddReportActivity.this, d1,
                dateAndTime1.get(Calendar.YEAR),
                dateAndTime1.get(Calendar.MONTH),
                dateAndTime1.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    public void onClickCalendar2(View view){

        new DatePickerDialog(AddReportActivity.this, d2,
                dateAndTime2.get(Calendar.YEAR),
                dateAndTime2.get(Calendar.MONTH),
                dateAndTime2.get(Calendar.DAY_OF_MONTH))
                .show();

    }

    DatePickerDialog.OnDateSetListener d1=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            dateAndTime1.set(Calendar.YEAR, year);
            dateAndTime1.set(Calendar.MONTH, monthOfYear);
            dateAndTime1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            sortDates();
            date1.setText(sdf.format(dateAndTime1.getTime()));
            dbHelper.insertReportDates(sdf.format(dateAndTime1.getTime()),sdf.format(dateAndTime2.getTime()));
        }
    };

    DatePickerDialog.OnDateSetListener d2=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
            Calendar cal = new GregorianCalendar();
            cal.set(Calendar.YEAR, year);
            cal.set(Calendar.MONTH, monthOfYear);
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            dateAndTime2.set(Calendar.YEAR, year);
            dateAndTime2.set(Calendar.MONTH, monthOfYear);
            dateAndTime2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            sortDates();
            date2.setText(sdf.format(dateAndTime2.getTime()));
            dbHelper.insertReportDates(sdf.format(dateAndTime1.getTime()),sdf.format(dateAndTime2.getTime()));

        }
    };

    private void sortDates(){
        if(dateAndTime1.getTimeInMillis()>dateAndTime2.getTimeInMillis()){
            Calendar temp = dateAndTime1;
            dateAndTime1 = dateAndTime2;
            dateAndTime2 = temp;
        }
    }

    public void onClickMainMenu(View view)
    {
        Intent intent = new Intent(AddReportActivity.this, ReportActivity.class);
        startActivity(intent);
    }
}
