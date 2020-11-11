package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

public class volCnf extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String link=null;
    String Expn =
            "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    +"((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    +"([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    +"[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    +"([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
    TextInputLayout passwordTextInput ;
    TextInputEditText passwordEditText;
    TextInputLayout usernameTextInput ;
    TextInputEditText usernameEditText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vol_cnf);

        passwordTextInput = findViewById(R.id.password_text_input1);
        passwordEditText = findViewById(R.id.password_edit_text1);
        usernameTextInput = findViewById(R.id.username_text_input1);
        usernameEditText = findViewById(R.id.username_edit_text1);
        MaterialButton nextButton = findViewById(R.id.cnf_button);

        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(getIntent())
                .addOnSuccessListener(this, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        link=getIntent().getData().toString();
                        Toast.makeText(getApplicationContext(),link,Toast.LENGTH_SHORT).show();

                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();
                            String l = deepLink.getQueryParameter("link");
                            Toast.makeText(getApplicationContext(),deepLink.toString()+" "+l,Toast.LENGTH_SHORT).show();
                        }


                        // Handle the deep link. For example, open the linked
                        // content, or apply promotional credit to the user's
                        // account.
                        // ...

                        // ...
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("failed", "getDynamicLink:onFailure", e);
                    }
                });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordValid(passwordEditText.getText()))
                    signIn(usernameEditText.getText().toString(), passwordEditText.getText().toString());
            }
        });

        passwordEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                if (isPasswordValid(passwordEditText.getText())) {
                    passwordTextInput.setError(null); //Clear the error
                }
                return false;
            }

        });
        mAuth = FirebaseAuth.getInstance();
        String password;
        String email;
    }

    private boolean isPasswordValid(@Nullable Editable text) {
        boolean to_ret = true;
        if(passwordEditText.getText().toString().isEmpty()){
            passwordTextInput.setError("Please enter a password");
            to_ret = false;
        }
        if(usernameEditText.getText().toString().isEmpty()){
            usernameTextInput.setError("Please enter an email ID");
            to_ret = false;
        }
        else if(!(usernameEditText.getText().toString().matches(Expn)))
        {
            usernameEditText.setError("Enter a valid email !!");
            to_ret = false;
        }
        return to_ret;
    }
    private void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(), "Signin Successful", Toast.LENGTH_SHORT).show();
//                            ((NavigationHost) getContext()).navigateTo(new Hotspots(), false);
                            int startIndex = link.indexOf("https://morsel.com/trip/");
                            String temp="https://morsel.com/trip/";
                            startIndex+=temp.length();
                            int endIndex = link.indexOf(" ", startIndex);
                            if (endIndex == -1) {
                                endIndex =link.length();
                            }
                            String tripid = link.substring(startIndex, endIndex);
                            Intent j=new Intent(volCnf.this,MsgVolStatus.class);
                            j.putExtra("tripid",tripid);
                            Toast.makeText(getApplicationContext(),tripid,Toast.LENGTH_SHORT).show();
                            startActivity(j);

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getApplicationContext(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            passwordTextInput.setError("Incorrect Email or Password");
                            usernameTextInput.setError("Incorrect Email or Password");

                            //updateUI(null);
                        }
                    }
                });
    }

}