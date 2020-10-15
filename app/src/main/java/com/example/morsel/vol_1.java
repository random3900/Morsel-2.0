package com.example.morsel;


import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ToggleButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.ArrayList;

public class vol_1 extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private DatabaseReference mDatabase,mDBw;
    ToggleButton tb_mode;
    SQLiteDatabase db;
    Button btn_ods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_1);

        tb_mode =  findViewById(R.id.tb_mode);
        btn_ods =  findViewById(R.id.btn_ord_status);
        btn_ods.setOnClickListener(this);
        tb_mode.setOnCheckedChangeListener(this);
    }

    public void onCheckedChanged(CompoundButton compoundButton, final boolean isChecked) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("volunteer");
        if(isChecked)
        {
            /*db = openOrCreateDatabase("VolDB", Context.MODE_PRIVATE, null);
            db.execSQL("CREATE TABLE IF NOT EXISTS vol(vol_id VARCHAR, location VARCHAR,weight VARCHAR);");
            db.execSQL("INSERT INTO vol VALUES('" + 1 + "','" + 0+","+0 + "','" + 0 + "');");
            db.execSQL("INSERT INTO vol VALUES('" + 2 + "','" + 0+","+0 + "','" + 0 + "');");
            db.execSQL("INSERT INTO vol VALUES('" + 3 + "','" + 0+","+0 + "','" + 0 + "');");*/
            loadFragment(new vol_1_FirstFragment());

        }
        else
        {
            loadFragment(new vol_1_SecondFragment());
        }
    }

     private void loadFragment(Fragment fragment) {
        // create a FragmentManager
        FragmentManager fm = getSupportFragmentManager();

        // create a FragmentTransaction to begin the transaction and replace the Fragment
        FragmentTransaction fragmentTransaction =
                fm.beginTransaction();

        // replace the FrameLayout with new Fragment
        fragmentTransaction.replace(R.id.frameLayout, fragment);

        //fragmentTransaction.add(R.id.firstFragment,fragment);
        fragmentTransaction.commit(); // save the changes
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==btn_ods.getId())
        {
            //Intent i3=new Intent(this,vol_5.class);
            //startActivity(i3);
        }
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
