package com.carnalizer.mybudjet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.carnalizer.mybudjet.adapters.ExpenseCategoryAdapter;
import com.carnalizer.mybudjet.db.DB;
import com.carnalizer.mybudjet.db.ExpenseCategoryData;
import com.carnalizer.mybudjet.entities.Expense;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;


public class ChooseExpenseActivity extends AppCompatActivity {

    DB dbHelper;
    private ListView allTasks;
    private ExpenseCategoryAdapter adapter;
    private Calendar dateAndTime1 = new GregorianCalendar();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_expense);
        dbHelper = new DB(this);
        allTasks = findViewById(R.id.categoriesList);
        loadAllTasks();
    }

    private void loadAllTasks() {
        ArrayList<String> taskList = ExpenseCategoryData.loadData();
        if (adapter == null) {
            adapter = new ExpenseCategoryAdapter(this, R.layout.expense_category_row,
                    taskList);
            allTasks.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickAddExpense(final View view)
    {

        final EditText userGetMoney = new EditText(this);
        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Новая транзакция")
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
                    Toast.makeText(ChooseExpenseActivity.this, "Введите сумму!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    if(!isNumeric(String.valueOf(userGetMoney.getText())))
                    {
                        Toast.makeText(ChooseExpenseActivity.this, "Вы ввели неверное значение суммы", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        wantToCloseDialog = true;
                    }
                }
                if(wantToCloseDialog)
                {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
                    View parent = (View) view.getParent();
                    TextView textcat = (TextView) parent.findViewById(R.id.category);
                    String s = String.valueOf(textcat.getText());
                    Expense expense = new Expense(Float.parseFloat(String.valueOf(userGetMoney.getText())),
                            s, sdf.format(dateAndTime1.getTime()));
                    dbHelper.insertExpenseData(expense.getExpenseDate(),expense.getAmount(),expense.getExpenseName());
                    Intent intent = new Intent(ChooseExpenseActivity.this, ExpenseActivity.class);
                    startActivity(intent);
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
                new DatePickerDialog(ChooseExpenseActivity.this, d1,
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

}
