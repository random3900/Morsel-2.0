package com.example.morsel;

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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.Calendar;

public class DonateHistory extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    GridView gv;
    EditText e;
    SQLiteDatabase db;
    FirebaseAuth mauth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate_history);
        db = openOrCreateDatabase("FoodsDB", Context.MODE_PRIVATE, null);
        gv=findViewById(R.id.gv);
        e=findViewById(R.id.edate);
        mauth=FirebaseAuth.getInstance();
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

        String d=i+"/"+(i1+1)+"/"+i2;
        e.setText(d);
        FirebaseUser u=mauth.getCurrentUser();
        Cursor c=db.rawQuery("select fname,qty,location from historydet where tdate='"+d+"' and email='"+u.getEmail()+"';",null);
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

        Cursor ci = db.rawQuery("select * from historydet",null);
        showMessage("Error", "No records found");

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

            case R.id.abtus:
                Intent i2=new Intent(this,aboutus.class);
                startActivity(i2);
                break;
        }
        return true;
    }
}