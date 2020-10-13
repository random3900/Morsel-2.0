package com.example.morsel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Registeration extends AppCompatActivity {
    EditText uname, password, email, address, phoneno;
    TextView result;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration);
        uname=findViewById(R.id.et_r_username);
        password = findViewById(R.id.et_r_password);
        email = findViewById(R.id.et_r_email);
        address = findViewById(R.id.et_r_address);
        phoneno= findViewById(R.id.et_r_phoneno);
        result = findViewById(R.id.resultView);

    }

    public void onClickRegister(View v){

        String getText=email.getText().toString();
        String Expn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        String pno = phoneno.getText().toString();
        if (uname.getText().toString().isEmpty() || password.getText().toString().isEmpty() ||
                email.getText().toString().isEmpty() || address.getText().toString().isEmpty() || phoneno.getText().toString().isEmpty()
        ) {
            result.setText("Please Fill All the Details");
        }
        else if(pno.length() < 10) {
                result.setText("Enter a valid phone number !!");
            }
        else if(!(getText.matches(Expn) && getText.length() > 0))
        {
            result.setText("Enter a valid email !!");
        }
        else
        {   result.setText("Successfully Registered!!");
            Intent i = new Intent(this, SecondScreen.class);
            startActivity(i);
        }



    }
    public void onClickLogin(View v){
        Intent i=new Intent(this, login.class);
        startActivity(i);
    }

}
