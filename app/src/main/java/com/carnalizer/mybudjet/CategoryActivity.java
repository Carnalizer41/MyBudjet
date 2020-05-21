package com.carnalizer.mybudjet;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.carnalizer.mybudjet.adapters.ExpenseAdapter;
import com.carnalizer.mybudjet.db.DB;
import com.carnalizer.mybudjet.entities.Expense;

import java.util.ArrayList;

public class CategoryActivity extends AppCompatActivity {

    DB dbHelper;
    private ListView allTasks;
    private ExpenseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
        dbHelper = new DB(this);
        allTasks = findViewById(R.id.catList);

        loadAllTasks();
    }

    private void loadAllTasks()
    {
        setLabel();
        ArrayList<Expense> taskList = dbHelper.getCategoryHistory(MainActivity.cat);
        if(adapter==null)
        {
            adapter = new ExpenseAdapter(this,R.layout.category_row, taskList);
            allTasks.setAdapter(adapter);
        }
        else
        {
            adapter.clear();
            adapter.addAll(taskList);
            adapter.notifyDataSetChanged();
        }
    }

    private void setLabel()
    {
        TextView textView = findViewById(R.id.textViewCategory);
        switch (MainActivity.cat)
        {
            case "regular":
                textView.setText("Регулярные расходы");
                break;
            case "big":
                textView.setText("Большие покупки");
                break;
            case "entertainment":
                textView.setText("Развлечения");
                break;
            case "gifts":
                textView.setText("Подарки");
                break;
            case "self":
                textView.setText("Образование");
                break;
            case "safement":
                textView.setText("Накопления");
                break;
        }
    }

    public void onClickMainMenu(View view)
    {
        Intent intent = new Intent(CategoryActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
