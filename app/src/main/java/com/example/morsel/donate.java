package com.example.morsel;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;
public class donate extends AppCompatActivity implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    Button submit, add, views, viewall, btnDatePicker, loc;
    EditText etFoodType, etQty, txtDate,e;
    SQLiteDatabase db;
    private int mYear, mMonth, mDay;
    private Button btnSelect, btnUpload;

    // view for image view
    private ImageView imageView;

    // Uri indicates, where the image will be picked from
    private Uri filePath;

    // request code
    private final int PICK_IMAGE_REQUEST = 22;

    // instance for firebase storage and StorageReference
    FirebaseStorage storage;
    StorageReference storageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);
        submit = findViewById(R.id.submit);
        etFoodType = findViewById(R.id.etFoodType);
        etQty = findViewById(R.id.etQty);
        add = (Button) findViewById(R.id.btnAdd);
//        ;modify = (Button) findViewById(R.id.btnModify)
        viewall = (Button) findViewById(R.id.btnViewAll);
        views = (Button) findViewById(R.id.btnView);
        loc = (Button) findViewById(R.id.btnLocation);
        add.setOnClickListener(this);
//        modify.setOnClickListener(this);
        viewall.setOnClickListener(this);
        views.setOnClickListener(this);
        loc.setOnClickListener(this);
        e = (EditText) findViewById(R.id.e1);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        txtDate=(EditText)findViewById(R.id.in_date);
//
        btnDatePicker.setOnClickListener(this);
        db = openOrCreateDatabase("FoodsDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS historys(tdate date,name VARCHAR,qty NUMERIC, location VARCHAR);");
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

        // on pressing btnSelect SelectImage() is called
        btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                SelectImage();
            }
        });

        // on pressing btnUpload uploadImage() is called
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                uploadImage();
            }
        });
    }
    private void SelectImage()
    {

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
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data)
    {

        super.onActivityResult(requestCode,
                resultCode,
                data);

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
            }

            catch (IOException e) {
                // Log the exception
                e.printStackTrace();
            }
        }
    }

    // UploadImage method
    private void uploadImage()
    {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            final ProgressDialog progressDialog
                    = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference ref
                    = storageReference
                    .child(
                            "images/"
                                    + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            ref.putFile(filePath)
                    .addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {

                                @Override
                                public void onSuccess(
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                                    progressDialog.dismiss();
                                    Toast
                                            .makeText(donate.this,
                                                    "Image Uploaded!!",
                                                    Toast.LENGTH_SHORT)
                                            .show();
                                }
                            })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e)
                        {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast
                                    .makeText(donate.this,
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
                                        UploadTask.TaskSnapshot taskSnapshot)
                                {
                                    double progress
                                            = (100.0
                                            * taskSnapshot.getBytesTransferred()
                                            / taskSnapshot.getTotalByteCount());
                                    progressDialog.setMessage(
                                            "Uploaded "
                                                    + (int)progress + "%");
                                }
                            });
        }
    }
    public void onClickSubmit(View v){

            int qty= Integer.parseInt(etQty.getText().toString());

    }

    @Override
    public void onClick(View view) {

        if (view == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog
                    (this, this, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if(view == loc)
        {
            String url;
            url = e.getText().toString();
            Intent mapIntent;
            Uri location;
            if(url.isEmpty()) {
                // Map point based on address
                location = Uri.parse("geo:0,0?q=Amrita Vishwa Vidyapeetham,+Ettimadai,+Coimbatore");
            }
            else {

                location = Uri.parse(url);
            }
            mapIntent = new Intent(Intent.ACTION_VIEW, location);
            startActivity(mapIntent);



        }

        if(view == views)
           {
               Cursor c = db.rawQuery("SELECT * FROM historys WHERE tdate='" + txtDate.getText() + "'", null);
               // Checking if no records found 
               if (c.getCount() == 0) {
                   showMessage("Error", "No records found");
                   return;
               }
               // Appending records to a string buffer 
               StringBuffer buffer = new StringBuffer();
               while (c.moveToNext())
               {
                   buffer.append("date: " + c.getString(0) + "\n");
                   buffer.append("FoodType: " + c.getString(1) + "\n");
                   buffer.append("Quantity: " + c.getString(2) + "\n\n");
                   buffer.append("Location: " + c.getString(3) + "\n\n");

               }
               // Displaying all records 
               showMessage("Today's History", buffer.toString());
           }

        if (view == add) {
            // Checking empty fields
            if (txtDate.getText().toString().trim().length() == 0 || etFoodType.getText().toString().trim().length() == 0 ||
                    etQty.getText().toString().trim().length() == 0||e.getText().toString().trim().length() == 0) {
                showMessage("Error", "Please enter all values");
                return;
            }
            // Inserting record 
            db.execSQL("INSERT INTO historys VALUES('" + txtDate.getText() + "','" + etFoodType.getText() +
                    "','" + etQty.getText() + "','"+ e.getText()+ "');");
            showMessage("Success", "Record added");
            clearText();
        }

        if (view == viewall) {
            // Retrieving all records 
            Cursor c = db.rawQuery("SELECT * FROM historys", null);
            // Checking if no records found 
            if (c.getCount() == 0) {
                showMessage("Error", "No records found");
                return;
            }
            // Appending records to a string buffer 
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext())
            {
                buffer.append("date: " + c.getString(0) + "\n");
                buffer.append("FoodType: " + c.getString(1) + "\n");
                buffer.append("Quantity: " + c.getString(2) + "\n\n");
                buffer.append("Location: " + c.getString(3) + "\n\n");

            }
            // Displaying all records 
            showMessage("Donation History", buffer.toString());
        }

    }
    public void showMessage(String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }
    public void clearText(){

        txtDate.setText("");
        etFoodType.setText("");
        etQty.setText("");
        etFoodType.requestFocus();
    }

    @Override
    public void onDateSet(DatePicker datePicker, int dayOfMonth, int monthOfYear, int year) {
        txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1)
                + "-" + year);
    }
}
