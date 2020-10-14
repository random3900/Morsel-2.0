package com.example.morsel;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.regex.Pattern;

public class Registration extends Fragment {
    TextInputLayout uname_ti, password_ti, email_ti, address_ti, phoneno_ti;
    TextInputEditText uname_et, password_et, email_et, address_et, phoneno_et;
    MaterialButton registerButton;

    private FirebaseAuth mAuth;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view  = inflater.inflate(R.layout.fragment_registration, container, false);
        uname_et=view.findViewById(R.id.et_r_username);
        password_et = view.findViewById(R.id.et_r_password);
        email_et = view.findViewById(R.id.et_r_email);
        address_et = view.findViewById(R.id.et_r_address);
        phoneno_et= view.findViewById(R.id.et_r_phoneno);
        uname_ti=view.findViewById(R.id.tv_r_username);
        password_ti = view.findViewById(R.id.tv_r_password);
        email_ti = view.findViewById(R.id.tv_r_email);
        address_ti = view.findViewById(R.id.tv_r_address);
        phoneno_ti= view.findViewById(R.id.tv_r_phoneno);
        mAuth = FirebaseAuth.getInstance();
        registerButton = view.findViewById(R.id.register_button);

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("REG", "Inside Onclick Register");
                register(email_et.getText().toString(), password_et.getText().toString());

            }
        });
        return view;
    }

    private void register(String email, String password){
        if(validate()){
            Log.d("REG", "Validate = True");
            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d("REG", "createUserWithEmail:success");
                                Toast.makeText(getActivity(), "Successfully Registered!!!",
                                        Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();

                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w("REG", "createUserWithEmail:failure", task.getException());
                                Toast.makeText(getActivity(), "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }

                        }
                    });
        }
    }

    public boolean validate(){
        Log.d("REG", "Inside Validate");
        boolean to_ret = true;
        String getText=email_et.getText().toString();
        String Expn =
                "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                        +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                        +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                        +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                        +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
        final Pattern PASSWORD_PATTERN =
                Pattern.compile("^" +
                        "(?=.*[0-9])" +         //at least 1 digit
                        "(?=.*[a-z])" +         //at least 1 lower case letter
                        "(?=.*[A-Z])" +         //at least 1 upper case letter
                        "(?=.*[a-zA-Z])" +      //any letter
                        "(?=.*[@#$%^&+=])" +    //at least 1 special character
                        "(?=\\S+$)" +           //no white spaces
                        ".{4,}" +               //at least 4 characters
                        "$");
        String pno = phoneno_et.getText().toString();

        if (uname_et.getText().toString().isEmpty()){
            uname_ti.setError("Please enter a username");
            to_ret = false;
        }

        if(password_et.getText().toString().isEmpty()){
            password_ti.setError("Please enter a password");
            to_ret = false;
        }
        else if(!PASSWORD_PATTERN.matcher(password_et.getText().toString()).matches())
        {
            password_ti.setError("Weak Password !!!");
            to_ret = false;
        }
        if(email_et.getText().toString().isEmpty()){
            email_ti.setError("Please enter an email ID");
            to_ret = false;
        }
        if((email_et.getText().toString().isEmpty())){
            email_ti.setError("Please enter an email ID");
            to_ret = false;
        }
        else if(!(getText.matches(Expn) && getText.length() > 0))
        {
            email_ti.setError("Enter a valid email !!");
            to_ret = false;
        }

        if(address_et.getText().toString().isEmpty()){
            address_ti.setError("Please enter an address");
            to_ret = false;
        }

        if(phoneno_et.getText().toString().isEmpty()){
            phoneno_ti.setError("Please enter a phone number");
            to_ret = false;
        }
        else if(pno.length() < 10) {
            phoneno_ti.setError("Please enter a valid phone number");
            to_ret = false;
        }

        return to_ret;
    }
    public void onClickLogin(View v){
        ((NavigationHost) getContext()).navigateTo(new Login(), false);
    }
}