package com.example.morsel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class donate extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Button submit, add, views, viewall, btnDatePicker, loc;
    EditText etFoodType, etQty, txtDate,e;
    SQLiteDatabase db;
    private int mYear, mMonth, mDay;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        submit = findViewById(R.id.submit);
        etFoodType = findViewById(R.id.etFoodType);
        etQty = findViewById(R.id.etQty);
        add = (Button) findViewById(R.id.btnAdd);
//        ;modify = (Button) findViewById(R.id.btnModify)
        viewall = (Button) findViewById(R.id.btnViewAll);
        views = (Button) findViewById(R.id.btnView);
        loc = (Button) findViewById(R.id.btnLocation);
        add.setOnClickListener(this);
//        modify.setOnClickListener(this);
        viewall.setOnClickListener(this);
        views.setOnClickListener(this);
        loc.setOnClickListener(this);
        e = (EditText) findViewById(R.id.e1);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        txtDate=(EditText)findViewById(R.id.in_date);
//
        btnDatePicker.setOnClickListener(this);
        db = openOrCreateDatabase("FoodsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS historys(tdate date,name VARCHAR,qty NUMERIC, location VARCHAR);");
    }

    public void onClickSubmit(View v){

            Intent i=new Intent(this, SecondScreen.class);
            startActivity(i);


    }

    @Override
    public void onClick(View view) {

        if (view == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog
                    (this, this, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if(view == loc)
        {
            String url;
            url = e.getText().toString();
            Intent mapIntent;
            Uri location;
            if(url.isEmpty()) {
                // Map point based on address
                location = Uri.parse("geo:0,0?q=Amrita Vishwa Vidyapeetham,+Ettimadai,+Coimbatore");
            }
            else {

                location = Uri.parse(url);
            }
            mapIntent = new Intent(Intent.ACTION_VIEW, location);
            startActivity(mapIntent);



        }

        if(view == views)
           {
               Cursor c = db.rawQuery("SELECT * FROM historys WHERE tdate='" + txtDate.getText() + "'", null);
               // Checking if no records found 
               if (c.getCount() == 0) {
                   showMessage("Error", "No records found");
                   return;
               }
               // Appending records to a string buffer 
               StringBuffer buffer = new StringBuffer();
               while (c.moveToNext())
               {
                   buffer.append("date: " + c.getString(0) + "\n");
                   buffer.append("FoodType: " + c.getString(1) + "\n");
                   buffer.append("Quantity: " + c.getString(2) + "\n\n");
                   buffer.append("Location: " + c.getString(3) + "\n\n");

               }
               // Displaying all records 
               showMessage("Today's History", buffer.toString());
           }

        if (view == add) {
            // Checking empty fields
            if (txtDate.getText().toString().trim().length() == 0 || etFoodType.getText().toString().trim().length() == 0 ||
                    etQty.getText().toString().trim().length() == 0||e.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter all values");
                return;
            }
            // Inserting record 
            db.execSQL("INSERT INTO historys VALUES('" + txtDate.getText() + "','" + etFoodType.getText() +
                    "','" + etQty.getText() + "','"+ e.getText()+ "');");
            showMessage("Success", "Record added");
            clearText();
        }

        if (view == viewall) {
            // Retrieving all records 
            Cursor c = db.rawQuery("SELECT * FROM historys", null);
            // Checking if no records found 
            if (c.getCount() == 0) {
                showMessage("Error", "No records found");
                return;
            }
            // Appending records to a string buffer 
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext())
            {
                buffer.append("date: " + c.getString(0) + "\n");
                buffer.append("FoodType: " + c.getString(1) + "\n");
                buffer.append("Quantity: " + c.getString(2) + "\n\n");
                buffer.append("Location: " + c.getString(3) + "\n\n");

            }
            // Displaying all records 
            showMessage("Donation History", buffer.toString());
        }

    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText(){

        txtDate.setText("");
        etFoodType.setText("");
        etQty.setText("");
        etFoodType.requestFocus();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int dayOfMonth, int monthOfYear, int year) {
        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1)
                + "-" + year);
    }
}
