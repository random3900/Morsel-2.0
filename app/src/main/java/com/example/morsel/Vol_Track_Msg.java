package com.example.morsel;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.net.URLEncoder;


public class Vol_Track_Msg extends Fragment {


    Button nd,nn;
    MaterialCardView cp,cc;
    DatabaseReference ref;
    String t,phno,slat,slon,dlat,dlon,pl;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_vol__track__msg, container, false);
        t=getActivity().getIntent().getStringExtra("tripid");
        nd=v.findViewById(R.id.nvdnr);
        nn=v.findViewById(R.id.nvneedy);
        cp=v.findViewById(R.id.cphone);
        cc=v.findViewById(R.id.cchat);
        nd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //
                // map to donor
                Intent mapIntent;
                Uri location;
                String url = "http://maps.google.com/maps?q=loc:" + slat + "," + slon + " (" + pl + ")";
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

        nn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //map to needy
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

        cp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_DIAL,
                        Uri.parse("tel:" + phno));
                startActivity(i);

            }
        });

        cc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PackageManager packageManager = getContext().getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ "91"+phno +"&text=" + URLEncoder.encode("Volunteer for trip: "+t, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        getContext().startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        });

        ref= FirebaseDatabase.getInstance().getReference().child("dnmapping").child(t);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                phno=snapshot.child("phone").getValue().toString();
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
        return v;
    }
}