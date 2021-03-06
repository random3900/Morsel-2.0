package com.example.morsel;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class donate2 extends AppCompatActivity {

    private DatabaseReference mDatabase, mDBw, mDB1;
    FirebaseAuth mauth;
    TextView txt;
    String a, c,mPhoneNumber;
    int count;
    TextInputEditText ftype, fqty, flat, flon,ph;
    TextInputLayout ti_ftype, ti_fqty, ti_flat, ti_flon, ti_ph;
    Spinner fcity, farea;
    ArrayList<String> cities;
    ArrayList<String> areas;
    ArrayAdapter<String> adap1;
    ArrayAdapter<String> adap2;
    private int mYear, mMonth, mDay;
    private Button btnSelect, btnUpload;
    TextInputLayout addrTextInput;
    TextInputEditText addrEditText;
    String cr,ar;
    double lat,lon;
    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    public Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;
    SQLiteDatabase db;
    ImageButton addr_btn;
    SQLiteDatabase bb;
    final Uri[] link = new Uri[1];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate2);
        addr_btn = (ImageButton)findViewById(R.id.addr_btn);

        addr_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pickPointOnMap();
            }
        });
        addrEditText = findViewById(R.id.add_hotspot_addr_edit_text);
        mauth = FirebaseAuth.getInstance();
        db = openOrCreateDatabase("FoodsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS historydet(email VARCHAR,tdate date,fname VARCHAR,qty NUMERIC, location VARCHAR);");
        bb = openOrCreateDatabase("122BonuspDB", Context.MODE_PRIVATE, null);
        bb.execSQL("CREATE TABLE IF NOT EXISTS bonus(user VARCHAR, bonuspt NUMERIC);");
        ftype = findViewById(R.id.et_d_foodtype);
        fqty = findViewById(R.id.et_d_foodqty);
        flat = findViewById(R.id.et_d_lat);
        flon = findViewById(R.id.et_d_long);
        fcity = (Spinner) findViewById(R.id.ecity2);
        farea = (Spinner) findViewById(R.id.earea2);
        ph=findViewById(R.id.et_d_phno);

        ti_flat = findViewById(R.id.tv_d_lat);
        ti_flon = findViewById(R.id.tv_d_long);
        ti_fqty = findViewById(R.id.tv_d_foodqty);
        ti_ftype = findViewById(R.id.tv_d_foodtype);
        ti_ph = findViewById(R.id.tv_d_phno);

        ActionBar actionBar;
        actionBar = getSupportActionBar();
        ColorDrawable colorDrawable
                = new ColorDrawable(
                Color.parseColor("#0F9D58"));
        actionBar.setBackgroundDrawable(colorDrawable);

        // initialise views
        btnSelect = findViewById(R.id.btnChoose);
        btnUpload = findViewById(R.id.btnUpload);
        imageView = findViewById(R.id.imgView);

        // get the Firebase  storage reference
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mDB1 = FirebaseDatabase.getInstance().getReference().child("hotspot list");

        mDB1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                cities = new ArrayList<>();
                for (DataSnapshot snap : snapshot.getChildren()) {
                    cities.add(snap.getKey().toString());
                }
                adap1 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, cities);
                fcity.setAdapter(adap1);
                fcity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        c = cities.get(i);
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("hotspot list").child(c);
                        ref.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                areas = new ArrayList<>();
                                for (DataSnapshot s : snapshot.getChildren()) {
                                    areas.add(s.getKey().toString());
                                }
                                adap2 = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, areas);
                                farea.setAdapter(adap2);
                                farea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                    @Override
                                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                                        a = areas.get(i);
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


                // on pressing btnSelect SelectImage() is called
                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        SelectImage();
                    }
                });

                // on pressing btnUpload uploadImage() is called
                btnUpload.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        uploadImage();
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

