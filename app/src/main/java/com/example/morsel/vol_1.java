package com.example.morsel;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class vol_1 extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {

    private DatabaseReference mDatabase,mDBw;
    ToggleButton tb_mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_1);

        tb_mode =  findViewById(R.id.tb_mode);
        //btn_ods =  findViewById(R.id.btn_ord_status);
        //btn_ods.setOnClickListener(this);
        tb_mode.setOnCheckedChangeListener(this);
    }

    public void onCheckedChanged(CompoundButton compoundButton, final boolean isChecked) {
        mDatabase = FirebaseDatabase.getInstance().getReference().child("volunteer");
        if(isChecked)
        {
            loadFragment(new vol_1_FirstFragment());
        }
        else
        {
            stopService(new Intent(this,LocationTrackerService.class));
            Toast.makeText(this,"Location sharing stopped", Toast.LENGTH_SHORT).show();
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

            case R.id.bonpts:
                Intent i3=new Intent(this,BonusPoints.class);
                startActivity(i3);
                break;

        }
        return true;
    }
}
