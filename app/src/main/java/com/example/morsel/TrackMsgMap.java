package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class TrackMsgMap extends AppCompatActivity {

    Button dm,dn,c;
    String slat,slon,dlat,dlon,pl,t;
    DatabaseReference ref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_msg_map);
        dm=findViewById(R.id.mapdnr);
        dn=findViewById(R.id.mapneedy);
        c=findViewById(R.id.comp);
        t=getIntent().getStringExtra("tripid");
        ref= FirebaseDatabase.getInstance().getReference().child("dnmapping").child(t);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                slat=snapshot.child("slat").getValue().toString();
                slon=snapshot.child("slon").getValue().toString();
                dlat=snapshot.child("dlat").getValue().toString();
                dlon=snapshot.child("dlon").getValue().toString();
                pl=snapshot.child("place").getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        dm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent;
                Uri location;
                String url = "http://maps.google.com/maps?q=loc:" + slat + "," + slon ;
                //String url=("geo:"+String.valueOf(sh_loc[0])+","+String.valueOf(sh_loc[1])+"?z=14");
                // Or map point based on latitude/longitude
                // location = Uri.parse("geo:37.422219,-122.08364?z=14");
                location = Uri.parse(url);
                // z param is zoom level
           /*
                1: World
                5: Landmass/continent
                10: City
                15: Streets
                20: Buildings
               */

                mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        dn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent;
                Uri location;
                String url = "http://maps.google.com/maps?q=loc:" + dlat + "," + dlon + " (" + pl + ")";
                //String url=("geo:"+String.valueOf(sh_loc[0])+","+String.valueOf(sh_loc[1])+"?z=14");
                // Or map point based on latitude/longitude
                // location = Uri.parse("geo:37.422219,-122.08364?z=14");
                location = Uri.parse(url);
                // z param is zoom level
           /*
                1: World
                5: Landmass/continent
                10: City
                15: Streets
                20: Buildings
               */

                mapIntent = new Intent(Intent.ACTION_VIEW, location);
                startActivity(mapIntent);
            }
        });

        c.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference ref1=FirebaseDatabase.getInstance().getReference().child("dnmapping").child(t);
                ref1.child("status").child("4");
            }
        });
    }
}