package com.example.morsel;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LocationTrackerService extends Service {
    private FusedLocationProviderClient mLocationProviderClient;
    private LocationCallback locationUpdatesCallback;
    private LocationRequest locationRequest;
    String id2;
    //Integer id=1;
    public LocationTrackerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        setUpLocationRequest();

    }


    private void setUpLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(100000000);
        locationRequest.setFastestInterval(500000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /*String keyValue = intent.getStringExtra("key");
        if(keyValue!=null && keyValue.equals("stop")){
            stopSelf();
        }else {*/
            //Bundle b=intent.getExtras();
            //id2=b.getString("id_vol");
            id2=intent.getStringExtra("vol_id");
            setUpLocationUpdatesCallback();
            Toast.makeText(getApplicationContext(),id2,Toast.LENGTH_SHORT).show();
            mLocationProviderClient.requestLocationUpdates(locationRequest, locationUpdatesCallback, null);
        //}
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mLocationProviderClient.removeLocationUpdates(locationUpdatesCallback);
    }

    private void setUpLocationUpdatesCallback() {
        locationUpdatesCallback = new LocationCallback(){
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if(locationResult!=null){

                    Location lastLocation = locationResult.getLastLocation();
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    //DatabaseReference ref = database.getReference().child("volunteer");
                    //String id1=ref.push().getKey();
                    final String loc=lastLocation.getLatitude()+","+lastLocation.getLongitude();
                    //Toast.makeText(getApplicationContext(),loc,Toast.LENGTH_SHORT).show();
                    //ref.child(id1).child("location").setValue(loc);


                    final DatabaseReference mdb = FirebaseDatabase.getInstance().getReference().child("volunteer").child(id2);
                    mdb.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {

                            if(snapshot.getValue()!=null) {
                                //String id1=Object.keys(snapshot.val())[0];
                                //Toast.makeText(getApplicationContext(),id1,Toast.LENGTH_SHORT).show();
                                //f (id1 == id2) {
                                    mdb.child("location").setValue(loc);
                                    //Toast.makeText(getApplicationContext(), lat1d+" "+lon1d, Toast.LENGTH_SHORT).show();
                                }
                            //}
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
                        }
                    });
                    /*Map<String, Object> data = new HashMap<>();
                    data.put("latitude", lastLocation.getLatitude());
                    data.put("longitude", lastLocation.getLongitude());
                    data.put("time", lastLocation.getTime());
                    ref.setValue(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //
                            Log.i("tag", "Location update saved");
                        }
                    });*/

                }else{
                    Log.i("tag", "Location null");
                }
            }
        };
    }

}