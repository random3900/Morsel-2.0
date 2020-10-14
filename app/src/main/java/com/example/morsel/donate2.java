package com.example.morsel;

import java.util.*;
import java.lang.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;

public class donate2 extends AppCompatActivity {

    private DatabaseReference mDatabase;
    TextView txt;
    String a,c;
    EditText ftype,fqty,flat,flon,fcity,farea;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate2);
        txt=findViewById(R.id.res);
        ftype=findViewById(R.id.etFoodType2);
        fqty=findViewById(R.id.etQty2);
        flat=findViewById(R.id.elat2);
        flon=findViewById(R.id.elon2);
        fcity=findViewById(R.id.ecity2);
        farea=findViewById(R.id.earea2);


    }

    public void onClickSubmit2(View view)
    {

        a=farea.getText().toString();
        c=fcity.getText().toString();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("hotspot list").child(c).child(a);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                ArrayList<Hotspot> hl=new ArrayList<>();
                Hotspot h;
                for(DataSnapshot d:snapshot.getChildren())
                {
                    int a=Integer.parseInt(d.child("avgnum").getValue().toString());
                    double lat=Double.parseDouble(d.child("lat").getValue().toString());
                    double lon=Double.parseDouble(d.child("long").getValue().toString());
                    String n=d.child("name").getValue().toString();
                    h=new Hotspot(a,lat,lon,n);
                    hl.add(h);
                }

                String t=" ";
                double ulat,ulon,hlat,hlon,dlat,dlon,a,c,r;
                ulat=Double.parseDouble(flat.getText().toString());
                ulon=Double.parseDouble(flon.getText().toString());
                ulat=Math.toRadians(ulat);
                ulon=Math.toRadians(ulon);
                for(Hotspot h1:hl)
                {
                    hlat=Math.toRadians(h1.getLat());
                    hlon=Math.toRadians(h1.getLon());
                    dlat=hlat-ulat;
                    dlon=hlon-ulon;
                    a = Math.pow(Math.sin(dlat / 2), 2)
                            + Math.cos(ulat) * Math.cos(hlat)
                            * Math.pow(Math.sin(dlon / 2),2);
                    c = 2 * Math.asin(Math.sqrt(a));
                    r=6371;
                    h1.setDist(c * r);

                }
                Collections.sort(hl);
                ArrayList<Hotspot> dnmap=new ArrayList<>();
                double avail,req;
                avail=Double.parseDouble(fqty.getText().toString());
                for(Hotspot h1:hl)
                {
                    req=h1.getAvgnum()*450;
                    if(req>=avail)
                    {
                        h1.setPackets((int) Math.floor(avail/450));
                        t+=h1.getName()+" "+h1.getPackets()+"\n";
                        dnmap.add(h1);
                        break;
                    }
                    else {
                        avail -= req;
                        h1.setPackets((int)h1.getAvgnum());
                        t+=h1.getName()+" "+h1.getPackets()+"\n";
                        dnmap.add(h1);
                    }
                }
                ArrayList<String> nl=new ArrayList<>();
                ArrayList<String> cl=new ArrayList<>();
                ArrayList<String> pl=new ArrayList<>();
                ArrayList<String> dl=new ArrayList<>();

                for(Hotspot h1:hl)
                {
                    nl.add(h1.getName());
                    cl.add(h1.getLat()+" "+h1.getLon());
                    pl.add(h1.getPackets()+"");
                    dl.add(h1.getDist()+"");
                }
                Intent i=new Intent(getApplicationContext(),MappedDetails.class);
                i.putStringArrayListExtra("nl",nl);
                i.putStringArrayListExtra("cl",cl);
                i.putStringArrayListExtra("pl",pl);
                i.putStringArrayListExtra("dl",dl);
                String ar=farea.getText().toString();
                String ci=fcity.getText().toString();
                i.putExtra("area",ar);
                i.putExtra("city",ci);
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Database error",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
