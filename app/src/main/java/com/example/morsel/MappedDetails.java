package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
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
import java.util.HashMap;

public class MappedDetails extends AppCompatActivity {

    String area,city,msg;
    int sz;
    private FirebaseAuth mauth;
    DatabaseReference mdb;
    ArrayList<String> nl=new ArrayList<String>();
    ArrayList<String> cl=new ArrayList<String>();
    ArrayList<String> pl=new ArrayList<String>();
    ArrayList<String> dl=new ArrayList<String>();
    ArrayList<String> phl=new ArrayList<String>();
    TextView tn,ta,tc;
    ListView lv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mapped_details);
        mauth=FirebaseAuth.getInstance();
        FirebaseUser u=mauth.getCurrentUser();
        nl=getIntent().getStringArrayListExtra("nl");
        cl=getIntent().getStringArrayListExtra("cl");
        pl=getIntent().getStringArrayListExtra("pl");
        dl=getIntent().getStringArrayListExtra("dl");
        area=getIntent().getStringExtra("area");
        city=getIntent().getStringExtra("city");
        sz=getIntent().getIntExtra("size",0);
        tn=findViewById(R.id.nameval);
        tn.setText(u.getEmail());
        ta=findViewById(R.id.areaval);
        ta.setText(area);
        tc=findViewById(R.id.cityval);
        tc.setText(city);
        lv=findViewById(R.id.mlv);

        HashMap<String,String> hm;
        ArrayList<HashMap<String,String>> lvl=new ArrayList<>();
        hm=new HashMap<String, String>();
        hm.put("name","Place");
        hm.put("pack","Pkts");
        hm.put("addr","\n");
        lvl.add(hm);
        for(int i=0;i<sz;i++)
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
        mdb= FirebaseDatabase.getInstance().getReference().child("volunteers-by-place").child(city).child(area);
        mdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d:snapshot.getChildren()){
                    String no=d.child("Phone").getValue().toString();
                    phl.add(no);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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

    public void notifyVols(View v)
    {
        msg="Food Packets to be delivered. Can you please Volunteer?\n" +
                "City:"+city+"\n"+
                "Area:"+area+"\n";
        for(int i=0;i<sz;i++)
        {
            msg+="Place:"+nl.get(i)+"\n"+
                    "Coordinates:"+cl.get(i);
        }
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            // if (ActivityCompat.shouldShowRequestPermissionRationale(this,
            //       Manifest.permission.SEND_SMS)) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.SEND_SMS},
                    111);
            return;
        } else {
            // Permission already granted
            // }
        }
        SmsManager sms=SmsManager.getDefault();
        for(String i :phl)
        {
            Intent intent=new Intent(this,MappedDetails.class);
            PendingIntent pi=PendingIntent.getActivity(this, 0, intent,0);
            //Get the SmsManager instance and call the sendTextMessage method to send message


            sms.sendTextMessage(i, null, msg, pi,null);
            Toast.makeText(getApplicationContext(),"Message sent to : "+i,Toast.LENGTH_SHORT).show();
        }
    }
}