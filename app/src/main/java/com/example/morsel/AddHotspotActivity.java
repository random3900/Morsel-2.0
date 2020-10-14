package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
        int a = Integer.parseInt(avgNumEditText.getText().toString());
        double lat = Double.parseDouble(addressEditText.getText().toString().split(" ")[0]);
        double lon = Double.parseDouble(addressEditText.getText().toString().split(" ")[1]);;
        String n = nameEditText.getText().toString();
        Hotspot h = new
                Hotspot(a, lat, lon, n);
        FirebaseDatabase.getInstance().getReference().child("hotspot list").child(c).child(ar).push().setValue(h);

            Intent i=new Intent(AddHotspotActivity.this,HotspotsActivity.class);
        startActivity(i);
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