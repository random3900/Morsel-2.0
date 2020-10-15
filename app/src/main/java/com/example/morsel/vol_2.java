package com.example.morsel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class vol_2 extends AppCompatActivity implements
        AdapterView.OnItemClickListener {

    ListView lv_vol;
    EditText et_dis;
    SQLiteDatabase db;
    Integer id;
    private DatabaseReference mDatabase, mdb,mdb1;
    int reg_vol_id=1;
    double lat1d;
    //double lat1d = 12.9611159;
    double lon1d;
    double lat2d;
    double lon2d;
// Longitude of customer who needs a cab.
    //double lon1d = 77.6362214;
     double pi = 3.14159265358979323846;
      double earth_radius = 6371.0;
    //Button add;
    //Button bill;
    int i=0;
    List<String> place = new ArrayList<>();
    //StringBuffer food_item = new StringBuffer();
    List<String> distance = new ArrayList<>();
    //StringBuffer price = new StringBuffer();
    //List<Integer> ps = new ArrayList<>();
    //List<Integer> cs = new ArrayList<>();
    //int [] ps={0,0,0,0};
    //int [] cs={0,0,0,0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_2);
        lv_vol=findViewById(R.id.lv_vol);
        et_dis=findViewById(R.id.et_dis);
        Bundle b=getIntent().getExtras();
        id=Integer.parseInt(b.getString("id_vol"));

        //add=findViewById(R.id.add);
        //bill=findViewById(R.id.bill);
        //count.setVisibility(View.INVISIBLE);

        /*db = openOrCreateDatabase("VolDB", Context.MODE_PRIVATE, null);
        //db.execSQL("CREATE TABLE IF NOT EXISTS vol(vol_id VARCHAR, location VARCHAR,weight VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS orders(don_location VARCHAR,don_name VARCHAR,don_weight VARCHAR,dest_name VARCHAR,vol_id VARCHAR,order_status BOOLEAN,delivery_status BOOLEAN);");
        //db.execSQL("INSERT INTO orders VALUES('" +  + "','" + "A2B1" + "','" + 0 + "', '" + "Temple1" + "','" + 0 + "','" + false + "');");
        db.execSQL("INSERT INTO orders VALUES('" + "11.92893,78.27699" + "','" + "A2B2" + "','" + 10 + "', '" + "Temple2" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" + "11.8856167,78.4240911" + "','" + "A2B3" + "','" + 20 + "', '" + "Temple3" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" + "12.3191841,78.5072391" + "','" + "A2B4" + "','" + 10 + "', '" + "Temple4" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" + "13.807778,76.714444" + "','" + "A2B5" + "','" + 30 + "', '" + "Temple5" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" + "13.4692815,-9.436036" + "','" + "A2B6" + "','" + 20 + "', '" + "Temple6" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" + "14.0894797,77.18671" + "','" + "A2B7" + "','" + 30 + "', '" + "Temple7" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" + "13.038056,76.613889" + "','" + "A2B8" + "','" + 20 + "', '" + "Temple8" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" + "14.1225,78.143333" + "','" + "A2B9" + "','" + 40 + "', '" + "Temple9" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13.1229599,77.2701202" + "','" + "A2B10" + "','" + 30 + "', '" + "Temple10" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "12.2559432,76.1048927" + "','" + "A2B11" + "','" + 50 + "', '" + "Temple11" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "12.240382,77.972413" + "','" + "A2B12" + "','" + 20 + "', '" + "Temple12" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13.2411022,77.238335" + "','" + "A2B13" + "','" + 20 + "', '" + "Temple13" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13.1302756,77.2397222" + "','" + "A2B14" + "','" + 70 + "', '" + "Temple14" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13.008769,77.1056711" + "','" + "A2B15" + "','" + 60 + "', '" + "Temple15" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13.1489345,77.8422408" + "','" + "A2B16" + "','" + 50 + "', '" + "Temple16" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13,76" + "','" + "A2B17" + "','" + 40 + "', '" + "Temple17" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "11.999447,-9.742744" + "','" + "A2B18" + "','" + 10 + "', '" + "Temple18" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "12.966,77.463" + "','" + "A2B19" + "','" + 60 + "', '" + "Temple19" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "12.366037,78.179118" + "','" + "A2B20" + "','" + 70 + "', '" + "Temple20" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "14.180238,-5.920898" + "','" + "A2B21" + "','" + 60 + "', '" + "Temple21" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13.0033946,77.3877505" + "','" + "A2B22" + "','" + 50 + "', '" + "Temple22" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "12.228056,76.915833" + "','" + "A2B23" + "','" + 30 + "', '" + "Temple23" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "14.133333,77.433333" + "','" + "A2B24" + "','" + 20 + "', '" + "Temple24" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "55.033,78.112" + "','" + "A2B25" + "','" + 10 + "', '" + "Temple25" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13.121111,-9.831111" + "','" + "A2B26" + "','" + 50 + "', '" + "Temple26" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "11.802,-9.442" + "','" + "A2B27" + "','" + 40 + "', '" + "Temple27" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "14.374208,78.371639" + "','" + "A2B28" + "','" + 30 + "', '" + "Temple28" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13.74412,76.11167" + "','" + "A2B29" + "','" + 20 + "', '" + "Temple29" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "13.761389,76.2875" + "','" + "A2B30" + "','" + 10 + "', '" + "Temple30" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +  "14.080556,77.361944" + "','" + "A2B31" + "','" + 10 + "', '" + "Temple31" + "','" + 0 + "','" + false +"','" + false +"');");
        db.execSQL("INSERT INTO orders VALUES('" +   "12.833502,78.122366" + "','" + "A2B32" + "','" + 10 + "', '" + "Temple32" + "','" + 0 + "','" + false +"','" + false +"');");
    Cursor c1 = db.rawQuery("SELECT * FROM vol WHERE vol_id ='" + String.valueOf(reg_vol_id) + "'", null);
       //Cursor c1 = db.rawQuery("SELECT * FROM vol WHERE vol_id ='" + String.valueOf(reg_vol_id) + "'", null);
       Cursor c = db.rawQuery("SELECT * FROM orders", null);
*/


        //mdb = FirebaseDatabase.getInstance().getReference().child("volunteer");
        mdb = FirebaseDatabase.getInstance().getReference().child("volunteer").child("vol"+(id));
        mdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if(snapshot.child("vol_id").getValue()!=null&&snapshot.child("vol_loc").getValue()!=null) {
                    int ids = Integer.parseInt(snapshot.child("vol_id").getValue().toString());
                    if (ids == (id)) {
                        String[] loc2 = (snapshot.child("vol_loc").getValue().toString()).split(",");
                        lat1d = Double.parseDouble(loc2[0]);
                        lon1d = Double.parseDouble(loc2[1]);
                        //Toast.makeText(getApplicationContext(), lat1d+" "+lon1d, Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });

        mdb1=FirebaseDatabase.getInstance().getReference().child("dnmapping");
        mdb1.addValueEventListener(new ValueEventListener() {
        public void onDataChange(DataSnapshot snapshot) {
            int i1=1;
        for(DataSnapshot ds:snapshot.getChildren())
        {
            lat2d = Double.parseDouble(ds.child("dlat").getValue().toString());
            lon2d = Double.parseDouble(ds.child("dlon").getValue().toString());
            //Toast.makeText(getApplicationContext(), lat2d+" "+lon2d, Toast.LENGTH_SHORT).show();
            double x=distanceEarth(lat2d, lon2d);
            if (x<=50.0000)
            {
                place.add((ds.child("area").getValue().toString())+" "+lat2d+" "+lon2d);
                distance.add(String.valueOf(x));
                /*if(!place.contains((ds.child("area")).toString()))
                {

                }*/
                /*if(!distance.contains(String.valueOf(x)))
                {
                    //Toast.makeText(getApplicationContext(),place+" ", Toast.LENGTH_SHORT).show();

                }
                */
                i1++;
            }
        }
            //Toast.makeText(getApplicationContext(),place+" ", Toast.LENGTH_SHORT).show();
            //Toast.makeText(getApplicationContext(),distance+" ", Toast.LENGTH_SHORT).show();
            enter(place,distance);
        }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
            });


    }

    public double degtorad(double deg)
    {
        return ( deg * pi / 180);
    }

    // Function to calculate distance
// between 2 given locations
// using Great Circle Distance Formula.
    public double distanceEarth(double lat2d, double lon2d)
    {
        double lat1, lon1, lat2, lon2,
                delta_lon, central_ang;

        lat1 = degtorad(lat1d);
        lon1 = degtorad(lon1d);
        lat2 = degtorad(lat2d);
        lon2 = degtorad(lon2d);

        delta_lon = lon2 - lon1;

        // great circle distance formula.
        central_ang = Math.acos( Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(delta_lon));

        return (earth_radius * central_ang);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long idp) {
        Intent i2 = new Intent(this,vol_3.class);
        i2.putExtra("place", place.get(position));
        i2.putExtra("distance", distance.get(position));
        i2.putExtra("id_vol",String.valueOf(id));
        startActivity(i2);
    }

    public void enter(List<String> place,List<String> distance)
    {
        //Toast.makeText(getApplicationContext(),place+" ", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getApplicationContext(),distance+" ", Toast.LENGTH_SHORT).show();
        HashMap<String,String> content ;
        List<Map<String, String>> messages = new ArrayList<>();
        //Toast.makeText(getApplicationContext(),distance.size()+"", Toast.LENGTH_SHORT).show();
        for(int i = 0 ; i < distance.size(); i++) {
            content = new HashMap<String, String>();
            content.put("pl", place.get(i));
            content.put("di", distance.get(i));
            //Toast.makeText(getApplicationContext(),"added", Toast.LENGTH_SHORT).show();
            messages.add(content);
        }

        String[] entry = new String[] {"pl", "di"};
        final SimpleAdapter adapter = new SimpleAdapter(this, messages,
                android.R.layout.simple_list_item_2,
                entry,
                new int[] {android.R.id.text1,
                        android.R.id.text2,
                });
        lv_vol.setAdapter(adapter);
        if(distance.size()==0)
        {
            Toast.makeText(getApplicationContext(),"No hotspots nearby in the surrounding of 50 kms", Toast.LENGTH_LONG).show();
        }

        et_dis.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                Log.d("Test","on text changed");
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // Toast.makeText(getApplicationContext(), "before text change", Toast.LENGTH_LONG).show();
                Log.d("test","before text changed");
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // Toast.makeText(getApplicationContext(), "after text change", Toast.LENGTH_LONG).show();
                Log.d("Test","after text changed");
                adapter.getFilter().filter(arg0);
            }
        });
        lv_vol.setOnItemClickListener(this);
    }



    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
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
