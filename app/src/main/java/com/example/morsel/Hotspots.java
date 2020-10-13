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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


public class Hotspots extends Fragment {

    public static class HotspotViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView avgnumTextView;
        TextView addressTextView;

        public HotspotViewHolder(View v) {
            super(v);
            nameTextView = (TextView) itemView.findViewById(R.id.hotspot_name);
            avgnumTextView = (TextView) itemView.findViewById(R.id.hotspot_avg_num);
            addressTextView = (TextView) itemView.findViewById(R.id.hotspot_address);
        }
    }

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
    private FirebaseRecyclerAdapter<Hotspot, HotspotViewHolder>
            mFirebaseAdapter;
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

        DatabaseReference hotspotsRef = mFirebaseDatabaseReference.child(HOTSPOTS_CHILD);
        FirebaseRecyclerOptions<Hotspot> options =
                new FirebaseRecyclerOptions.Builder<Hotspot>()
                        .setQuery(hotspotsRef, parser)
                        .build();

        Log.d("HOTSPOT","Inside Hotspot Reference Set");

        mFirebaseAdapter = new FirebaseRecyclerAdapter<Hotspot, HotspotViewHolder>(options) {
            @Override
            public HotspotViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
                LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
                Log.d("HOTSPOT","Inside View Holder");
                return new HotspotViewHolder(inflater.inflate(R.layout.item_hotspot, viewGroup, false));

            }

            @Override
            protected void onBindViewHolder(final HotspotViewHolder viewHolder,
                                            int position,
                                            Hotspot hotspot) {
                Log.d("HOTSPOT","Inside Bind View");
                if (hotspot.getName() != null) {
                    viewHolder.nameTextView.setText(hotspot.getName());
                    viewHolder.nameTextView.setVisibility(TextView.VISIBLE);

                    viewHolder.avgnumTextView.setText(Long.toString(hotspot.getAvgnum()));
                    viewHolder.avgnumTextView.setVisibility(TextView.VISIBLE);

                    viewHolder.addressTextView.setText(Double.toString(hotspot.getLat())+" "+Double.toString(hotspot.getLon()));
                    viewHolder.addressTextView.setVisibility(TextView.VISIBLE);

                }

            }
        };
        Log.d("HOTSPOT","Firebase Adapter set");

//        mFirebaseAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
//            @Override
//            public void onItemRangeInserted(int positionStart, int itemCount) {
//                super.onItemRangeInserted(positionStart, itemCount);
//                int hotspotCount = mFirebaseAdapter.getItemCount();
//                int lastVisiblePosition =
//                        mLinearLayoutManager.findLastCompletelyVisibleItemPosition();
//                // If the recycler view is initially being loaded or the
//                // user is at the bottom of the list, scroll to the bottom
//                // of the list to show the newly added message.
//                if (lastVisiblePosition == -1 ||
//                        (positionStart >= (hotspotCount - 1) &&
//                                lastVisiblePosition == (positionStart - 1))) {
//                    mHotspotRecyclerView.scrollToPosition(positionStart);
//                }
//            }
//        });
        Log.d("HOTSPOT",mFirebaseAdapter.toString());
        mHotspotRecyclerView.setAdapter(mFirebaseAdapter);
        Log.d("HOTSPOT",mHotspotRecyclerView.toString());

        return v;
    }
    @Override
    public void onPause() {
        mFirebaseAdapter.stopListening();
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        mFirebaseAdapter.startListening();
    }

}