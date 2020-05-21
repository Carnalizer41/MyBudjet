package com.carnalizer.mybudjet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.carnalizer.mybudjet.adapters.ExpenseAdapter;
import com.carnalizer.mybudjet.db.DB;
import com.carnalizer.mybudjet.entities.Expense;

import java.util.ArrayList;

public class ExpenseActivity extends AppCompatActivity {

    DB dbHelper;
    private ListView allTasks;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);
        dbHelper = new DB(this);
        allTasks = findViewById(R.id.expenseList);
        loadAllTasks();
    }

    private void loadAllTasks() {
        ArrayList<Expense> taskList = dbHelper.getExpense();
        if (adapter == null) {
            adapter = new ExpenseAdapter(this, R.layout.expense_row, taskList);
            allTasks.setAdapter(adapter);
        } else {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }

    public void onClickDeleteExpense(View view) {
        View parent = (View) view.getParent();
        TextView textDate = (TextView) parent.findViewById(R.id.expenseDate);
        TextView textAmount = (TextView) parent.findViewById(R.id.expenseAmount);
        TextView textCat = (TextView) parent.findViewById(R.id.expenseCategory);
        final String date = String.valueOf(textDate.getText());
        String s = String.valueOf(textAmount.getText());
        s = s.replaceAll("₴", "");
        final Float amount = Float.parseFloat(s);
        final String cat = String.valueOf(textCat.getText());

        final AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("Удалить расход?")
                .setMessage("Вы хотите удалить расход на " + cat + " от " + date + " на сумму " + amount + " ?")
                .setPositiveButton("Удалить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dbHelper.deleteExpenseData(date, amount, cat);
                        loadAllTasks();
                    }
                })
                .setNegativeButton("ГАЛЯ, ОТМЕНА!", null)
                .create();
        dialog.show();
    }

    public void onClickChoose(View view) {
        Intent intent = new Intent(ExpenseActivity.this, ChooseExpenseActivity.class);
        startActivity(intent);
    }

    public void onClickMainMenu(View view) {
        Intent intent = new Intent(ExpenseActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
