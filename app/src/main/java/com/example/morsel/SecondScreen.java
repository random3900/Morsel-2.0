package com.example.morsel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class SecondScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
      MenuInflater inflater = getMenuInflater();
       inflater.inflate(R.menu.menu_file, menu);
        return true;
    }


    public void onClickContribute(View V){
        Intent i = new Intent(this, donate2.class);
        startActivity(i);
    }
    public void onClickVolunteer(View V){
//        Intent i = new Intent(this, volunteer.class);
//        startActivity(i);
    }

    public  void onClickHotspot(View v){

    }

}
