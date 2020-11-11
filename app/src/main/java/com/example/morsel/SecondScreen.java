package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class SecondScreen extends AppCompatActivity {

    String email,name;
    private FirebaseAuth mauth;
    RequestQueue mreq;
    TextView t;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_screen);
        mauth=FirebaseAuth.getInstance();
        FirebaseUser user=mauth.getCurrentUser();
        t=findViewById(R.id.huser);
        t.setText("Hello User: "+user.getEmail());
        mreq= Volley.newRequestQueue(this);
        Toast.makeText(getApplicationContext(),"IN SECOND SCREEN",Toast.LENGTH_SHORT).show();


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

}
