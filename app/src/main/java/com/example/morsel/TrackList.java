package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;

public class TrackList extends AppCompatActivity {

    ListView lv;
    DatabaseReference mdb,mdb1;
    FirebaseAuth mauth;
    ArrayList<HashMap<String,String>> tripd=new ArrayList<>();
    ArrayList<String> ul=new ArrayList<>();
    SimpleAdapter adap;
    String pl="Place";
    HashMap<String,String> content ;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_list);
        lv=findViewById(R.id.tlv);
        mauth=FirebaseAuth.getInstance();



        mdb= FirebaseDatabase.getInstance().getReference().child("Users").child(mauth.getCurrentUser().getUid()).child("trips");
        mdb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot d:snapshot.getChildren()){

                        content = new HashMap<String, String>();
                        String dkey=d.getKey().toString();
                        ul.add(dkey);


                        content.put("place",d.getKey());
                        content.put("date",d.getValue().toString());
                        tripd.add(content);
                }
                String[] entry = new String[] {"place", "date"};
                SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), tripd,
                        android.R.layout.simple_list_item_2,
                        entry,
                        new int[] {android.R.id.text1,
                                android.R.id.text2
                        });
                lv.setAdapter(adapter);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i=new Intent(TrackList.this,TrackActivity.class);
                i.putExtra("tripid",ul.get(position));
                startActivity(i);
            }
        });
    }




}