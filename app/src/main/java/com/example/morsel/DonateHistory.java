package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;

import java.util.ArrayList;
import java.util.Calendar;

public class DonateHistory extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    GridView gv;
    EditText e;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_history);
        db = openOrCreateDatabase("FoodsDB", Context.MODE_PRIVATE, null);
        gv=findViewById(R.id.gv);
        e=findViewById(R.id.edate);

    }

    public void seldate(View v){
        final Calendar c1 = Calendar.getInstance();
        int mYear = c1.get(Calendar.YEAR);
        int mMonth = c1.get(Calendar.MONTH);
        int mDay = c1.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog
                (this, this, mYear, mMonth, mDay);
        datePickerDialog.show();

    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

        String d=i2+"/"+i1+"/"+i;
        e.setText(d);
        Cursor c=db.rawQuery("select fname,qty,location from historyd where tdate="+d,null);
        ArrayList<String> q=new ArrayList<>();
        if (c.getCount() == 0) {
            showMessage("Error", "No records found");
            return;
        }
        q.add("Food Type");
        q.add("Food Qty");
        q.add("Location");
        while (c.moveToNext())
        {
            q.add(c.getString(0));
            q.add(c.getString(1));
            q.add(c.getString(2));
        }
        ArrayAdapter<String> adap=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,q);
        gv.setAdapter(adap);

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_file, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.dhist:
                Intent i=new Intent(this,DonateHistory.class);
                startActivity(i);
                break;

            case R.id.mlog:
                Intent i1=new Intent(this,MainActivity.class);
                startActivity(i1);
                break;
        }
        return true;
    }
}