package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    String ti;
    TextView trip,pl,pk,volt;
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

        trip.setText(ti);
        mDatabase = FirebaseDatabase.getInstance().getReference().child("dnmapping").child(ti);
        vb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(volstatus==1)
                {
                    showMessage("Someone has already Volunteered","Thank you for your time.\n Pls check the same link at a later point of time or Pls do volunteer for some other trip. Thanks Again");
                }

                else
                {
                    //intent for tracking
                    DatabaseReference db=FirebaseDatabase.getInstance().getReference().child("dnmapping").child(ti);
                    db.child("vol?").setValue(1);
                    FragmentManager fm = getSupportFragmentManager();

                    // create a FragmentTransaction to begin the transaction and replace the Fragment
                    FragmentTransaction fragmentTransaction =
                            fm.beginTransaction();

                    // replace the FrameLayout with new Fragment
                    fragmentTransaction.replace(R.id.fvol, new Vol_Track_Msg());

                    //fragmentTransaction.add(R.id.firstFragment,fragment);
                    fragmentTransaction.commit(); // save the changes

                }
            }
        });
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pl.setText(snapshot.child("place").getValue().toString());
                pk.setText(snapshot.child("packets").getValue().toString());
                volstatus=Integer.parseInt(snapshot.child("vol?").getValue().toString());
                if(volstatus==0) {
                    int c=R.color.colorPrimaryDark;
                    mc2.setBackgroundResource(c);
                    volt.setText("N");
                }
                else {
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
    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
}

