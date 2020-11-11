package com.example.morsel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SetModerator extends AppCompatActivity {
    TextInputLayout uname_ti;
    TextInputEditText uname_et;
    MaterialButton grantButton, revokeButton;
    private DatabaseReference mDB;
    int btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_moderator);

        uname_et=findViewById(R.id.et_sm_username);
        uname_ti=findViewById(R.id.tv_sm_username);
        grantButton = findViewById(R.id.grant_button);
        revokeButton = findViewById(R.id.revoke_button);

        grantButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("REG", "Inside Onclick Register");
                btn = 1;
                grantOrRevokeAccess();
            }
        });

        revokeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("REG", "Inside Onclick Register");
                btn = 0;
                grantOrRevokeAccess();

            }
        });
    }

    private void grantOrRevokeAccess() {
        Log.d("REG", "Inside Onclick Register");
        FirebaseDatabase.getInstance().getReference().child("Users").orderByChild("uName").equalTo(uname_et.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("REG", "Inside Onclick Register");

                for (DataSnapshot childDataSnapshot : dataSnapshot.getChildren()) {
                    Log.d("REG", "Inside Onclick Register");
                    uname_ti.setError("");
                    if(btn==0) {
                        childDataSnapshot.child("isMod").getRef().setValue(false);
                        Toast.makeText(getApplicationContext(),"Moderator Access Revoked", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        childDataSnapshot.child("isMod").getRef().setValue(true);
                        Toast.makeText(getApplicationContext(),"Moderator Access Granted", Toast.LENGTH_SHORT).show();
                    }
                }
                if(dataSnapshot.getChildrenCount()==0)
                    uname_ti.setError("Username doesn't exist");

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

}