package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import android.widget.Spinner;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

public class AddHotspotActivity extends AppCompatActivity {
    private DatabaseReference mDatabase,mDBw,mDB1;

    TextInputLayout addressTextInput;
    TextInputEditText addressEditText;
    TextInputLayout nameTextInput;
    TextInputEditText nameEditText;
    TextInputLayout avgNumTextInput;
    TextInputEditText avgNumEditText;
    AutoCompleteTextView city,area;
    ArrayList<String> cities ;
    ArrayList<String> areas;
    ArrayAdapter<String> adap_cities;
    ArrayAdapter<String> adap_areas;
    MaterialButton addButton,cancel;
    String c,ar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hotspot);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.api_key), Locale.US);
        }

        PlacesClient placesClient = Places.createClient(this);

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.add_hotspot_address);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                // TODO: Get info about the selected place.
                Log.i("onCreate", "Place: " + place.getName() + ", " + place.getId());
            }


            @Override
            public void onError(@NotNull Status status) {
                // TODO: Handle the error.
                Log.i("onCreate", "An error occurred: " + status);
            }
        });


        addressTextInput = findViewById(R.id.add_hotspot_location);
        addressEditText = findViewById(R.id.add_hotspot_location_edit_text);
        nameTextInput = findViewById(R.id.add_hotspot_name);
        nameEditText = findViewById(R.id.add_hotspot_name_edit_text);
        avgNumTextInput = findViewById(R.id.add_hotspot_num);
        avgNumEditText = findViewById(R.id.add_hotspot_num_edit_text);
        city=(AutoCompleteTextView) findViewById(R.id.add_hotspot_city);
        city.setThreshold(1);
        area=(AutoCompleteTextView) findViewById(R.id.add_hotspot_area);
        area.setThreshold(1);
        addButton = findViewById(R.id.add_hotspot_button);
        cancel = findViewById(R.id.cancel_add_hotspot);

        mDB1= FirebaseDatabase.getInstance().getReference().child("hotspot list");

        mDB1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cities =new ArrayList<>();
                for(DataSnapshot snap:snapshot.getChildren())
                {
                    cities.add(snap.getKey().toString());
                }
                adap_cities=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item, cities);
                city.setAdapter(adap_cities);
                city.setOnTouchListener(new View.OnTouchListener(){
                    @Override
                    public boolean onTouch(View v, MotionEvent event){
                        city.showDropDown();
                        return false;
                    }
                });

                city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        c= cities.get(i);
                        DatabaseReference ref=FirebaseDatabase.getInstance().getReference().child("hotspot list").child(c);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                areas=new ArrayList<>();
                                for(DataSnapshot s:snapshot.getChildren()){
                                    areas.add(s.getKey().toString());
                                }
                                adap_areas=new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_dropdown_item,areas);
                                area.setAdapter(adap_areas);
                                area.setOnTouchListener(new View.OnTouchListener(){
                                    @Override
                                    public boolean onTouch(View v, MotionEvent event){
                                        area.showDropDown();
                                        return false;
                                    }
                                });

                                area.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        ar=areas.get(i);
                                    }

                                    @Override
                                    public void onNothingSelected(AdapterView<?> adapterView) {

                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
    public void addHotspot(View v){
        if(!isValid()){
            return;
        }
            int a = Integer.parseInt(avgNumEditText.getText().toString());
            double lat = Double.parseDouble(addressEditText.getText().toString().split(" ")[0]);
            double lon = Double.parseDouble(addressEditText.getText().toString().split(" ")[1]);;
            String n = nameEditText.getText().toString();

            Hotspot h = new
                    Hotspot(a, lat, lon, n);
            if(c==null)c=city.getText().toString();
            if(ar==null)ar=area.getText().toString();

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
        if(addressEditText.getText().toString()==null||addressEditText.getText().toString().equals("")){
            addressTextInput.setError("Enter Address in Latitude Longitude Format");
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

}