package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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
    ArrayList<String> nl=new ArrayList<>();
    ArrayList<String> cl=new ArrayList<>();
    ArrayList<String> pl=new ArrayList<>();
    ArrayList<String> dl=new ArrayList<>();
    TextView tn,ta,tc;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapped_details);
        mauth=FirebaseAuth.getInstance();
        FirebaseUser u=mauth.getCurrentUser();
        nl=(ArrayList<String>)getIntent().getStringArrayListExtra("nl");
        cl=(ArrayList<String>)getIntent().getStringArrayListExtra("cl");
        pl=(ArrayList<String>)getIntent().getStringArrayListExtra("pl");
       dl=(ArrayList<String>)getIntent().getStringArrayListExtra("dl");
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
        hm=new HashMap<String, String>();
        hm.put("name","Place");
        hm.put("pack","Pkts");
        hm.put("addr","\n");
        lvl.add(hm);
        for(int i=0;i<nl.size();i++)
        {
            hm=new HashMap<String, String>();
            hm.put("name",nl.get(i));
            hm.put("pack",pl.get(i));
            hm.put("addr"," ");
            lvl.add(hm);
        }
        String[] entry=new String[]{"name","pack","addr"};
        SimpleAdapter adap=new SimpleAdapter(this,lvl,R.layout.item_hotspot,entry,new int[]{R.id.hotspot_name,R.id.hotspot_avg_num,R.id.hotspot_address});
        lv.setAdapter(adap);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i==0)
                    return;
                String url="geo:"+cl.get(i-1)+"?z=17";
                Uri location;
                location = Uri.parse(url);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

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