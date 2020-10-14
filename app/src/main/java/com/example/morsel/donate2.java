package com.example.morsel;

import java.util.*;
import java.lang.*;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;


public class donate2 extends AppCompatActivity {

    private DatabaseReference mDatabase,mDBw,mDB1;
    FirebaseAuth mauth;
    TextView txt;
    String a,c;
    EditText ftype,fqty,flat,flon;
    Spinner fcity,farea;
    ArrayList<String> cities;
    ArrayList<String> areas;
    ArrayAdapter<String> adap1;
    ArrayAdapter<String> adap2;
    private int mYear, mMonth, mDay;

    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate2);
        mauth=FirebaseAuth.getInstance();
        db = openOrCreateDatabase("FoodsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS historydet(email VARCHAR,tdate date,fname VARCHAR,qty NUMERIC, location VARCHAR);");

        ftype=findViewById(R.id.etFoodType2);
        fqty=findViewById(R.id.etQty2);
        flat=findViewById(R.id.elat2);
        flon=findViewById(R.id.elon2);
        fcity=(Spinner)findViewById(R.id.ecity2);
        farea=(Spinner)findViewById(R.id.earea2);
        mDB1=FirebaseDatabase.getInstance().getReference().child("hotspot list");

        mDB1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cities =new ArrayList<>();
                for(DataSnapshot snap:snapshot.getChildren())
                {
                    cities.add(snap.getKey().toString());
                }
                adap1=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,cities);
                fcity.setAdapter(adap1);
                fcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        c=cities.get(i);
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("hotspot list").child(c);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                areas=new ArrayList<>();
                                for(DataSnapshot s:snapshot.getChildren()){
                                    areas.add(s.getKey().toString());
                                }
                                adap2=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,areas);
                                farea.setAdapter(adap2);
                                farea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        a=areas.get(i);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void onClickSubmit2(View view)
    {
        final Calendar c1 = Calendar.getInstance();
        mYear = c1.get(Calendar.YEAR);
        mMonth = c1.get(Calendar.MONTH);
        mDay = c1.get(Calendar.DAY_OF_MONTH);
        if (fqty.getText().toString().trim().length() == 0 || ftype.getText().toString().trim().length() == 0 ||
                a.trim().length() == 0|| c.trim().length() == 0 || flat.getText().toString().trim().length() == 0 ||
                flon.getText().toString().trim().length()==0) {
            showMessage("Error", "Please enter all values");
            return;
        }


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
                    double lon=Double.parseDouble(d.child("lon").getValue().toString());
                    String n=d.child("name").getValue().toString();
                    h=new Hotspot(a,lat,lon,n);
                    hl.add(h);
                }

                String t=" ";
                double ulat,ulon,hlat,hlon,dlat,dlon,a1,c1,r,ulat1,ulon1;
                ulat1=Double.parseDouble(flat.getText().toString());
                ulon1=Double.parseDouble(flon.getText().toString());

                if(hl.size()==0)
                {
                    showMessage("Oops","No requirement. Thank you for your initiative");
                    return;
                }
                String d=mYear+"/"+mMonth+"/"+mDay;


                ulat=Math.toRadians(ulat1);
                ulon=Math.toRadians(ulon1);
                for(Hotspot h1:hl)
                {
                    hlat=Math.toRadians(h1.getLat());
                    hlon=Math.toRadians(h1.getLon());
                    dlat=hlat-ulat;
                    dlon=hlon-ulon;
                    a1 = Math.pow(Math.sin(dlat / 2), 2)
                            + Math.cos(ulat) * Math.cos(hlat)
                            * Math.pow(Math.sin(dlon / 2),2);
                    c1 = 2 * Math.asin(Math.sqrt(a1));
                    r=6371;
                    h1.setDist(c1 * r);

                }



                Collections.sort(hl);
                ArrayList<Hotspot> dnmap=new ArrayList<>();
                double avail,req;
                avail=Double.parseDouble(fqty.getText().toString());
                for(Hotspot h1:hl)
                {
                    req=h1.getAvgnum()*450;
                    String loc="("+h1.getLat()+","+h1.getLon()+")";
                    FirebaseUser u=mauth.getCurrentUser();
                    db.execSQL("INSERT INTO historydet VALUES('"+u.getEmail()+"','" + d + "','" + ftype.getText() +
                            "','" + fqty.getText() + "','"+ loc+ "');");
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

                mDBw=FirebaseDatabase.getInstance().getReference().child("dnmapping");
                int i1=1;
                for(Hotspot h1:hl)
                {
                    nl.add(h1.getName());
                    cl.add(h1.getLat()+" "+h1.getLon());
                    pl.add(h1.getPackets()+"");
                    dl.add(String.format("%.20f",h1.getDist()));
                    mDBw.child("Trip"+i1).child("slat").setValue(ulat1);
                    mDBw.child("Trip"+i1).child("slon").setValue(ulon1);
                    mDBw.child("Trip"+i1).child("dlat").setValue(h1.getLat());
                    mDBw.child("Trip"+i1).child("dlon").setValue(h1.getLon());
                    mDBw.child("Trip"+i1).child("dist").setValue(h1.getDist());
                    mDBw.child("Trip"+i1).child("packets").setValue(h1.getPackets());
                    mDBw.child("Trip"+i1).child("area").setValue(a);
                    mDBw.child("Trip"+i1).child("city").setValue(c);
                    i1++;
                }


                Intent i=new Intent(getApplicationContext(),MappedDetails.class);
                i.putStringArrayListExtra("nl",nl);
                i.putStringArrayListExtra("cl",cl);
                i.putStringArrayListExtra("pl",pl);
                i.putStringArrayListExtra("dl",dl);

                i.putExtra("area",a);
                i.putExtra("city",c);
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Database error",Toast.LENGTH_SHORT).show();
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
