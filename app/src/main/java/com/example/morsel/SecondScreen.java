package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SecondScreen extends AppCompatActivity {

    String email,name;
    private FirebaseAuth mauth;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);
        mauth=FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();
        t=findViewById(R.id.huser);
        t.setText("Hello User: "+user.getEmail());
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


    public void onClickContribute(View V){
        Intent i = new Intent(this, donate2.class);
        startActivity(i);
    }
    public void onClickVolunteer(View V){
        Intent i = new Intent(this, vol_1.class);
        startActivity(i);
    }

    public  void onClickHotspot(View v){

    }

}
