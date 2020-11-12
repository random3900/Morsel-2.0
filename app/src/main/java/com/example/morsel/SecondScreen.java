package com.example.morsel;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class SecondScreen extends AppCompatActivity {

    String email,name;
    private FirebaseAuth mauth;
    RequestQueue mreq;
    TextView t;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        final FloatingActionButton btn = findViewById(R.id.add_moderator);
        btn.setVisibility(View.INVISIBLE);

        mauth=FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();
        t=findViewById(R.id.huser);
        t.setText("Hello User: "+user.getEmail());
        mreq= Volley.newRequestQueue(this);
        Toast.makeText(getApplicationContext(),"IN SECOND SCREEN",Toast.LENGTH_SHORT).show();

        FirebaseDatabase.getInstance().getReference().child("Users").child(mauth.getCurrentUser().getUid()).child("isMod").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("DC","Inside");
                Boolean b = dataSnapshot.getValue(Boolean.class);
                if(b==false){

                    btn.setVisibility(View.INVISIBLE);
                }
                else{

                    btn.setVisibility(View.VISIBLE);
                }
                progressDialog.cancel();
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

            case R.id.abtus:
                Intent i2=new Intent(this,aboutus.class);
                startActivity(i2);
                break;

            case R.id.bonpts:
                Intent i3=new Intent(this,BonusPoints.class);
                startActivity(i3);
                break;

            case R.id.up:
                Intent i4=new Intent(this,ProfileActivity.class);
                startActivity(i4);
                break;

            case R.id.mtr:
                Intent i5=new Intent(this,TrackActivity.class);
                startActivity(i5);
                break;

        }
        return true;
    }


    public void onClickContribute(View V){
        Intent i = new Intent(this, donate2.class);
        Toast.makeText(getApplicationContext(),"Message has been sent",Toast.LENGTH_SHORT).show();
        startActivity(i);
    }
    public void onClickVolunteer(View V){
        Intent i = new Intent(this, vol_1.class);
        startActivity(i);
    }

    public  void onClickHotspot(View v){
        Intent i=new Intent(this,HotspotsActivity.class);
        startActivity(i);
    }

    public  void onClickModerator(View v){
        Intent i=new Intent(this,SetModerator.class);
        startActivity(i);
    }

}
