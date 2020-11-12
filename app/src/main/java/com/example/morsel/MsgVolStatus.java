package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MsgVolStatus extends AppCompatActivity {

    DatabaseReference mDatabase;
    MaterialCardView mc1,mc2;
    String ti,imgurl;
    Double d;
    Vol_Track_Msg vtm;
    TextView trip,pl,pk,volt,img,dist;
    Button vb;
    int volstatus;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_msg_vol_status);
        ti=getIntent().getStringExtra("tripid");

        mc1=findViewById(R.id.card1);
        mc2=findViewById(R.id.card2);
        trip=findViewById(R.id.tid);
        pl=findViewById(R.id.pld);
        pk=findViewById(R.id.pkd);
        volt=findViewById(R.id.volt);
        vb=findViewById(R.id.volb);
        dist=findViewById(R.id.dist);
        img=findViewById(R.id.imgd);
        trip.setText(ti);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("dnmapping").child(ti);
        vb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String disp;
                disp=vb.getText().toString();
                if(disp.equals("Volunteer")) {
                    if (volstatus == 2) {
                        showMessage("Someone has already Volunteered", "Thank you for your time.\n Pls check the same link at a later point of time or Pls do volunteer for some other trip. Thanks Again");
                    } else {
                        //intent for tracking
                        DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("dnmapping").child(ti);
                        db.child("status").setValue(2);
                        vtm=new Vol_Track_Msg();
                        FragmentManager fm = getSupportFragmentManager();

                        // create a FragmentTransaction to begin the transaction and replace the Fragment
                        FragmentTransaction fragmentTransaction =
                                fm.beginTransaction();

                        // replace the FrameLayout with new Fragment
                        fragmentTransaction.replace(R.id.fvol, vtm);

                        //fragmentTransaction.add(R.id.firstFragment,fragment);
                        fragmentTransaction.commit(); // save the changes

                    }
                    vb.setText("Cancel");
                }
                else if(disp.equals("Cancel")){
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("dnmapping").child(ti);
                    db.child("status").setValue(1);
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                    if (vtm != null) {
                        transaction.remove(vtm);
                        transaction.commit();
                        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
                        vtm = null;
                }
                    vb.setText("Volunteer");
            }
        }});
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pl.setText(snapshot.child("place").getValue().toString());
                pk.setText(snapshot.child("packets").getValue().toString());
                d=Double.parseDouble(snapshot.child("dist").getValue().toString());
                String dt=String.format("%.2f", d);
                dist.setText(dt+"km");
                volstatus=Integer.parseInt(snapshot.child("status").getValue().toString());
                imgurl=snapshot.child("imageurl").getValue().toString();
                if(volstatus==1) {
                    int c=R.color.colorPrimaryDark;
                    mc2.setBackgroundResource(c);
                    volt.setText("N");
                }
                else if(volstatus==2) {
                    int c=R.color.themegreen;
                    int c1=R.color.white;
                    mc2.setBackgroundResource(c);
                    volt.setText("Y");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent down=new Intent(Intent.ACTION_VIEW, Uri.parse(imgurl));
                startActivity(down);
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
}

