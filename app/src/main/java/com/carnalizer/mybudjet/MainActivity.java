package com.carnalizer.mybudjet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.carnalizer.mybudjet.entities.Expense;

public class MainActivity extends AppCompatActivity {

    private TextView totalIncome;
    private TextView totalExpense;
    private ProgressBar regularBar;
    private ProgressBar educationBar;
    private ProgressBar entertainmentBar;
    private ProgressBar bigBar;
    private ProgressBar giftBar;
    private ProgressBar safeBar;
    private Button incomeBtn;
    private Button expenseBtn;
    private Button reportBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        totalIncome = findViewById(R.id.mainMenuTotalIncome);
        totalExpense = findViewById(R.id.mainMenuTotalExpense);
        regularBar = findViewById(R.id.regularBar);
        educationBar = findViewById(R.id.educationBar);
        entertainmentBar = findViewById(R.id.entertainmentBar);
        bigBar = findViewById(R.id.bigBar);
        giftBar = findViewById(R.id.giftBar);
        safeBar = findViewById(R.id.safeBar);
        incomeBtn = findViewById(R.id.incomeBtn);
        expenseBtn = findViewById(R.id.expenseBtn);
        reportBtn = findViewById(R.id.reportBtn);
    }

    public void onClickIncome(View view){
        Intent intent = new Intent(MainActivity.this, IncomeActivity.class);
        startActivity(intent);
    }

    public void onClickExpense(View view){
        Intent intent = new Intent(MainActivity.this, ExpenseActivity.class);
        startActivity(intent);
    }

    public void onClickReport(View view){
        Intent intent = new Intent(MainActivity.this, ReportActivity.class);
        startActivity(intent);
    }

}
