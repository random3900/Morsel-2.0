package com.example.morsel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class MappedDetails extends AppCompatActivity {

    String area,city;
    TextView ta,tc;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapped_details);
        ArrayList<String> nl=(ArrayList<String>)getIntent().getStringArrayListExtra("nl");
        ArrayList<String> cl=(ArrayList<String>)getIntent().getStringArrayListExtra("cl");
        ArrayList<String> pl=(ArrayList<String>)getIntent().getStringArrayListExtra("pl");
        ArrayList<String> dl=(ArrayList<String>)getIntent().getStringArrayListExtra("dl");
        area=getIntent().getStringExtra("area");
        city=getIntent().getStringExtra("city");
        ta=findViewById(R.id.areadet);
        ta.setText(area);
        tc=findViewById(R.id.citydet);
        tc.setText(city);
        lv=findViewById(R.id.mlv);
        HashMap<String,String> hm;
        ArrayList<HashMap<String,String>> lvl=new ArrayList<>();
        for(int i=0;i<nl.size();i++)
        {
            hm=new HashMap<String, String>();
            hm.put("name",nl.get(0));
            hm.put("coord",cl.get(0));
            hm.put("dist",dl.get(0)+" Km");
            hm.put("pack",pl.get(0));
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
}