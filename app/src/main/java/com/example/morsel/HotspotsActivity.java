package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.morsel.R.id.add_hotspot_button;
import static java.security.AccessController.getContext;

public class HotspotsActivity extends AppCompatActivity {

    static AppLocationService appLocationService;
    private String locAddr;

    public static final String HOTSPOTS_CHILD = "hotspot list";
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private SharedPreferences mSharedPreferences;
    private GoogleSignInClient mSignInClient;
    private static final String HOTSPOT_URL = "https://morsel-a2477.firebaseio.com/hotspot%20list";

    private RecyclerView mHotspotRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hotspots);


        Log.d("HOTSPOT","Inside Oncreate");
        FloatingActionButton add_button = findViewById(R.id.add_hotspot_button);
        add_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i=new Intent(HotspotsActivity.this,AddHotspotActivity.class);
                startActivity(i);
            }
        });

//        appLocationService = new this.AppLocationService(getApplicationContext());


        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(getApplicationContext(), gso);

        // Initialize ProgressBar and RecyclerView.
        mHotspotRecyclerView = (RecyclerView) findViewById(R.id.hotspot_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mHotspotRecyclerView.setLayoutManager(mLinearLayoutManager);

        //FIREBASE AUTH
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            ((NavigationHost) getApplicationContext()).navigateTo(new Login(), false);
        }
        Log.d("HOTSPOT","User Exists");

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<Hotspot> parser;
        parser = new SnapshotParser<Hotspot>() {
            @Override
            public Hotspot parseSnapshot(DataSnapshot dataSnapshot) {
                Hotspot hotspot = dataSnapshot.getValue(Hotspot.class);
                if (hotspot != null) {
                    hotspot.setId(dataSnapshot.getKey());
                }
                return hotspot;
            }
        };

        Log.d("HOTSPOT","Snapshot Parser set");

        DatabaseReference hotspotsRefCity = mFirebaseDatabaseReference.child(HOTSPOTS_CHILD).child("Chennai");


        hotspotsRefCity.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                final ArrayList<Hotspot> hl = new ArrayList<>();
                for (DataSnapshot area : snapshot.getChildren()) {
                    for (DataSnapshot hotspot : area.getChildren()) {
                        Hotspot h_temp;
                        int a = Integer.parseInt(hotspot.child("avgnum").getValue().toString());
                        double lat = Double.parseDouble(hotspot.child("lat").getValue().toString());
                        double lon;
                        if(hotspot.child("long").getValue()!=null)
                            lon = Double.parseDouble(hotspot.child("long").getValue().toString());
                        else
                            lon = Double.parseDouble(hotspot.child("lon").getValue().toString());
                        String n = hotspot.child("name").getValue().toString();
                        h_temp = new Hotspot(a, lat, lon, n);
                        hl.add(h_temp);
//                        Location location = appLocationService
//                                .getLocation(LocationManager.GPS_PROVIDER);
//                        if (location != null) {
//                            double latitude = lat;
//                            double longitude = lon;
//                            LocationAddress locationAddress = new LocationAddress();
//                            locationAddress.getAddressFromLocation(latitude, longitude,
//                                    getApplicationContext(), new GeocoderHandler());
//                            h_temp.setAddress(locAddr);
//                            hl.add(h_temp);
//                        } else {
//                        }
                    }
                }
                HotspotAdapter h_adapter = new HotspotAdapter(hl);
                mHotspotRecyclerView.setAdapter(h_adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Database error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locAddr = bundle.getString("address");
                    break;
                default:
                    locAddr = null;
            }
            return;
        }
    }
}