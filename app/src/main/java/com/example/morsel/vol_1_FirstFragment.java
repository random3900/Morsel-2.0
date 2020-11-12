package com.example.morsel;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class vol_1_FirstFragment extends Fragment implements View.OnClickListener {

    View view;
    private DatabaseReference mDatabase, mdb;
    EditText et_loc, et_weight;
    Button btn_volunteer;
    int reg_vol_id = 1;
    Integer id = 1;
    int f=1;

    private static final int RC_LOCATION_REQUEST = 1234;
    private int RC_LOCATION_ON_REQUEST = 1235;
    private Button mTrackButton;
    private LocationRequest locationRequest;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.vol_1_fragment_first, container, false);
        //et_loc = view.findViewById(R.id.et_loc);
        et_weight = view.findViewById(R.id.et_weight);
        btn_volunteer = view.findViewById(R.id.btn_volunteer);


        checkLocationSettingsRequest();
        setUpLocationRequest();

        btn_volunteer.setOnClickListener(this);

        return view;

    }



    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION }, RC_LOCATION_REQUEST );
        }else {
            //startService(new Intent(this, LocationTrackerService.class));
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RC_LOCATION_ON_REQUEST && resultCode == Activity.RESULT_OK){
            Log.e("tag", "Resolution done");
            startLocationUpdates();
        }
    }
    private void setUpLocationRequest() {
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void checkLocationSettingsRequest(){

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);

        SettingsClient locationClient = LocationServices.getSettingsClient(getActivity());
        Task<LocationSettingsResponse> locationSettings = locationClient.checkLocationSettings(builder.build());

        locationSettings.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {
                if(!task.isSuccessful()){
                    Log.e("tag", "Exception" + task.getException().getMessage());
                    if(task.getException() instanceof ResolvableApiException){
                        // show permission request to user
                        try {
                            ResolvableApiException resolvable = (ResolvableApiException) task.getException();
                            resolvable.startResolutionForResult(getActivity(),
                                    RC_LOCATION_ON_REQUEST);
                        } catch (IntentSender.SendIntentException sendEx) {
                            // Ignore the error.
                        }
                    }
                }else{
                    startLocationUpdates();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == RC_LOCATION_REQUEST && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            startLocationUpdates();
        }
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == btn_volunteer.getId()) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("volunteer");
            //mdb = FirebaseDatabase.getInstance().getReference().child("volunteer").child("vol"+"_"+id);

            f=1;
            if (et_weight.getText().toString().isEmpty()) {
                showMessage("Error", "Please enter all values");
                f=0;
            }
            else{

                if(Integer.parseInt(et_weight.getText().toString())<=0)
                {
                    f=0;
                    Toast.makeText(getActivity(),"Enter a valid packets no",Toast.LENGTH_SHORT).show();
                }
                if(f==1) {
                    Integer weight = Integer.parseInt(et_weight.getText().toString());
                    //String loc = et_loc.getText().toString();
                    String id1=mDatabase.push().getKey();
                    //mDatabase.child(id1).child("vol_id").setValue(id);
                    //mDatabase.child("vol" + id).child("vol_loc").setValue(loc);
                    mDatabase.child(id1).child("weight").setValue(weight);
                    //Vol vol = new Vol(id, loc, weight);
                    //mDatabase.child("vol" + id).setValue(vol);
                    Intent i5=new Intent(getActivity(), LocationTrackerService.class);
                    i5.putExtra("vol_id",id1);
                    getActivity().startService(i5);

                        /*Cursor c = db.rawQuery("SELECT * FROM vol WHERE vol_id ='" + String.valueOf(reg_vol_id) + "'", null);
                        if (c.moveToFirst()) {
                            // Modifying record if found 
                            //"12.986375,77.043701"
                            db.execSQL("UPDATE vol SET location='" + et_loc.getText() + "',weight='" + et_weight.getText() +
                                    "' WHERE vol_id='" + String.valueOf(reg_vol_id) + "'");
                            Toast.makeText(getActivity(), c.getString(1), Toast.LENGTH_LONG).show();
                        }
                        Cursor c1 = db.rawQuery("SELECT * FROM vol WHERE vol_id ='" + String.valueOf(reg_vol_id) + "'", null);
                        if (c1.moveToFirst()) {
                            Toast.makeText(getActivity(), c1.getString(1), Toast.LENGTH_LONG).show();
                        }*/
                    Intent i1 = new Intent(getActivity(), vol_2.class);
                    i1.putExtra("id_vol", id1);
                    startActivity(i1);
                }
            }

        }


    }


    public void showMessage (String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
