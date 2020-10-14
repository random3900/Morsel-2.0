package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
    Spinner city,area;
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
        city=(Spinner)findViewById(R.id.add_hotspot_city);
        area=(Spinner)findViewById(R.id.add_hotspot_area);
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

}