//    protected void onActivityResults(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Log.d("OnActRes","Inside");
//        if (requestCode == PICK_MAP_POINT_REQUEST) {
//            // Make sure the request was successful
//            if (resultCode == RESULT_OK) {
//
//                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
////hi;
//                getAddress(getApplicationContext(),latLng.latitude,latLng.longitude);
//
//                // venue_editText.setText((float) latLng.latitude +" "+ (float)latLng.longitude);
//                Toast.makeText(this, "Point Chosen: " + latLng.latitude + " " + latLng.longitude, Toast.LENGTH_LONG).show();
//            }
//        }
//    }
//
//
//    public void getAddress(Context context, double LATITUDE, double LONGITUDE) {
//
////Set Address
//        try {
//            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
//            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
//            if (addresses != null && addresses.size() > 0) {
//
//
//
//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//
//                cr = addresses.get(0).getSubAdminArea();
//                ar = addresses.get(0).getLocality();
//                lat = addresses.get(0).getLatitude();
//                lon = addresses.get(0).getLongitude();
//
//                addrEditText.setText(address);
//
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return;
//    }
    static final int PICK_MAP_POINT_REQUEST = 999;  // The request code
    private void pickPointOnMap() {
        Intent pickPointIntent = new Intent(this, MapsActivity.class);
        startActivityForResult(pickPointIntent, PICK_MAP_POINT_REQUEST);
    }
    private void SelectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(
                        intent,
                        "Select Image from here..."),
                PICK_IMAGE_REQUEST);
    }

    // Override onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // if request code is PICK_IMAGE_REQUEST and
        // resultCode is RESULT_OK
        // then set image in the image view
        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            // Get the Uri of data
            filePath = data.getData();
            try {

                // Setting image on image view using Bitmap
                Bitmap bitmap = MediaStore
                        .Images
                        .Media
                        .getBitmap(
                                getContentResolver(),
                                filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
        Log.d("OnActRes","Inside");
        if (requestCode == PICK_MAP_POINT_REQUEST) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {

                LatLng latLng = (LatLng) data.getParcelableExtra("picked_point");
//hi;
                getAddress(getApplicationContext(),latLng.latitude,latLng.longitude);
                double llat=latLng.latitude;
                String finallat = new Double(llat).toString();
                flat.setText(finallat);
                double llong=latLng.longitude;
                String finallong = new Double(llong).toString();
                flon.setText(finallong);

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

                cr = addresses.get(0).getSubAdminArea();
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


    // UploadImage method
    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
//            String apig =ref.toString();
//            Toast.makeText(donate2.this,"OTP Received is: "+ apig ,Toast.LENGTH_LONG).show();
//            Log.e("The url is", apig);



                // Get the download URL for 'images/stars.jpg'
                // This can be inserted into an <img> tag
                // This can also be downloaded directly

                    // adding listeners on upload
            // or failure of image
            ref.putFile(filePath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();

                                    ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>()
                                    {
                                        @Override
                                        public void onSuccess(Uri downloadUrl){
                                            //do something with downloadurl
                                            link[0] =downloadUrl;
                                            Toast.makeText(donate2.this,"The link: "+ link[0] ,Toast.LENGTH_LONG).show();
                                            Log.e("The url is", String.valueOf(link[0]));
                                        }});

                                    Toast.makeText(donate2.this, "Image Uploaded!!", Toast.LENGTH_SHORT).show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();

                            Toast
                                    .makeText(donate2.this,
                                            "Failed " + e.getMessage(),
                                            Toast.LENGTH_SHORT)
                                    .show();
                        }
                    })
                    .addOnProgressListener(
                            new OnProgressListener<UploadTask.TaskSnapshot>() {

                                // Progress Listener for loading
                                // percentage on the dialog box
                                @Override
                                public void onProgress(
                                        UploadTask.TaskSnapshot taskSnapshot) {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int) progress + "%");
                                }
                            });

        }
    }

    public void showMessage(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void onClickSubmit2(View view) {
        final Calendar c1 = Calendar.getInstance();
        mYear = c1.get(Calendar.YEAR);
        mMonth = c1.get(Calendar.MONTH) + 1;
        mDay = c1.get(Calendar.DAY_OF_MONTH);

        if (fqty.getText().toString().trim().length() == 0 || ftype.getText().toString().trim().length() == 0 ||
                a.trim().length() == 0 || c.trim().length() == 0 || flat.getText().toString().trim().length() == 0 ||
                flon.getText().toString().trim().length() == 0) {
            showMessage("Error", "Please enter all values");
            return;
        }

        if (Integer.parseInt(fqty.getText().toString()) == 0) {
            showMessage("Error", "Please enter the quantity of food");
            return;
        }

//        db.execSQL("INSERT INTO bonus VALUES(" + Integer.toString(2) +");");
//        Toast.makeText(this,String.valueOf(count),Toast.LENGTH_LONG).show();
//        db.execSQL("UPDATE bonus SET bonuspt=" +(bonuspt+2)+ "");

        int bpr;
        Cursor cpr = bb.rawQuery("SELECT * FROM bonus WHERE user='" + "xyz" + "'", null);
        if (cpr.moveToFirst()) {
            // Displaying record if found 
            bpr = cpr.getInt(1);
//            Toast.makeText(this, "hi"+String.valueOf(bpr), Toast.LENGTH_LONG).show();
            bb.execSQL("UPDATE bonus SET bonuspt='" + (Integer.toString(bpr + 2)) +
                    "' WHERE user='" + "xyz" + "'");
        }
        else
        {
            bb.execSQL("Insert into bonus values('"+"xyz"+"','" + Integer.toString(0)+"');");
//            Toast.makeText(this, "I am in Donate but wont update", Toast.LENGTH_LONG).show();
        }
//        db.execSQL("INSERT INTO bonus VALUES(" + Integer.toString(2) +");");


        mDatabase = FirebaseDatabase.getInstance().getReference().child("hotspot list").child(c).child(a);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {

                ArrayList<Hotspot> hl = new ArrayList<>();
                Hotspot h;
                for (DataSnapshot d : snapshot.getChildren()) {
                    int a = Integer.parseInt(d.child("avgnum").getValue().toString());
                    double lat = Double.parseDouble(d.child("lat").getValue().toString());
                    double lon = Double.parseDouble(d.child("lon").getValue().toString());
                    String n = d.child("name").getValue().toString();
                    h = new Hotspot(a, lat, lon, n);
                    hl.add(h);
                }

                String t = " ";
                double ulat, ulon, hlat, hlon, dlat, dlon, a1, c1, r, ulat1, ulon1;
                ulat1 = Double.parseDouble(flat.getText().toString());
                ulon1 = Double.parseDouble(flon.getText().toString());

                if (hl.size() == 0) {
                    showMessage("Oops", "No requirement. Thank you for your initiative");
                    return;
                }
                String d = mYear + "/" + mMonth + "/" + mDay;


                ulat = Math.toRadians(ulat1);
                ulon = Math.toRadians(ulon1);
                for (Hotspot h1 : hl) {
                    hlat = Math.toRadians(h1.getLat());
                    hlon = Math.toRadians(h1.getLon());
                    dlat = hlat - ulat;
                    dlon = hlon - ulon;
                    a1 = Math.pow(Math.sin(dlat / 2), 2)
                            + Math.cos(ulat) * Math.cos(hlat)
                            * Math.pow(Math.sin(dlon / 2), 2);
                    c1 = 2 * Math.asin(Math.sqrt(a1));
                    r = 6371;
                    h1.setDist(c1 * r);

                }


                Collections.sort(hl);
                ArrayList<Hotspot> dnmap = new ArrayList<>();
                double avail, req;
                avail = Double.parseDouble(fqty.getText().toString());
                for (Hotspot h1 : hl) {
                    req = h1.getAvgnum() * 450;
                    String loc = "(" + h1.getLat() + "," + h1.getLon() + ")";
                    FirebaseUser u = mauth.getCurrentUser();
                    db.execSQL("INSERT INTO historydet VALUES('" + u.getEmail() + "','" + d + "','" + ftype.getText() +
                            "','" + fqty.getText() + "','" + loc + "');");
                    if (req >= avail) {
                        h1.setPackets((int) Math.floor(avail / 450));
                        t += h1.getName() + " " + h1.getPackets() + "\n";
                        dnmap.add(h1);
                        break;
                    } else {
                        avail -= req;
                        h1.setPackets((int) h1.getAvgnum());
                        t += h1.getName() + " " + h1.getPackets() + "\n";
                        dnmap.add(h1);
                    }
                }


                ArrayList<String> nl = new ArrayList<>();
                ArrayList<String> cl = new ArrayList<>();
                ArrayList<String> pl = new ArrayList<>();
                ArrayList<String> dl = new ArrayList<>();
                ArrayList<String> idl = new ArrayList<>();
                mPhoneNumber=ph.getText().toString();
                mDBw = FirebaseDatabase.getInstance().getReference().child("dnmapping");

                for(Hotspot h1:hl)
                {
                    nl.add(h1.getName());
                    cl.add(h1.getLat()+","+h1.getLon());
                    pl.add(h1.getPackets()+"");
                    dl.add(String.format("%.20f",h1.getDist()));

                   

                    String id=mDBw.push().getKey();
                    idl.add(id);
                    mDBw.child(id).child("slat").setValue(ulat1);
                    mDBw.child(id).child("slon").setValue(ulon1);
                    mDBw.child(id).child("dlat").setValue(h1.getLat());
                    mDBw.child(id).child("dlon").setValue(h1.getLon());
                    mDBw.child(id).child("dist").setValue(h1.getDist());
                    mDBw.child(id).child("packets").setValue(h1.getPackets());
                    mDBw.child(id).child("place").setValue(h1.getName());
                    mDBw.child(id).child("area").setValue(a);
                    mDBw.child(id).child("phone").setValue(mPhoneNumber);
                    mDBw.child(id).child("city").setValue(c);
                    mDBw.child(id).child("status").setValue(1);
                    mDBw.child(id).child("imageurl").setValue(link[0].toString());
//                    mDBw.child(id).child("imageUrl").setValue(link[0]);
                    Toast.makeText(getApplicationContext(),id,Toast.LENGTH_SHORT).show();

                }


                Intent i=new Intent(getApplicationContext(),MappedDetails.class);
                i.putStringArrayListExtra("nl",nl);
                i.putStringArrayListExtra("cl",cl);
                i.putStringArrayListExtra("pl",pl);
                i.putStringArrayListExtra("dl",dl);
                i.putStringArrayListExtra("idl",idl);
                i.putExtra("size",nl.size());
                i.putExtra("area",a);
                i.putExtra("city",c);
                startActivity(i);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Database error",Toast.LENGTH_SHORT).show();
            }
        });
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
            case R.id.abtus:
                Intent i2=new Intent(this,aboutus.class);
                startActivity(i2);
                break;

            case R.id.bonpts:
                Intent i3=new Intent(this,BonusPoints.class);
                startActivity(i3);
                break;

            case R.id.mtr:
                Intent i5=new Intent(this,TrackList.class);
                startActivity(i5);
                break;
        }
        return true;
    }
}
