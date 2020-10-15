package com.example.morsel;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class vol_1_FirstFragment extends Fragment implements View.OnClickListener {

    View view;
    private DatabaseReference mDatabase, mdb;
    EditText et_loc, et_weight;
    Button btn_volunteer;
    SQLiteDatabase db;
    int reg_vol_id = 1;
    Integer id = 1;
    int f=1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.vol_1_fragment_first, container, false);
        et_loc = view.findViewById(R.id.et_loc);
        et_weight = view.findViewById(R.id.et_weight);
        btn_volunteer = view.findViewById(R.id.btn_volunteer);
        // perform setOnClickListener on first Button
        //db = new DatabaseHandler(getContext());
        //db = getActivity().openOrCreateDatabase("DATABASE",android.content.Context.MODE_PRIVATE ,null);
        btn_volunteer.setOnClickListener(this);
        return view;

    }

    @Override
    public void onClick(View view) {
        if (view.getId() == btn_volunteer.getId()) {
            mDatabase = FirebaseDatabase.getInstance().getReference().child("volunteer");
            //mdb = FirebaseDatabase.getInstance().getReference().child("volunteer").child("vol"+"_"+id);

                    f=1;
                    if (et_loc.getText().toString().isEmpty() || et_weight.getText().toString().isEmpty()) {
                        showMessage("Error", "Please enter all values");
                        f=0;
                    }
                    else{
                        String[] loc2 =(et_loc.getText().toString()).split(",");
                        if(loc2.length!=2)
                        {
                            Toast.makeText(getActivity(),"Enter a valid location",Toast.LENGTH_SHORT).show();
                            f=0;
                        }
                        if(loc2.length==2)
                        {
                            try {
                                double number = Double.parseDouble(loc2[0]);
                            } catch (NumberFormatException e) {
                                f=0;
                                Toast.makeText(getActivity(),"Enter a valid location",Toast.LENGTH_SHORT).show();
                            }
                            try {
                                double number1 = Double.parseDouble(loc2[1]);
                            } catch (NumberFormatException ex) {
                                f=0;
                                Toast.makeText(getActivity(),"Enter a valid location",Toast.LENGTH_SHORT).show();
                            }

                        }
                        if(Integer.parseInt(et_weight.getText().toString())<=0)
                        {
                            f=0;
                            Toast.makeText(getActivity(),"Enter a valid packets no",Toast.LENGTH_SHORT).show();
                        }
                        if(f==1) {
                            Integer weight = Integer.parseInt(et_weight.getText().toString());
                            String loc = et_loc.getText().toString();
                            mDatabase.setValue("vol" + id);
                            mDatabase.child("vol" + id).child("vol_id").setValue(id);
                            mDatabase.child("vol" + id).child("vol_loc").setValue(loc);
                            mDatabase.child("vol" + id).child("vol_weight").setValue(weight);
                            //Vol vol = new Vol(id, loc, weight);
                            //mDatabase.child("vol" + id).setValue(vol);


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
                            i1.putExtra("id_vol", String.valueOf(id));
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
