package com.carnalizer.mybudjet.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.carnalizer.mybudjet.entities.Dates;
import com.carnalizer.mybudjet.entities.Expense;
import com.carnalizer.mybudjet.entities.Income;
import com.carnalizer.mybudjet.entities.Report;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class DB extends SQLiteOpenHelper {

    private static final String db_name = "carnalizerApp";
    private static final int db_version = 1;

    private static final String db_table_categories = "categories";
    private static final String db_column_category_name = "categoryName";

    private static final String db_table_subcategories = "subcategories";
    private static final String db_column_subcategory_name = "subcategoryName";
    //ID_category

    private static final String db_table_expense = "expense";
    private static final String db_column_expense_date = "expenseDate";
    private static final String db_column_expense_amount = "expenseAmount";
    //ID_subcategory

    private static final String db_table_income = "income";
    private static final String db_column_income_date = "incomeDate";
    private static final String db_column_income_amount = "incomeAmount";

    private static final String db_table_dates = "dates";
    private static final String db_column_dates_1 = "firstDate";
    private static final String db_column_dates_2 = "secondDate";

    private static final String db_table_report = "report";
    private static final String db_column_report_date = "reportDate";
    private static final String db_column_report_name = "reportName";
    private static final String db_column_report_text = "reportText";

    private static final String db_table_report_dates = "reportDates";
    private static final String db_column_report_dates_1 = "firstReportDate";
    private static final String db_column_report_dates_2 = "secondReportDate";

    public DB(Context context) {
        super(context, db_name, null, db_version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");

        String queryCat = String.format("CREATE TABLE %s (ID_category INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                "TEXT NOT NULL);", db_table_categories, db_column_category_name);
        db.execSQL(queryCat);
        //fillCategories();

        String querySubcat = String.format("CREATE TABLE %s (ID_subcategory INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL,ID_category INTEGER, FOREIGN KEY (ID_category) REFERENCES %s(ID_category));",
                db_table_subcategories, db_column_subcategory_name, db_table_categories);
        db.execSQL(querySubcat);
        //fillSubcategories();

        String queryExpense = String.format("CREATE TABLE %s (ID_expense INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL, %s FLOAT NOT NULL, ID_subcategory INTEGER, FOREIGN KEY (ID_subcategory) REFERENCES " +
                        "%s(ID_subcategory));",
                db_table_expense, db_column_expense_date, db_column_expense_amount, db_table_subcategories);
        db.execSQL(queryExpense);

        String queryIncome = String.format("CREATE TABLE %s (ID_income INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL, %s FLOAT NOT NULL);",
                db_table_income, db_column_income_date, db_column_income_amount);
        db.execSQL(queryIncome);

        String queryDates = String.format("CREATE TABLE %s (ID_dates INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL, %s TEXT NOT NULL);",
                db_table_dates, db_column_dates_1, db_column_dates_2);
        db.execSQL(queryDates);

        String queryReport = String.format("CREATE TABLE %s (ID_report INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL, %s TEXT NOT NULL, %s TEXT NOT NULL);",
                db_table_report, db_column_report_date, db_column_report_name, db_column_report_text);
        db.execSQL(queryReport);

        String queryReportDates = String.format("CREATE TABLE %s (ID_reportDates INTEGER PRIMARY KEY AUTOINCREMENT, %s " +
                        "TEXT NOT NULL, %s TEXT NOT NULL);",
                db_table_report_dates, db_column_report_dates_1, db_column_report_dates_2);
        db.execSQL(queryReportDates);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query1 = String.format("DELETE TABLE IF EXISTS %s", db_table_categories);
        db.execSQL(query1);
        String query2 = String.format("DELETE TABLE IF EXISTS %s", db_table_subcategories);
        db.execSQL(query2);
        String query3 = String.format("DELETE TABLE IF EXISTS %s", db_table_expense);
        db.execSQL(query3);
        String query4 = String.format("DELETE TABLE IF EXISTS %s", db_table_income);
        db.execSQL(query4);
        String query5 = String.format("DELETE TABLE IF EXISTS %s", db_table_dates);
        db.execSQL(query5);
        String query6 = String.format("DELETE TABLE IF EXISTS %s", db_table_report);
        db.execSQL(query6);
        String query7 = String.format("DELETE TABLE IF EXISTS %s", db_table_report_dates);
        db.execSQL(query7);
        onCreate(db);
    }

    public void insertReport(String date, String name, String text) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_column_report_date, date);
        values.put(db_column_report_name, name);
        values.put(db_column_report_text, text);
        db.insertWithOnConflict(db_table_report, null, values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void deleteReportData(String date, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + db_table_report + " WHERE (" + db_column_report_date + " = '" + date + "' AND " + db_column_report_name + " = " + name + ")");
        db.close();
    }

    public ArrayList<Report> getReport() {
        ArrayList<Report> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + db_column_report_date + ", " + db_column_report_name + ", " + db_column_report_text + " FROM " + db_table_report + " ORDER BY " + db_column_report_date + " DESC", null);
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(db_column_report_date);
            int index2 = cursor.getColumnIndex(db_column_report_name);
            int index3 = cursor.getColumnIndex(db_column_report_text);
            allTasks.add(new Report(cursor.getString(index1), cursor.getString(index2), cursor.getString(index3)));
        }
        cursor.close();
        db.close();

        return allTasks;
    }

    public Report getOneReport(String date, String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + db_column_report_date + ", " + db_column_report_name + ", " + db_column_report_text + " FROM " + db_table_report + " WHERE " + db_column_report_date + " = '"+date+"' AND "+db_column_report_name+" = '"+name+"'", null);

        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex(db_column_report_date);
            int index2 = cursor.getColumnIndex(db_column_report_name);
            int index3 = cursor.getColumnIndex(db_column_report_text);
            Report report = new Report(cursor.getString(index1), cursor.getString(index2), cursor.getString(index3));
            cursor.close();
            db.close();
            return report;
        }


        return null;
    }

    public void insertReportDates(String date1, String date2) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (isReportDatesExists())
            db.execSQL("DELETE FROM " + db_table_report_dates);
        ContentValues values = new ContentValues();
        values.put(db_column_report_dates_1, date1);
        values.put(db_column_report_dates_2, date2);
        db.insert(db_table_report_dates, null, values);
        db.close();
    }

    public Dates getReportDates() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + db_column_report_dates_1 + ", " + db_column_report_dates_2 + " FROM " + db_table_report, null);
        Dates dates = new Dates();
        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex(db_column_report_dates_1);
            int index2 = cursor.getColumnIndex(db_column_report_dates_2);
            dates = new Dates(cursor.getString(index1), cursor.getString(index2));
        }

        cursor.close();
        return dates;
    }

    public boolean isReportDatesExists() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + db_column_report_dates_1 + ", " + db_column_report_dates_2 + " FROM " + db_table_report_dates, null);
            if (cursor != null && cursor.getCount() > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void insertDates(String date1, String date2) {
        SQLiteDatabase db = this.getWritableDatabase();
        if (isDatesExists())
            db.execSQL("DELETE FROM " + db_table_dates);
        ContentValues values = new ContentValues();
        values.put(db_column_dates_1, date1);
        values.put(db_column_dates_2, date2);
        db.insert(db_table_dates, null, values);
        db.close();
    }

    public Dates getDates() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + db_column_dates_1 + ", " + db_column_dates_2 + " FROM " + db_table_dates, null);
        Dates dates = new Dates();
        if (cursor.moveToFirst()) {
            int index1 = cursor.getColumnIndex(db_column_dates_1);
            int index2 = cursor.getColumnIndex(db_column_dates_2);
            dates = new Dates(cursor.getString(index1), cursor.getString(index2));
        }

        cursor.close();
        return dates;
    }

    public boolean isDatesExists() {
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery("SELECT " + db_column_dates_1 + ", " + db_column_dates_2 + " FROM " + db_table_dates, null);
            if (cursor != null && cursor.getCount() > 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            return false;
        }
    }

    public void insertIncomeData(String date, float amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(db_column_income_date, date);
        values.put(db_column_income_amount, amount);
        db.insertWithOnConflict(db_table_income, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public void insertExpenseData(String date, float amount, String subcategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id = getCatId(subcategory);

        values.put(db_column_expense_date, date);
        values.put(db_column_expense_amount, amount);
        values.put("ID_subcategory", id);
        db.insertWithOnConflict(db_table_expense, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }


    public void deleteCategoriesData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + db_table_categories);
        db.close();
    }

    public void deleteSubcategoriesData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + db_table_subcategories);
        db.close();
    }


    public ArrayList<Income> getIncome() {
        ArrayList<Income> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + db_column_income_date + ", " + db_column_income_amount + " FROM " + db_table_income + " ORDER BY " + db_column_income_date + " DESC", null);
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(db_column_income_date);
            int index2 = cursor.getColumnIndex(db_column_income_amount);
            allTasks.add(new Income(cursor.getFloat(index2), cursor.getString(index1)));
        }
        cursor.close();
        db.close();

        return allTasks;
    }

    public float getIncomeSum() {
        float sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + db_column_income_amount + ") as TotalIncome FROM " + db_table_income + " WHERE " + db_column_income_date + " >= (SELECT " + db_column_dates_1 + " FROM " + db_table_dates + ") AND " + db_column_income_date + " <= (SELECT " + db_column_dates_2 + " FROM " + db_table_dates + ")", null);
        if (cursor.moveToFirst()) {
            sum = cursor.getFloat(cursor.getColumnIndex("TotalIncome"));
        }
        cursor.close();
        return sum;
    }

    public float getDateIncomeSum() {
        float sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + db_column_income_amount + ") as TotalIncome FROM " + db_table_income + " WHERE " + db_column_income_date + " BETWEEN (SELECT "+db_column_report_dates_1+" FROM "+db_table_report_dates+") AND (SELECT "+db_column_report_dates_2+" FROM "+db_table_report_dates+")", null);
        if (cursor.moveToFirst()) {
            sum = cursor.getFloat(cursor.getColumnIndex("TotalIncome"));
        }
        cursor.close();
        return sum;
    }

    public float getExpenseSum() {
        float sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + db_column_expense_amount + ") as TotalExpense FROM " + db_table_expense + " WHERE " + db_column_expense_date + " >= (SELECT " + db_column_dates_1 + " FROM " + db_table_dates + ") AND " + db_column_expense_date + " <= (SELECT " + db_column_dates_2 + " FROM " + db_table_dates + ")", null);
        if (cursor.moveToFirst()) {
            sum = cursor.getFloat(cursor.getColumnIndex("TotalExpense"));
        }
        cursor.close();
        return sum;
    }

    public float getDateExpenseSum() {
        float sum = 0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + db_column_expense_amount + ") as TotalExpense FROM " + db_table_expense + " WHERE " + db_column_expense_date + " BETWEEN (SELECT "+db_column_report_dates_1+" FROM "+db_table_report_dates+") AND (SELECT "+db_column_report_dates_2+" FROM "+db_table_report_dates+")", null);
        if (cursor.moveToFirst()) {
            sum = cursor.getFloat(cursor.getColumnIndex("TotalExpense"));
        }
        cursor.close();
        return sum;
    }

    public float getDateCategorySum(String category, String date1, String date2) {
        float sum = 0;
        String subcats = "";
        switch (category) {
            case "regular":
                subcats = "WHERE ((ID_subcategory = 3 OR ID_subcategory = 6 OR " +
                        "ID_subcategory = 7 OR ID_subcategory = 8 OR ID_subcategory = 10 OR " +
                        "ID_subcategory = 11 OR ID_subcategory = 12 OR ID_subcategory = 14 OR " +
                        "ID_subcategory = 16 OR ID_subcategory = 17 OR ID_subcategory = 19 OR " +
                        "ID_subcategory = 23 OR ID_subcategory = 24 OR ID_subcategory = 25 OR " +
                        "ID_subcategory = 27 OR ID_subcategory = 28 OR ID_subcategory = 29 OR " +
                        "ID_subcategory = 33 OR ID_subcategory = 34 OR ID_subcategory = 35 OR " +
                        "ID_subcategory = 38 OR ID_subcategory = 41 OR ID_subcategory = 42 OR " +
                        "ID_subcategory = 43 OR ID_subcategory = 44 OR ID_subcategory = 52 OR " +
                        "ID_subcategory = 59 OR ID_subcategory = 78 OR ID_subcategory = 79) ";
                break;
            case "gifts":
                subcats = "WHERE ((ID_subcategory = 58 OR ID_subcategory = 15) ";
                break;
            case "entertainment":
                subcats = "WHERE ((ID_subcategory = 60 OR ID_subcategory = 61 OR " +
                        "ID_subcategory = 62 OR ID_subcategory = 63 OR ID_subcategory = 64 OR " +
                        "ID_subcategory = 65 OR ID_subcategory = 66 OR ID_subcategory = 67 OR " +
                        "ID_subcategory = 68 OR ID_subcategory = 69 OR ID_subcategory = 70 OR " +
                        "ID_subcategory = 71 OR ID_subcategory = 72 OR ID_subcategory = 73 OR " +
                        "ID_subcategory = 74 OR ID_subcategory = 75) ";
                break;
            case "self":
                subcats = "WHERE ((ID_subcategory = 53 OR ID_subcategory = 54 OR " +
                        "ID_subcategory = 48) ";
                break;
            case "big":
                subcats = "WHERE ((ID_subcategory = 4 OR ID_subcategory = 5 OR " +
                        "ID_subcategory = 9 OR ID_subcategory = 18 OR ID_subcategory = 20 OR " +
                        "ID_subcategory = 21 OR ID_subcategory = 22 OR ID_subcategory = 26 OR " +
                        "ID_subcategory = 30 OR ID_subcategory = 31 OR ID_subcategory = 32 OR " +
                        "ID_subcategory = 36 OR ID_subcategory = 37 OR ID_subcategory = 39 OR " +
                        "ID_subcategory = 45 OR ID_subcategory = 46 OR ID_subcategory = 47 OR " +
                        "ID_subcategory = 49 OR ID_subcategory = 50 OR ID_subcategory = 51 OR " +
                        "ID_subcategory = 55 OR ID_subcategory = 56 OR ID_subcategory = 57 OR " +
                        "ID_subcategory = 76 OR ID_subcategory = 77) ";
                break;
            case "safement":
                subcats = "WHERE ((ID_subcategory = 2 OR ID_subcategory = 13) ";
                break;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + db_column_expense_amount + ") as TotalExpense FROM (SELECT " + db_column_expense_date + ", " + db_column_expense_amount + ", ID_subcategory FROM " + db_table_expense + " " + subcats + " AND " + db_column_expense_date + " >= (SELECT " + db_column_report_dates_1 + " " + "FROM " + db_table_report_dates + ") AND " + db_column_expense_date + " <= " + "(SELECT " + db_column_report_dates_2 + " FROM " + db_table_report_dates + ")))", null);
        if (cursor.moveToFirst()) {
            sum = cursor.getFloat(cursor.getColumnIndex("TotalExpense"));
        }
        cursor.close();
        return sum;
    }

    public float getCategorySum(String category) {
        float sum = 0;
        String subcats = "";
        switch (category) {
            case "regular":
                subcats = "WHERE ((ID_subcategory = 3 OR ID_subcategory = 6 OR " +
                        "ID_subcategory = 7 OR ID_subcategory = 8 OR ID_subcategory = 10 OR " +
                        "ID_subcategory = 11 OR ID_subcategory = 12 OR ID_subcategory = 14 OR " +
                        "ID_subcategory = 16 OR ID_subcategory = 17 OR ID_subcategory = 19 OR " +
                        "ID_subcategory = 23 OR ID_subcategory = 24 OR ID_subcategory = 25 OR " +
                        "ID_subcategory = 27 OR ID_subcategory = 28 OR ID_subcategory = 29 OR " +
                        "ID_subcategory = 33 OR ID_subcategory = 34 OR ID_subcategory = 35 OR " +
                        "ID_subcategory = 38 OR ID_subcategory = 41 OR ID_subcategory = 42 OR " +
                        "ID_subcategory = 43 OR ID_subcategory = 44 OR ID_subcategory = 52 OR " +
                        "ID_subcategory = 59 OR ID_subcategory = 78 OR ID_subcategory = 79) " +
                        "AND " + db_column_expense_date + " >= (SELECT " + db_column_dates_1 + " " +
                        "FROM " + db_table_dates + ") AND " + db_column_expense_date + " <= " +
                        "(SELECT " + db_column_dates_2 + " FROM " + db_table_dates + "))";
                break;
            case "gifts":
                subcats = "WHERE ((ID_subcategory = 58 OR ID_subcategory = 15) " +
                        "AND " + db_column_expense_date + " >= " +
                        "(SELECT " + db_column_dates_1 + " FROM " + db_table_dates + ") " +
                        "AND " + db_column_expense_date + " <= (SELECT " + db_column_dates_2 +
                        " FROM " + db_table_dates + "))";
                break;
            case "entertainment":
                subcats = "WHERE ((ID_subcategory = 60 OR ID_subcategory = 61 OR " +
                        "ID_subcategory = 62 OR ID_subcategory = 63 OR ID_subcategory = 64 OR " +
                        "ID_subcategory = 65 OR ID_subcategory = 66 OR ID_subcategory = 67 OR " +
                        "ID_subcategory = 68 OR ID_subcategory = 69 OR ID_subcategory = 70 OR " +
                        "ID_subcategory = 71 OR ID_subcategory = 72 OR ID_subcategory = 73 OR " +
                        "ID_subcategory = 74 OR ID_subcategory = 75) " +
                        "AND " + db_column_expense_date + " >= (SELECT " + db_column_dates_1 +
                        " FROM " + db_table_dates + ") AND " + db_column_expense_date + " <= (SELECT " +
                        db_column_dates_2 + " FROM " + db_table_dates + "))";
                break;
            case "self":
                subcats = "WHERE ((ID_subcategory = 53 OR ID_subcategory = 54 OR " +
                        "ID_subcategory = 48) " +
                        "AND " + db_column_expense_date + " >= (SELECT " + db_column_dates_1 + " FROM " +
                        db_table_dates + ") AND " + db_column_expense_date + " <= (SELECT " +
                        db_column_dates_2 + " FROM " + db_table_dates + "))";
                break;
            case "big":
                subcats = "WHERE ((ID_subcategory = 4 OR ID_subcategory = 5 OR " +
                        "ID_subcategory = 9 OR ID_subcategory = 18 OR ID_subcategory = 20 OR " +
                        "ID_subcategory = 21 OR ID_subcategory = 22 OR ID_subcategory = 26 OR " +
                        "ID_subcategory = 30 OR ID_subcategory = 31 OR ID_subcategory = 32 OR " +
                        "ID_subcategory = 36 OR ID_subcategory = 37 OR ID_subcategory = 39 OR " +
                        "ID_subcategory = 45 OR ID_subcategory = 46 OR ID_subcategory = 47 OR " +
                        "ID_subcategory = 49 OR ID_subcategory = 50 OR ID_subcategory = 51 OR " +
                        "ID_subcategory = 55 OR ID_subcategory = 56 OR ID_subcategory = 57 OR " +
                        "ID_subcategory = 76 OR ID_subcategory = 77) " +
                        "AND " + db_column_expense_date + " >= (SELECT " +
                        db_column_dates_1 + " FROM " + db_table_dates + ") AND " +
                        db_column_expense_date + " <= (SELECT " + db_column_dates_2 +
                        " FROM " + db_table_dates + "))";
                break;
            case "safement":
                subcats = "WHERE ((ID_subcategory = 2 OR ID_subcategory = 13) AND " +
                        db_column_expense_date + " >= (SELECT " + db_column_dates_1 + " FROM " +
                        db_table_dates + ") AND " + db_column_expense_date + " <= (SELECT " +
                        db_column_dates_2 + " FROM " + db_table_dates + "))";
                break;
        }
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT SUM(" + db_column_expense_amount + ") as TotalExpense FROM (SELECT " + db_column_expense_date + ", " + db_column_expense_amount + ", ID_subcategory FROM " + db_table_expense + " " + subcats + ")", null);
        if (cursor.moveToFirst()) {
            sum = cursor.getFloat(cursor.getColumnIndex("TotalExpense"));
        }
        cursor.close();
        return sum;
    }

    public ArrayList<Expense> getExpense() {
        ArrayList<Expense> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + db_column_expense_date + ", " + db_column_expense_amount + ", ID_subcategory FROM " + db_table_expense + " ORDER BY " + db_column_expense_date + " DESC", null);

        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(db_column_expense_date);
            int index2 = cursor.getColumnIndex(db_column_expense_amount);
            int index3 = cursor.getColumnIndex("ID_subcategory");
            int id = cursor.getInt(index3);
            String catName = getCatName(id);

            allTasks.add(new Expense(cursor.getFloat(index2), catName, cursor.getString(index1)));
        }
        cursor.close();
        db.close();
        return allTasks;
    }

    public ArrayList<Expense> getCategoryHistory(String category) {
        ArrayList<Expense> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String subcats = "";
        switch (category) {
            case "regular":
                subcats = "WHERE ((ID_subcategory = 3 OR ID_subcategory = 6 OR " +
                        "ID_subcategory = 7 OR ID_subcategory = 8 OR ID_subcategory = 10 OR " +
                        "ID_subcategory = 11 OR ID_subcategory = 12 OR ID_subcategory = 14 OR " +
                        "ID_subcategory = 16 OR ID_subcategory = 17 OR ID_subcategory = 19 OR " +
                        "ID_subcategory = 23 OR ID_subcategory = 24 OR ID_subcategory = 25 OR " +
                        "ID_subcategory = 27 OR ID_subcategory = 28 OR ID_subcategory = 29 OR " +
                        "ID_subcategory = 33 OR ID_subcategory = 34 OR ID_subcategory = 35 OR " +
                        "ID_subcategory = 38 OR ID_subcategory = 41 OR ID_subcategory = 42 OR " +
                        "ID_subcategory = 43 OR ID_subcategory = 44 OR ID_subcategory = 52 OR " +
                        "ID_subcategory = 59 OR ID_subcategory = 78 OR ID_subcategory = 79) " +
                        "AND " + db_column_expense_date + " >= (SELECT " + db_column_dates_1 + " " +
                        "FROM " + db_table_dates + ") AND " + db_column_expense_date + " <= " +
                        "(SELECT " + db_column_dates_2 + " FROM " + db_table_dates + "))";
                break;
            case "gifts":
                subcats = "WHERE ((ID_subcategory = 58 OR ID_subcategory = 15) " +
                        "AND " + db_column_expense_date + " >= " +
                        "(SELECT " + db_column_dates_1 + " FROM " + db_table_dates + ") " +
                        "AND " + db_column_expense_date + " <= (SELECT " + db_column_dates_2 +
                        " FROM " + db_table_dates + "))";
                break;
            case "entertainment":
                subcats = "WHERE ((ID_subcategory = 60 OR ID_subcategory = 61 OR " +
                        "ID_subcategory = 62 OR ID_subcategory = 63 OR ID_subcategory = 64 OR " +
                        "ID_subcategory = 65 OR ID_subcategory = 66 OR ID_subcategory = 67 OR " +
                        "ID_subcategory = 68 OR ID_subcategory = 69 OR ID_subcategory = 70 OR " +
                        "ID_subcategory = 71 OR ID_subcategory = 72 OR ID_subcategory = 73 OR " +
                        "ID_subcategory = 74 OR ID_subcategory = 75) " +
                        "AND " + db_column_expense_date + " >= (SELECT " + db_column_dates_1 +
                        " FROM " + db_table_dates + ") AND " + db_column_expense_date + " <= (SELECT " +
                        db_column_dates_2 + " FROM " + db_table_dates + "))";
                break;
            case "self":
                subcats = "WHERE ((ID_subcategory = 53 OR ID_subcategory = 54 OR " +
                        "ID_subcategory = 48) " +
                        "AND " + db_column_expense_date + " >= (SELECT " + db_column_dates_1 + " FROM " +
                        db_table_dates + ") AND " + db_column_expense_date + " <= (SELECT " +
                        db_column_dates_2 + " FROM " + db_table_dates + "))";
                break;
            case "big":
                subcats = "WHERE ((ID_subcategory = 4 OR ID_subcategory = 5 OR " +
                        "ID_subcategory = 9 OR ID_subcategory = 18 OR ID_subcategory = 20 OR " +
                        "ID_subcategory = 21 OR ID_subcategory = 22 OR ID_subcategory = 26 OR " +
                        "ID_subcategory = 30 OR ID_subcategory = 31 OR ID_subcategory = 32 OR " +
                        "ID_subcategory = 36 OR ID_subcategory = 37 OR ID_subcategory = 39 OR " +
                        "ID_subcategory = 45 OR ID_subcategory = 46 OR ID_subcategory = 47 OR " +
                        "ID_subcategory = 49 OR ID_subcategory = 50 OR ID_subcategory = 51 OR " +
                        "ID_subcategory = 55 OR ID_subcategory = 56 OR ID_subcategory = 57 OR " +
                        "ID_subcategory = 76 OR ID_subcategory = 77) " +
                        "AND " + db_column_expense_date + " >= (SELECT " +
                        db_column_dates_1 + " FROM " + db_table_dates + ") AND " +
                        db_column_expense_date + " <= (SELECT " + db_column_dates_2 +
                        " FROM " + db_table_dates + "))";
                break;
            case "safement":
                subcats = "WHERE ((ID_subcategory = 2 OR ID_subcategory = 13) AND " +
                        db_column_expense_date + " >= (SELECT " + db_column_dates_1 + " FROM " +
                        db_table_dates + ") AND " + db_column_expense_date + " <= (SELECT " +
                        db_column_dates_2 + " FROM " + db_table_dates + "))";
                break;
        }
        Cursor cursor = db.rawQuery("SELECT " + db_column_expense_date + ", " + db_column_expense_amount + ", ID_subcategory FROM " + db_table_expense + " " + subcats + " ORDER BY " + db_column_expense_date + " DESC", null);

        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(db_column_expense_date);
            int index2 = cursor.getColumnIndex(db_column_expense_amount);
            int index3 = cursor.getColumnIndex("ID_subcategory");
            int id = cursor.getInt(index3);
            String catName = getCatName(id);

            allTasks.add(new Expense(cursor.getFloat(index2), catName, cursor.getString(index1)));
        }
        cursor.close();
        db.close();
        return allTasks;
    }

    public ArrayList<Expense> getHistory() {
        ArrayList<Expense> allTasks = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + db_column_expense_date + ", " + db_column_expense_amount + ", ID_subcategory FROM " + db_table_expense + " WHERE " + db_column_expense_date + " >= (SELECT " + db_column_dates_1 + " FROM " + db_table_dates + ") AND " + db_column_expense_date + " <= (SELECT " + db_column_dates_2 + " FROM " + db_table_dates + ") ORDER BY " + db_column_expense_date + " DESC", null);

        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(db_column_expense_date);
            int index2 = cursor.getColumnIndex(db_column_expense_amount);
            int index3 = cursor.getColumnIndex("ID_subcategory");
            int id = cursor.getInt(index3);
            String catName = getCatName(id);

            allTasks.add(new Expense(cursor.getFloat(index2), catName, cursor.getString(index1)));
        }
        cursor = db.rawQuery("SELECT " + db_column_income_date + ", " + db_column_income_amount + " FROM " + db_table_income + " WHERE " + db_column_income_date + " >= (SELECT " + db_column_dates_1 + " FROM " + db_table_dates + ") AND " + db_column_income_date + " <= (SELECT " + db_column_dates_2 + " FROM " + db_table_dates + ") ORDER BY " + db_column_income_date + " DESC", null);
        while (cursor.moveToNext()) {
            int index1 = cursor.getColumnIndex(db_column_income_date);
            int index2 = cursor.getColumnIndex(db_column_income_amount);
            allTasks.add(new Expense(cursor.getFloat(index2), "Доход", cursor.getString(index1)));
        }
        cursor.close();
        db.close();
        return allTasks;
    }

    public void deleteIncomeData(String date, Float amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM " + db_table_income + " WHERE (" + db_column_income_date + " = '" + date + "' AND " + db_column_income_amount + " = " + amount + ")");
        db.close();
    }

    public void deleteExpenseData(String date, Float amount, String subcategory) {
        SQLiteDatabase db = this.getWritableDatabase();
        int id = getCatId(subcategory);

        db.execSQL("DELETE FROM " + db_table_expense + " WHERE " + db_column_expense_date + " = '" + date + "' AND " + db_column_expense_amount + " = " + amount + " AND ID_subcategory = " + id);
        db.close();
    }

    private int getCatId(String subcategory) {
        int id = 0;
        switch (subcategory) {
            case "Без категории":
                id = 1;
                break;
            case "Накопления":
                id = 2;
                break;
            case "Автомобиль (Автохимия)":
                id = 3;
                break;
            case "Автомобиль (Аксессуары)":
                id = 4;
                break;
            case "Автомобиль (Запчати)":
                id = 5;
                break;
            case "Автомобиль (Мойка)":
                id = 6;
                break;
            case "Автомобиль (Парковка)":
                id = 7;
                break;
            case "Автомобиль (Платные дороги)":
                id = 8;
                break;
            case "Автомобиль (Сервис)":
                id = 9;
                break;
            case "Автомобиль (Страховка)":
                id = 10;
                break;
            case "Автомобиль (Топливо)":
                id = 11;
                break;
            case "Банк (Кредитные выплаты)":
                id = 12;
                break;
            case "Банк (Депозит)":
                id = 13;
                break;
            case "Бизнес-услуги":
                id = 14;
                break;
            case "Благотворительность":
                id = 15;
                break;
            case "Государство (Налоги)":
                id = 16;
                break;
            case "Государство (Пошлины)":
                id = 17;
                break;
            case "Государство (Штрафы)":
                id = 18;
                break;
            case "Дети (Занятия)":
                id = 19;
                break;
            case "Дети (Здоровье)":
                id = 20;
                break;
            case "Дети (Игрушки)":
                id = 21;
                break;
            case "Дети (Одежда)":
                id = 22;
                break;
            case "Дети (Питание)":
                id = 23;
                break;
            case "Дом (Аренда)":
                id = 24;
                break;
            case "Дом (Бытовая химия)":
                id = 25;
                break;
            case "Дом (Бытовые услуги)":
                id = 26;
                break;
            case "Дом (Газ)":
                id = 27;
                break;
            case "Дом (Интернет)":
                id = 28;
                break;
            case "Дом (Квартплата)":
                id = 29;
                break;
            case "Дом (Мебель)":
                id = 30;
                break;
            case "Дом (Посуда)":
                id = 31;
                break;
            case "Дом (Ремонт)":
                id = 32;
                break;
            case "Дом (Страхование)":
                id = 33;
                break;
            case "Дом (Телефон)":
                id = 34;
                break;
            case "Дом (Электричество)":
                id = 35;
                break;
            case "Домашние животные (Аксессуары, игрушки)":
                id = 36;
                break;
            case "Домашние животные (Вет. услуги)":
                id = 37;
                break;
            case "Домашние животные (Корм)":
                id = 38;
                break;
            case "Домашние животные (Медикаменты)":
                id = 39;
                break;
            case "Другое":
                id = 40;
                break;
            case "Еда вне дома (Кофейня)":
                id = 41;
                break;
            case "Еда вне дома (Ланч)":
                id = 42;
                break;
            case "Еда вне дома (Ресторан)":
                id = 43;
                break;
            case "Еда вне дома (Фастфуд)":
                id = 44;
                break;
            case "Здоровье (Аптека)":
                id = 45;
                break;
            case "Здоровье (Инвентарь)":
                id = 46;
                break;
            case "Здоровье (Мед. услуги)":
                id = 47;
                break;
            case "Здоровье (Спорт)":
                id = 48;
                break;
            case "Здоровье (Страхование)":
                id = 49;
                break;
            case "Красота (Косметика)":
                id = 50;
                break;
            case "Красота (Салон красоты)":
                id = 51;
                break;
            case "Мобильные услуги":
                id = 52;
                break;
            case "Образование (Книги)":
                id = 53;
                break;
            case "Образование (Услуги)":
                id = 54;
                break;
            case "Одежда и обувь (Аксуссуары)":
                id = 55;
                break;
            case "Одежда и обувь (Взрослая)":
                id = 56;
                break;
            case "Одежда и обувь (Детская)":
                id = 57;
                break;
            case "Подарки":
                id = 58;
                break;
            case "Продукты питания":
                id = 59;
                break;
            case "Путешествия (Аренда авто)":
                id = 60;
                break;
            case "Путешествия (Билеты)":
                id = 61;
                break;
            case "Путешествия (Визы)":
                id = 62;
                break;
            case "Путешествия (Отель)":
                id = 63;
                break;
            case "Путешествия (Страховка)":
                id = 64;
                break;
            case "Путешествия (Сувениры)":
                id = 65;
                break;
            case "Путешествия (Услуги)":
                id = 66;
                break;
            case "Разлечения (Активный отдых)":
                id = 67;
                break;
            case "Разлечения (Бар, клуб)":
                id = 68;
                break;
            case "Разлечения (Игры, софт)":
                id = 69;
                break;
            case "Разлечения (Кино, театр, концерты)":
                id = 70;
                break;
            case "Разлечения (Книги)":
                id = 71;
                break;
            case "Разлечения (Музеи)":
                id = 72;
                break;
            case "Разлечения (Музыка, видео)":
                id = 73;
                break;
            case "Разлечения (Пресса)":
                id = 74;
                break;
            case "Разлечения (Хобби)":
                id = 75;
                break;
            case "Техника (Бытовая химия)":
                id = 76;
                break;
            case "Техника (Электроника)":
                id = 77;
                break;
            case "Транспорт (Общ. транспорт)":
                id = 78;
                break;
            case "Транспорт (Такси)":
                id = 79;
                break;
        }
        return id;
    }

    private String getCatName(int id) {
        String catName = null;
        switch (id) {
            case 1:
                catName = "Без категории";
                break;
            case 2:
                catName = "Накопления";
                break;
            case 3:
                catName = "Автомобиль (Автохимия)";
                break;
            case 4:
                catName = "Автомобиль (Аксессуары)";
                break;
            case 5:
                catName = "Автомобиль (Запчати)";
                break;
            case 6:
                catName = "Автомобиль (Мойка)";
                break;
            case 7:
                catName = "Автомобиль (Парковка)";
                break;
            case 8:
                catName = "Автомобиль (Платные дороги)";
                break;
            case 9:
                catName = "Автомобиль (Сервис)";
                break;
            case 10:
                catName = "Автомобиль (Страховка)";
                break;
            case 11:
                catName = "Автомобиль (Топливо)";
                break;
            case 12:
                catName = "Банк (Кредитные выплаты)";
                break;
            case 13:
                catName = "Банк (Депозит)";
                break;
            case 14:
                catName = "Бизнес-услуги";
                break;
            case 15:
                catName = "Благотворительность";
                break;
            case 16:
                catName = "Государство (Налоги)";
                break;
            case 17:
                catName = "Государство (Пошлины)";
                break;
            case 18:
                catName = "Государство (Штрафы)";
                break;
            case 19:
                catName = "Дети (Занятия)";
                break;
            case 20:
                catName = "Дети (Здоровье)";
                break;
            case 21:
                catName = "Дети (Игрушки)";
                break;
            case 22:
                catName = "Дети (Одежда)";
                break;
            case 23:
                catName = "Дети (Питание)";
                break;
            case 24:
                catName = "Дом (Аренда)";
                break;
            case 25:
                catName = "Дом (Бытовая химия)";
                break;
            case 26:
                catName = "Дом (Бытовые услуги)";
                break;
            case 27:
                catName = "Дом (Газ)";
                break;
            case 28:
                catName = "Дом (Интернет)";
                break;
            case 29:
                catName = "Дом (Квартплата)";
                break;
            case 30:
                catName = "Дом (Мебель)";
                break;
            case 31:
                catName = "Дом (Посуда)";
                break;
            case 32:
                catName = "Дом (Ремонт)";
                break;
            case 33:
                catName = "Дом (Страхование)";
                break;
            case 34:
                catName = "Дом (Телефон)";
                break;
            case 35:
                catName = "Дом (Электричество)";
                break;
            case 36:
                catName = "Домашние животные (Аксессуары, игрушки)";
                break;
            case 37:
                catName = "Домашние животные (Вет. услуги)";
                break;
            case 38:
                catName = "Домашние животные (Корм)";
                break;
            case 39:
                catName = "Домашние животные (Медикаменты)";
                break;
            case 40:
                catName = "Другое";
                break;
            case 41:
                catName = "Еда вне дома (Кофейня)";
                break;
            case 42:
                catName = "Еда вне дома (Ланч)";
                break;
            case 43:
                catName = "Еда вне дома (Ресторан)";
                break;
            case 44:
                catName = "Еда вне дома (Фастфуд)";
                break;
            case 45:
                catName = "Здоровье (Аптека)";
                break;
            case 46:
                catName = "Здоровье (Инвентарь)";
                break;
            case 47:
                catName = "Здоровье (Мед. услуги)";
                break;
            case 48:
                catName = "Здоровье (Спорт)";
                break;
            case 49:
                catName = "Здоровье (Страхование)";
                break;
            case 50:
                catName = "Красота (Косметика)";
                break;
            case 51:
                catName = "Красота (Салон красоты)";
                break;
            case 52:
                catName = "Мобильные услуги";
                break;
            case 53:
                catName = "Образование (Книги)";
                break;
            case 54:
                catName = "Образование (Услуги)";
                break;
            case 55:
                catName = "Одежда и обувь (Аксуссуары)";
                break;
            case 56:
                catName = "Одежда и обувь (Взрослая)";
                break;
            case 57:
                catName = "Одежда и обувь (Детская)";
                break;
            case 58:
                catName = "Подарки";
                break;
            case 59:
                catName = "Продукты питания";
                break;
            case 60:
                catName = "Путешествия (Аренда авто)";
                break;
            case 61:
                catName = "Путешествия (Билеты)";
                break;
            case 62:
                catName = "Путешествия (Визы)";
                break;
            case 63:
                catName = "Путешествия (Отель)";
                break;
            case 64:
                catName = "Путешествия (Страховка)";
                break;
            case 65:
                catName = "Путешествия (Сувениры)";
                break;
            case 66:
                catName = "Путешествия (Услуги)";
                break;
            case 67:
                catName = "Разлечения (Активный отдых)";
                break;
            case 68:
                catName = "Разлечения (Бар, клуб)";
                break;
            case 69:
                catName = "Разлечения (Игры, софт)";
                break;
            case 70:
                catName = "Разлечения (Кино, театр, концерты)";
                break;
            case 71:
                catName = "Разлечения (Книги)";
                break;
            case 72:
                catName = "Разлечения (Музеи)";
                break;
            case 73:
                catName = "Разлечения (Музыка, видео)";
                break;
            case 74:
                catName = "Разлечения (Пресса)";
                break;
            case 75:
                catName = "Разлечения (Хобби)";
                break;
            case 76:
                catName = "Техника (Бытовая химия)";
                break;
            case 77:
                catName = "Техника (Электроника)";
                break;
            case 78:
                catName = "Транспорт (Общ. транспорт)";
                break;
            case 79:
                catName = "Транспорт (Такси)";
                break;
        }
        return catName;
    }


}
