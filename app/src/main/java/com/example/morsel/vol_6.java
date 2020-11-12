package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class vol_6 extends AppCompatActivity implements View.OnClickListener {
    ImageView img_src1, img_dst1;
    TextView area,place,city,city1,packets,packets1,dist,dist1;
    Button btn_src,btn_dst;
    String idk1,idv;
    Double dist3,lat1d,lon1d,x,y;
    final Double[] sh_loc = new Double[4];
    private DatabaseReference mDatabase, mdb,mdb1;
    double pi = 3.14159265358979323846;
    double earth_radius = 6371.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_6);
        img_src1 = findViewById(R.id.img_src);
        img_dst1 = findViewById(R.id.img_dst);
        area = findViewById(R.id.tv_area);
        city = findViewById(R.id.tv_city);
        dist = findViewById(R.id.tv_dist);
        packets = findViewById(R.id.tv_packets);
        place = findViewById(R.id.tv_place);
        city1 = findViewById(R.id.tv_city1);
        dist1 = findViewById(R.id.tv_dist1);
        packets1 = findViewById(R.id.tv_packets1);
        btn_src = findViewById(R.id.btn_src);
        btn_dst = findViewById(R.id.btn_dst);
        img_src1.setOnClickListener(this);
        btn_src.setOnClickListener(this);
        btn_dst.setOnClickListener(this);
        img_dst1.setOnClickListener(this);
        Bundle b = getIntent().getExtras();
        sh_loc[0] = b.getDouble("slat");
        sh_loc[1] = b.getDouble("slon");
        sh_loc[2] = b.getDouble("dlat");
        sh_loc[3] = b.getDouble("dlon");
        idk1=b.getString("idkey");
        idv=b.getString("idv");
        dist3 = b.getDouble("dist2");
        dist1.setText(dist3.toString());
        mdb1= FirebaseDatabase.getInstance().getReference().child("dnmapping").child(idk1);
        mdb1.addValueEventListener(new ValueEventListener() {
            public void onDataChange(DataSnapshot ds) {
                area.setText(ds.child("area").getValue().toString());
                city.setText(ds.child("city").getValue().toString());
                dist.setText(ds.child("dist").getValue().toString());
                packets.setText(ds.child("packets").getValue().toString());
                place.setText(ds.child("place").getValue().toString());
                city1.setText(ds.child("city").getValue().toString());

                packets1.setText(ds.child("packets").getValue().toString());

            }

            //}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == img_src1.getId()) {
            Intent mapIntent;
            Uri location;
            String url = "http://maps.google.com/maps?q=loc:" + sh_loc[0] + "," + sh_loc[1] + " (" + area.getText().toString()+ ")";
            //String url=("geo:"+String.valueOf(sh_loc[0])+","+String.valueOf(sh_loc[1])+"?z=14");
            // Or map point based on latitude/longitude
            // location = Uri.parse("geo:37.422219,-122.08364?z=14");
            location = Uri.parse(url);
            // z param is zoom level
           /*
                1: World
                5: Landmass/continent
                10: City
                15: Streets
                20: Buildings
               */

            mapIntent = new Intent(Intent.ACTION_VIEW, location);
            startActivity(mapIntent);
            /*String orderStatus="1";
            Intent intent=new Intent(getApplicationContext(),TrackActivity.class);
            intent.putExtra("orderStatus",orderStatus);
            startActivity(intent);*/

        }
        else if(view.getId() == img_dst1.getId())
        {
            Intent mapIntent;
            Uri location;
            String url = "http://maps.google.com/maps?q=loc:" + sh_loc[2] + "," + sh_loc[3] + " (" + place.getText().toString() + ")";
            //String url=("geo:"+String.valueOf(sh_loc[0])+","+String.valueOf(sh_loc[1])+"?z=14");
            // Or map point based on latitude/longitude
            // location = Uri.parse("geo:37.422219,-122.08364?z=14");
            location = Uri.parse(url);
            // z param is zoom level
           /*
                1: World
                5: Landmass/continent
                10: City
                15: Streets
                20: Buildings
               */

            mapIntent = new Intent(Intent.ACTION_VIEW, location);
            startActivity(mapIntent);
            /*String orderStatus="1";
            Intent intent=new Intent(getApplicationContext(),TrackActivity.class);
            intent.putExtra("orderStatus",orderStatus);
            startActivity(intent);*/
        }
        else if(view.getId() == btn_src.getId())
        {
            mdb = FirebaseDatabase.getInstance().getReference().child("volunteer").child(idv);
            mdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //String id1=mdb.push().getKey();
                    if(snapshot.getValue()!=null&&snapshot.child("location").getValue()!=null) {
                        //if (id1 == id) {
                        String[] loc2 = (snapshot.child("location").getValue().toString()).split(",");
                        lat1d = Double.parseDouble(loc2[0]);
                        lon1d = Double.parseDouble(loc2[1]);
                        //Toast.makeText(getApplicationContext(), lat1d+" "+lon1d, Toast.LENGTH_SHORT).show();
                        x=distanceEarth(sh_loc[0],sh_loc[1]);
                        if(x<=5000)
                        {

                            final DatabaseReference mdb = FirebaseDatabase.getInstance().getReference().child("dnmapping").child(idk1);
                            mdb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {

                                    if(snapshot.getValue()!=null) {
                                        //String id1=Object.keys(snapshot.val())[0];
                                        //Toast.makeText(getApplicationContext(),id1,Toast.LENGTH_SHORT).show();
                                        //f (id1 == id2) {
                                        mdb.child("status").setValue(3);
                                        //Toast.makeText(getApplicationContext(), lat1d+" "+lon1d, Toast.LENGTH_SHORT).show();
                                    }
                                    //}
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
                                }
                            });

                            Toast.makeText(getApplicationContext(), "Succesfully completed half trip.Take up the remaining trip quickly to donate food", Toast.LENGTH_LONG).show();
                        }
                        //}
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(view.getId() == btn_dst.getId())
        {
            mdb = FirebaseDatabase.getInstance().getReference().child("volunteer").child(idv);
            mdb.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot snapshot) {
                    //String id1=mdb.push().getKey();
                    if(snapshot.getValue()!=null&&snapshot.child("location").getValue()!=null) {
                        //if (id1 == id) {
                        String[] loc2 = (snapshot.child("location").getValue().toString()).split(",");
                        lat1d = Double.parseDouble(loc2[0]);
                        lon1d = Double.parseDouble(loc2[1]);
                        //Toast.makeText(getApplicationContext(), lat1d+" "+lon1d, Toast.LENGTH_SHORT).show();
                        y=distanceEarth(sh_loc[2],sh_loc[3]);
                        if(y<=5000)
                        {

                            final DatabaseReference mdb = FirebaseDatabase.getInstance().getReference().child("dnmapping").child(idk1);
                            mdb.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot snapshot) {

                                    if(snapshot.getValue()!=null) {
                                        //String id1=Object.keys(snapshot.val())[0];
                                        //Toast.makeText(getApplicationContext(),id1,Toast.LENGTH_SHORT).show();
                                        //f (id1 == id2) {
                                        mdb.child("status").setValue(4);
                                        //Toast.makeText(getApplicationContext(), lat1d+" "+lon1d, Toast.LENGTH_SHORT).show();
                                    }
                                    //}
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Toast.makeText(getApplicationContext(), "Succesfully donated food.Thank you for your service", Toast.LENGTH_LONG).show();
                            //Intent i6=new Intent(getApplicationContext(),SecondScreen.class);
                            //stopService(new Intent(getApplicationContext(),LocationTrackerService.class));
                            //startActivity(i6);
                        }
                        //}
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(getApplicationContext(), "Database error", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    public double degtorad(double deg)
    {
        return ( deg * pi / 180);
    }

    // Function to calculate distance
// between 2 given locations
// using Great Circle Distance Formula.
    public double distanceEarth(double lat2d, double lon2d)
    {
        double lat1, lon1, lat2, lon2,
                delta_lon, central_ang;

        lat1 = degtorad(lat1d);
        lon1 = degtorad(lon1d);
        lat2 = degtorad(lat2d);
        lon2 = degtorad(lon2d);

        delta_lon = lon2 - lon1;

        // great circle distance formula.
        central_ang = Math.acos( Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(delta_lon));

        return (earth_radius * central_ang);
    }
}