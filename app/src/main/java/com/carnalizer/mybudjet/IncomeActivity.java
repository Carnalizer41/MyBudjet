package com.carnalizer.mybudjet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carnalizer.mybudjet.adapters.IncomeAdapter;
import com.carnalizer.mybudjet.db.DB;
import com.carnalizer.mybudjet.entities.Income;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class IncomeActivity extends AppCompatActivity {

    DB dbHelper;
    private Button addIncomeBtn;
    private ListView allIncomes;
    private IncomeAdapter adapter;
    private Calendar dateAndTime1=new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        dbHelper = new DB(this);
        addIncomeBtn = findViewById(R.id.addIncomeBth);
        allIncomes = findViewById(R.id.incomeList);
        loadAllTasks();
    }

    private void loadAllTasks()
    {
        ArrayList<Income> taskList = dbHelper.getIncome();
        if(adapter==null)
        {
            adapter = new IncomeAdapter(this,R.layout.income_row, taskList);
            allIncomes.setAdapter(adapter);
        }
        else
        {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickAddIncome(View view)
    {
        final EditText userGetMoney = new EditText(this);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Новый доход!")
                .setMessage("Введите сумму и дату")
                .setNeutralButton("Дата",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                })
                .setView(userGetMoney)
                .setPositiveButton("Добавить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setNegativeButton("ОТМЕНА",null)
                .create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean wantToCloseDialog = false;
                if(String.valueOf(userGetMoney.getText()).isEmpty())
                {
                    Toast.makeText(IncomeActivity.this, "Введите сумму!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!isNumeric(String.valueOf(userGetMoney.getText())))
                    {
                        Toast.makeText(IncomeActivity.this, "Вы ввели неверное значение суммы", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        wantToCloseDialog = true;
                    }
                }
                if(wantToCloseDialog)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    Income income = new Income(Float.parseFloat(String.valueOf(userGetMoney.getText())),
                            sdf.format(dateAndTime1.getTime()));
                    dbHelper.insertIncomeData(income.getIncomeDate(),income.getIncomeAmount());
                    loadAllTasks();
                    dialog.dismiss();
                }
            }
        });

        dialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Boolean wantToCloseDialog = false;
                //Do stuff, possibly set wantToCloseDialog to true then...
                new DatePickerDialog(IncomeActivity.this, d1,
                        dateAndTime1.get(Calendar.YEAR),
                        dateAndTime1.get(Calendar.MONTH),
                        dateAndTime1.get(Calendar.DAY_OF_MONTH))
                        .show();
                if(wantToCloseDialog)
                    dialog.dismiss();
                //else dialog stays open. Make sure you have an obvious way to close the dialog especially if you set cancellable to false.
            }
        });
    }

    // установка обработчика выбора даты
    DatePickerDialog.OnDateSetListener d1=new DatePickerDialog.OnDateSetListener() {
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            dateAndTime1.set(Calendar.YEAR, year);
            dateAndTime1.set(Calendar.MONTH, monthOfYear);
            dateAndTime1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        }
    };

    public static boolean isNumeric(String str)
    {
        try
        {
            float f = Float.parseFloat(str);
        }
        catch(NumberFormatException nfe)
        {
            return false;
        }
        return true;
    }

    public void onClickDeleteIncome(final View view)
    {

        final AlertDialog dialog = new AlertDialog.Builder(IncomeActivity.this)
                .setTitle("Удалить доход?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        View parent = (View) view.getParent();
                        TextView textDate = (TextView) parent.findViewById(R.id.incomeDate);
                        String date = String.valueOf(textDate.getText());
                        TextView textAmount = (TextView) parent.findViewById(R.id.incomeAmount);
                        String s = String.valueOf(textAmount.getText());
                        s = s.replaceAll("₴","");
                        Float amount = Float.parseFloat(s);
                        dbHelper.deleteIncomeData(date, amount);
                        loadAllTasks();
                    }
                })
                .setNegativeButton("ГАЛЯ, ОТМЕНА!",null)
                .create();
        dialog.show();
    }


    public void onClickMainMenu(View view)
    {
        Intent intent = new Intent(IncomeActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
