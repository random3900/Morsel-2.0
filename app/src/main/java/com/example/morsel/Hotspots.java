package com.example.morsel;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class Hotspots extends Fragment {



    private static final String TAG = "HOT";
    public static final String HOTSPOTS_CHILD = "hotspot list";
    private static final int REQUEST_INVITE = 1;
    public static final String ANONYMOUS = "anonymous";
    private String mUsername;
    private SharedPreferences mSharedPreferences;
    private GoogleSignInClient mSignInClient;
    private static final String HOTSPOT_URL = "https://morsel-a2477.firebaseio.com/hotspot%20list";

    private RecyclerView mHotspotRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;

    // Firebase instance variables
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private DatabaseReference mFirebaseDatabaseReference;
//    private FirebaseRecyclerAdapter<Hotspot, HotspotViewHolder>
//            mFirebaseAdapter;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d("HOTSPOT","Inside Oncreate");

        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_hotspots, container, false);
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        // Set default username is anonymous.
        mUsername = ANONYMOUS;

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mSignInClient = GoogleSignIn.getClient(getContext(), gso);

        // Initialize ProgressBar and RecyclerView.
        mHotspotRecyclerView = (RecyclerView) v.findViewById(R.id.hotspot_recycler_view);
        mLinearLayoutManager = new LinearLayoutManager(getContext());
        mLinearLayoutManager.setStackFromEnd(true);
        mHotspotRecyclerView.setLayoutManager(mLinearLayoutManager);

        //FIREBASE AUTH
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            // Not signed in, launch the Sign In activity
            ((NavigationHost) getContext()).navigateTo(new Login(), false);
        }
        Log.d("HOTSPOT","User Exists");

        mFirebaseDatabaseReference = FirebaseDatabase.getInstance().getReference();
        SnapshotParser<Hotspot> parser;
        parser = new SnapshotParser<Hotspot>() {
            @Override
            public Hotspot parseSnapshot(DataSnapshot dataSnapshot) {
                Hotspot hotspot = dataSnapshot.getValue(Hotspot.class);
                if (hotspot != null) {
                    hotspot.setId(dataSnapshot.getKey());
                }
                return hotspot;
            }
        };

        Log.d("HOTSPOT","Snapshot Parser set");

        DatabaseReference hotspotsRefCity = mFirebaseDatabaseReference.child(HOTSPOTS_CHILD).child("Chennai");


        hotspotsRefCity.addValueEventListener(new ValueEventListener() {
                                              @Override
                                              public void onDataChange(DataSnapshot snapshot) {
                                                  final ArrayList<Hotspot> hl = new ArrayList<>();
                                                  for (DataSnapshot area : snapshot.getChildren()) {
                                                      for (DataSnapshot hotspot : area.getChildren()) {
                                                          Hotspot h_temp;
                                                          int a = Integer.parseInt(hotspot.child("avgnum").getValue().toString());
                                                          double lat = Double.parseDouble(hotspot.child("lat").getValue().toString());
                                                          double lon = Double.parseDouble(hotspot.child("long").getValue().toString());
                                                          String n = hotspot.child("name").getValue().toString();
                                                          h_temp = new Hotspot(a, lat, lon, n);
                                                          hl.add(h_temp);
                                                      }
                                                  }
                                                  HotspotAdapter h_adapter = new HotspotAdapter(hl);
                                                  mHotspotRecyclerView.setAdapter(h_adapter);
                                              }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getContext(),"Database error",Toast.LENGTH_SHORT).show();
            }
                                          });


        return v;
    }
    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

}