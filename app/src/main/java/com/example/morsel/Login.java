package com.example.morsel;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.concurrent.Executor;
import java.util.regex.Pattern;

public class Login extends Fragment {
    private FirebaseAuth mAuth;

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);

        passwordTextInput = view.findViewById(R.id.password_text_input);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        usernameTextInput = view.findViewById(R.id.username_text_input);
        usernameEditText = view.findViewById(R.id.username_edit_text);
        MaterialButton nextButton = view.findViewById(R.id.next_button);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

        return view;
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
                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("Login", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                           Toast.makeText(getContext(), "Signin Successful", Toast.LENGTH_SHORT).show();
//                            ((NavigationHost) getContext()).navigateTo(new Hotspots(), false);
                           Intent i=new Intent(getActivity(),SecondScreen.class);
                            startActivity(i);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("Login", "signInWithEmail:failure", task.getException());
                            Toast.makeText(getActivity(), "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });
    }

    private void signOut() {
        mAuth.signOut();

    }

    public void onClickRegister(View v){
        ((NavigationHost) getContext()).navigateTo(new Registration(), false);
    }

}