package com.carnalizer.mybudjet;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.carnalizer.mybudjet.db.DB;
import com.carnalizer.mybudjet.entities.BudjetSystem;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {

    DB dbHelper;
    BudjetSystem budjetSystem;
    private Calendar dateAndTime1=new GregorianCalendar();
    private Calendar dateAndTime2=new GregorianCalendar();
    private TextView totalIncome;
    private TextView totalExpense;
    private TextRoundCornerProgressBar regularBar;
    private TextRoundCornerProgressBar educationBar;
    private TextRoundCornerProgressBar entertainmentBar;
    private TextRoundCornerProgressBar bigBar;
    private TextRoundCornerProgressBar giftBar;
    private TextRoundCornerProgressBar safeBar;
    private Button incomeBtn;
    private Button expenseBtn;
    private Button reportBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper = new DB(this);
        budjetSystem = new BudjetSystem();
        totalIncome = findViewById(R.id.mainMenuTotalIncome);
        totalExpense = findViewById(R.id.mainMenuTotalExpense);
        regularBar = findViewById(R.id.regularBar);
        regularBar.setProgress(0);
        educationBar = findViewById(R.id.educationBar);
        educationBar.setProgress(0);
        entertainmentBar = findViewById(R.id.entertainmentBar);
        entertainmentBar.setProgress(0);
        bigBar = findViewById(R.id.bigBar);
        bigBar.setProgress(0);
        giftBar = findViewById(R.id.giftBar);
        giftBar.setProgress(0);
        safeBar = findViewById(R.id.safeBar);
        safeBar.setProgress(0);
        incomeBtn = findViewById(R.id.incomeBtn);
        expenseBtn = findViewById(R.id.expenseBtn);
        reportBtn = findViewById(R.id.reportBtn);
        setInitialStartDate();
        setCategoryProgress();
        setTotalProgress();

    }

    private void setTotalProgress(){
        Float incomeSum = dbHelper.getIncomeSum();
        Float expenseSum = dbHelper.getExpenseSum();
        totalIncome.setText(String.valueOf(incomeSum));
        totalExpense.setText(String.valueOf(expenseSum));
    }

    private void setCategoryProgress() {
        setCategoryMax();

        regularBar.setProgress(dbHelper.getCategorySum("regular"));
        regularBar.setProgressText(String.valueOf(dbHelper.getCategorySum("regular")));
        checkOverCat(regularBar,0.6f);
        educationBar.setProgress(dbHelper.getCategorySum("self"));
        educationBar.setProgressText(String.valueOf(dbHelper.getCategorySum("self")));
        checkOverCat(educationBar,0.1f);
        entertainmentBar.setProgress(dbHelper.getCategorySum("entertainment"));
        entertainmentBar.setProgressText(String.valueOf(dbHelper.getCategorySum("entertainment")));
        checkOverCat(entertainmentBar,0.1f);
        bigBar.setProgress(dbHelper.getCategorySum("big"));
        bigBar.setProgressText(String.valueOf(dbHelper.getCategorySum("big")));
        checkOverCat(bigBar,0.05f);
        giftBar.setProgress(dbHelper.getCategorySum("gifts"));
        giftBar.setProgressText(String.valueOf(dbHelper.getCategorySum("gifts")));
        checkOverCat(giftBar,0.05f);
        safeBar.setProgress(dbHelper.getCategorySum("safement"));
        safeBar.setProgressText(String.valueOf(dbHelper.getCategorySum("safement")));
        checkOverCat(safeBar,0.1f);
    }

    private void checkOverCat(TextRoundCornerProgressBar bar, float k){
        float totalIncome = dbHelper.getIncomeSum();
        if(Float.parseFloat(bar.getProgressText())>totalIncome*k){
            bar.setProgressColor(Color.parseColor("#F74949"));
        }
    }

    private void setCategoryMax(){
        Float incomeSum = dbHelper.getIncomeSum();
        regularBar.setMax((float) (incomeSum*0.6));
        regularBar.setSecondaryProgress((float) (incomeSum*0.6));
        educationBar.setMax((float) (incomeSum*0.1));
        educationBar.setSecondaryProgress((float) (incomeSum*0.1));
        entertainmentBar.setMax((float) (incomeSum*0.1));
        entertainmentBar.setSecondaryProgress((float) (incomeSum*0.1));
        bigBar.setMax((float) (incomeSum*0.05));
        bigBar.setSecondaryProgress((float) (incomeSum*0.05));
        giftBar.setMax((float) (incomeSum*0.05));
        giftBar.setSecondaryProgress((float) (incomeSum*0.05));
        safeBar.setMax((float) (incomeSum*0.1));
        safeBar.setSecondaryProgress((float) (incomeSum*0.1));
    }

    private void setInitialStartDate()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");

        if(dateAndTime1.get(Calendar.MONTH)!=0)
            dateAndTime1.set(dateAndTime1.get(Calendar.YEAR),dateAndTime1.get(Calendar.MONTH)-1,dateAndTime1.get(Calendar.DATE));
        else
            dateAndTime1.set(dateAndTime1.get(Calendar.YEAR)-1,dateAndTime1.get(Calendar.MONTH)-1,dateAndTime1.get(Calendar.DATE));

        dbHelper.insertDates(sdf.format(dateAndTime1.getTime()),sdf.format(dateAndTime2.getTime()));

    }

    public void onClickAbout(View view){
        final AlertDialog dialog = new AlertDialog.Builder(MainActivity.this)
                .setTitle("О программе")
                .setMessage("Приложение MyBudjet было разработано с целью упрощения контроля " +
                        "финансов. \nЗдесь вы можете вносить информацию о своих доходах и " +
                        "расходах, контролируя каждый аспект жизни человека." +
                        " \nОсновные категории это: регулярные расходы, образование," +
                        " развлечения, большие покупки, подарки и накопления. \nПрограмма построена " +
                        "на принципе распределения личных средств 'Метод кувшинов', выделяя для " +
                        "каждой категории часть доходов. \nЭто приложение поможет разумно тратить" +
                        " денежные средства и учитывать каждый важный аспект жизни.\n" +
                        "Данный програмный продукт разработал в качестве дипломного проекта " +
                        "студент ХНЭУ им. С.Кузнеца - Ширков Андрей. ")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create();
        dialog.show();
    }

    public static String cat;

    public void onClickCategory(View view)
    {
        switch (view.getId())
        {
            case R.id.regularBar:
                cat = "regular";
                break;
            case R.id.bigBar:
                cat = "big";
                break;
            case R.id.entertainmentBar:
                cat = "entertainment";
                break;
            case R.id.giftBar:
                cat = "gifts";
                break;
            case R.id.educationBar:
                cat = "self";
                break;
            case R.id.safeBar:
                cat = "safement";
                break;
        }
        Intent intent = new Intent(MainActivity.this, CategoryActivity.class);
        startActivity(intent);
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
