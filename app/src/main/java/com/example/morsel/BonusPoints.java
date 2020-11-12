package com.example.morsel;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class BonusPoints extends AppCompatActivity implements View.OnClickListener {
    int count, counter;
    int bonuspt = 0;
    SQLiteDatabase db;
    Button btnview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bonus_points);
        btnview = (Button) findViewById(R.id.bp1);
        btnview.setOnClickListener(this);
        count = getIntent().getIntExtra("count", count);
//        Log.i("count is : "+count, "the count is : "+count);
        counter = getIntent().getIntExtra("count 2", counter);
//        Log.i("count 2 is : "+counter, "the count 2 is : "+counter);
//        Toast.makeText(this,String.valueOf(count),Toast.LENGTH_LONG).show();
        db = openOrCreateDatabase("122BonuspDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS bonus(user VARCHAR, bonuspt NUMERIC);");


    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    public void onClick(View view) {
        int bpr, apr;
        int k=0;

        Cursor cpr = db.rawQuery("SELECT * FROM bonus WHERE user='" + "xyz" + "'", null);
        if (cpr.moveToFirst()) {
            // Displaying record if found
            bpr=cpr.getInt(1);
            TextView t = findViewById(R.id.tv);
            t.setText("Hello User, Congrats, Your bonus points is "+bpr);
        }
        else
        {  db.execSQL("Insert into bonus values('"+"xyz"+"','" + Integer.toString(0)+"');");
            TextView t = findViewById(R.id.tv);
            t.setText("Hello User, Please make your first step to earn more points" );

        }


//        bonuspt = bonuspt + count * 2;
//        db.execSQL("INSERT INTO bonus VALUES('" + "xyz" + "','" + Integer.toString(bonuspt) + "');");

//        db.execSQL("INSERT INTO bonus VALUES('" "xyz" +',' + Integer.toString(bonuspt) + "');");


//        Cursor c = db.rawQuery("SELECT * FROM bonus ORDER BY bonuspt DESC LIMIT 1");
//        Cursor cursor = db.rawQuery("SELECT SUM(" + DbHelper.bonuspt + ") as Total FROM " + DbHelper.bonus, null);
//        Cursor res = db.rawQuery("select (SUM(bonuspt)) AS finalbonus from " +bonuspt, null);
////        if (cur.moveToFirst()) {
//
//            int total = cursor.getInt(cursor.getColumnIndex("Total"));// get final total
//
//    }
    }
}