package com.example.morsel;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class vol_5 extends AppCompatActivity implements View.OnClickListener {

    SQLiteDatabase db;
    Button btn_pen,btn_del;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_pen =  findViewById(R.id.btn_pending);
        btn_del =  findViewById(R.id.btn_delivered);
        btn_pen.setOnClickListener(this);
        btn_pen.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==btn_pen.getId())
        {
            loadFragment(new vol_5_FirstFragment());
        }
        else if(view.getId()==btn_del.getId())
        {
            loadFragment(new vol_5_SecondFragment());
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



}
