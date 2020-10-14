package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;

public class MappedDetails extends AppCompatActivity {

    String area,city;
    private FirebaseAuth mauth;
    TextView tn,ta,tc;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapped_details);
        mauth=FirebaseAuth.getInstance();
        FirebaseUser u=mauth.getCurrentUser();
        ArrayList<String> nl=(ArrayList<String>)getIntent().getStringArrayListExtra("nl");
        ArrayList<String> cl=(ArrayList<String>)getIntent().getStringArrayListExtra("cl");
        ArrayList<String> pl=(ArrayList<String>)getIntent().getStringArrayListExtra("pl");
        ArrayList<String> dl=(ArrayList<String>)getIntent().getStringArrayListExtra("dl");
        area=getIntent().getStringExtra("area");
        city=getIntent().getStringExtra("city");
        tn=findViewById(R.id.donorname);
        tn.setText("Name: "+u.getEmail());
        ta=findViewById(R.id.areadet);
        ta.setText("Area: "+area);
        tc=findViewById(R.id.citydet);
        tc.setText("City: "+city);
        lv=findViewById(R.id.mlv);
        HashMap<String,String> hm;
        ArrayList<HashMap<String,String>> lvl=new ArrayList<>();
        for(int i=0;i<nl.size();i++)
        {
            hm=new HashMap<String, String>();
            hm.put("name",nl.get(i));
            hm.put("coord",cl.get(i));
            hm.put("dist",dl.get(i)+" Km");
            hm.put("pack",pl.get(i)+" packets");
            lvl.add(hm);
        }
        String[] entry=new String[]{"name","coord","pack","dist"};
        SimpleAdapter adap=new SimpleAdapter(this,lvl,R.layout.mapdetlv,entry,new int[]{R.id.t1,R.id.t2,R.id.t3,R.id.t4});
        lv.setAdapter(adap);

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