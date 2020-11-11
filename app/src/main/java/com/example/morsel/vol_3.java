package com.example.morsel;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class vol_3 extends AppCompatActivity implements View.OnClickListener {
    TextView tv_sn,tv_dn,tv_sd,tv_sdd,tv_wt,tv_wtc,tv_acc;
    Button btn_va;
    SQLiteDatabase db;
    String distance,place,w;
    double la,lo;
    int reg_vol_id=1;
    private DatabaseReference mDatabase, mdb,mdb1;
    int w1,w2;
    int id;
    SQLiteDatabase bb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_3);
        tv_sn=findViewById(R.id.tv_src_name);
        tv_dn=findViewById(R.id.tv_dst_name);
        tv_sd=findViewById(R.id.tv_src_dist);
        tv_sdd=findViewById(R.id.tv_src_dst_dist);
        tv_wt=findViewById(R.id.tv_weight);
        tv_wtc=findViewById(R.id.tv_weight_comp);
        tv_acc=findViewById(R.id.tv_acceppted);
        btn_va=findViewById(R.id.btn_vol_accept);
        Bundle b=getIntent().getExtras();
        place=b.getString("place");
        distance=b.getString("distance");
        tv_sd.setText(tv_sd.getText()+" : "+distance+" Kms");
        String s[]=place.split(" ");
        la=Double.parseDouble(s[1]);
        lo=Double.parseDouble(s[2]);
        id=Integer.parseInt(b.getString("id_vol"));
        btn_va.setOnClickListener(this);
        bb=openOrCreateDatabase("BonuspDB", Context.MODE_PRIVATE, null);
        bb.execSQL("CREATE TABLE IF NOT EXISTS bonus(user VARCHAR, bonuspt NUMERIC);");
        mdb = FirebaseDatabase.getInstance().getReference().child("volunteer").child("vol"+(id));
        mdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.child("vol_id").getValue()!=null && snapshot.child("vol_weight").getValue()!=null) {
                    int ids = Integer.parseInt(snapshot.child("vol_id").getValue().toString());
                    if (ids == (id)) {
                        w1 = Integer.parseInt(snapshot.child("vol_weight").getValue().toString());
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
                int i1 = 1;
                for (DataSnapshot ds : snapshot.getChildren()) {
                    double lat2d = Double.parseDouble(ds.child("dlat").getValue().toString());
                    double lon2d = Double.parseDouble(ds.child("dlon").getValue().toString());
                    if(lat2d==la && lon2d==lo) {
                        tv_sn.setText(tv_sn.getText()+" : "+ds.child("area").getValue().toString() + " , " + ds.child("city").getValue().toString());
                        tv_dn.setText(tv_dn.getText()+" : "+ds.child("slat").getValue().toString() + " , " + ds.child("slon").getValue().toString());
                        tv_sdd.setText(tv_sdd.getText()+" : "+ds.child("dist").getValue().toString()+" Kms");
                        w2=Integer.parseInt(ds.child("packets").getValue().toString());
                        tv_wt.setText(tv_wt.getText()+" : "+w2+" packets");
                        w=String.valueOf(Math.abs(w2-w1));
                        if(w1>=w2)
                        {
                            tv_wtc.setText("The food weight is "+w+" packets\nlower than your expected level");
                        }
                        else
                        {
                            tv_wtc.setText("The food weight is "+w+ " packets\ngreater than your expected level");
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });


    }


   /* db = openOrCreateDatabase("VolDB", Context.MODE_PRIVATE, null);
        //db.execSQL("CREATE TABLE IF NOT EXISTS vol(vol_id VARCHAR, location VARCHAR,weight VARCHAR);");
        db.execSQL("CREATE TABLE IF NOT EXISTS orders(don_location VARCHAR,don_name VARCHAR,don_weight VARCHAR,dest_name VARCHAR,vol_id VARCHAR,order_status BOOLEAN,delivery_status BOOLEAN);");

        Cursor c1 = db.rawQuery("SELECT * FROM vol WHERE vol_id ='" + String.valueOf(reg_vol_id) + "'", null);
        //Cursor c1 = db.rawQuery("SELECT * FROM vol WHERE vol_id='" + String.valueOf(reg_vol_id) + "'", null);
        if (c1.moveToFirst()) {
            w1=Integer.parseInt(c1.getString(2));
            Toast.makeText(this,w1, Toast.LENGTH_LONG).show();
            Log.d("getApplicationContext()","updated");
        }
        Cursor c = db.rawQuery("SELECT * FROM orders WHERE don_name='" + place + "'", null);
        if (c.moveToFirst()) {
            tv_sn.setText(tv_sn.getText()+" "+c.getString(1));
            tv_wt.setText(tv_wt.getText()+" "+c.getString(2)+" kgs");
            w2=Integer.parseInt(c.getString(2));
            w=String.valueOf(Math.abs(w2-w1));
            if(w1>=w2)
            {
                tv_wtc.setText("The food weight is "+w+" kgs\nlower than your expected level");
            }
            else
            {
                tv_wtc.setText("The food weight is "+w+ " kgs\ngreater than your expected level");
            }
            tv_dn.setText(tv_dn.getText()+" "+c.getString(3));
            tv_sd.setText(tv_sd.getText()+" "+distance+"Kms");
            tv_sdd.setText(tv_sdd.getText()+" "+String.valueOf(20)+"Kms");
        }

    }*/

    @Override
    public void onClick(View view) {
        if(view.getId()==btn_va.getId()) {
            /*Cursor c = db.rawQuery("SELECT * FROM orders WHERE vol_id ='" + String.valueOf(reg_vol_id) + "'", null);
            if (c.moveToFirst()) {
                // Modifying record if found 
                db.execSQL("UPDATE menu SET order_status='" + true + "' WHERE vol_id='" + String.valueOf(reg_vol_id) + "'");
            }*/
            int bpr;
            Cursor cpr = bb.rawQuery("SELECT * FROM bonus WHERE user='" + "xyz" + "'", null);
//        if (cpr.moveToFirst()) {
            if (cpr.moveToFirst()) {
                // Displaying record if found 
                bpr=cpr.getInt(1);
                bb.execSQL("UPDATE bonus SET bonuspt='" + (Integer.toString(bpr+2) )+
                        "' WHERE user='" + "xyz" + "'");
//                Toast.makeText(this, String.valueOf(bpr), Toast.LENGTH_LONG).show();
            }
            tv_acc.setText("Thank you for accepting the order\nDon't turn off your location");
//            counter=counter+1;
        }
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

//            case R.id.bonpts:
//                Intent i3=new Intent(this,BonusPoints.class);
//                i3.putExtra("count",counter);
//                Log.i("count is : "+counter, "the count is : "+counter);
//                startActivity(i3);
//                break;
        }
        return true;
    }
}