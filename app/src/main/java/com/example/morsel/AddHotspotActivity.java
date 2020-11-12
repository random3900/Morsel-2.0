package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class AddHotspotActivity extends AppCompatActivity {
    private DatabaseReference mDatabase,mDBw,mDB1;

    TextInputLayout addrTextInput;
    TextInputLayout nameTextInput;
    TextInputEditText nameEditText;
    TextInputLayout avgNumTextInput;
    TextInputEditText avgNumEditText;
    TextInputEditText addrEditText;
    MaterialButton addButton,cancel;
    ImageButton addr_btn;
    String c,ar;
    double lat,lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotspot);

        addr_btn = (ImageButton)findViewById(R.id.addr_btn);

        addr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPointOnMap();
            }
        });
        nameTextInput = findViewById(R.id.add_hotspot_name);
        nameEditText = findViewById(R.id.add_hotspot_name_edit_text);
        avgNumTextInput = findViewById(R.id.add_hotspot_num);
        avgNumEditText = findViewById(R.id.add_hotspot_num_edit_text);
        addrEditText = findViewById(R.id.add_hotspot_addr_edit_text);
        addButton = findViewById(R.id.add_hotspot_button);
        cancel = findViewById(R.id.cancel_add_hotspot);
        addrTextInput = findViewById(R.id.add_hotspot_addr);

    }
    public void addHotspot(View v){
        if(!isValid()){
            return;
        }
        addrTextInput.setError("");
        nameTextInput.setError("");
        avgNumTextInput.setError("");
        int a = Integer.parseInt(avgNumEditText.getText().toString());
        String n = nameEditText.getText().toString();

        Hotspot h = new
                Hotspot(a, lat, lon, n);

        FirebaseDatabase.getInstance().getReference().child("hotspot list").child(c).child(ar).push().setValue(h);

        Intent i = new Intent(AddHotspotActivity.this, HotspotsActivity.class);
        startActivity(i);

    }
    public boolean isValid(){
        boolean to_ret = true;
        if(avgNumEditText.getText().toString()==null||avgNumEditText.getText().toString().equals("")){
            avgNumTextInput.setError("Enter Number of Residents");
            to_ret = false;
        }
        if(nameEditText.getText().toString()==null||nameEditText.getText().toString().equals("")){
            nameTextInput.setError("Enter Hotspot Name");
            to_ret = false;
        }
        if(addrEditText.getText().toString()==null){
            addrTextInput.setError("Enter Address");
            to_ret = false;
        }
        return to_ret;
    }

    public void cancelButton(View view) {
        Intent i=new Intent(AddHotspotActivity.this,HotspotsActivity.class);
        startActivity(i);
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

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("OnActRes","Inside");
        if (requestCode == PICK_MAP_POINT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
//hi;
                getAddress(getApplicationContext(),latLng.latitude,latLng.longitude);

                // venue_editText.setText((float) latLng.latitude +" "+ (float)latLng.longitude);
                Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
            }
        }
    }


    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {

//Set Address
        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null && addresses.size() > 0) {



                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

                c = addresses.get(0).getSubAdminArea();
                ar = addresses.get(0).getLocality();
                lat = addresses.get(0).getLatitude();
                lon = addresses.get(0).getLongitude();

                addrEditText.setText(address);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return;
    }
    static final int PICK_MAP_POINT_REQUEST = 999;  // The request code
    private void pickPointOnMap() {
        Intent pickPointIntent = new Intent(this, MapsActivity.class);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
    }
}