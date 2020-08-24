package com.maxbets.makaveli;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class realtime extends AppCompatActivity {

    DatabaseReference reference;
    RecyclerView recyclerView;
    ArrayList<Profile> list;
    MyAdapter adapter;
    FirebaseFirestore firebaseFirestore;
    Query query;
    AdView mAdView, mAdViews;
    AdRequest adRequest;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_realtime);

        mAdViews= findViewById(R.id.ads);
        recyclerView =  findViewById(R.id.recycler);
        recyclerView.setLayoutManager( new LinearLayoutManager(this));


 //       reference = FirebaseDatabase.getInstance().getReference("USERS");
//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                list = new ArrayList<Profile>();
//                for(DataSnapshot dataSnapshot1: dataSnapshot.getChildren())
//                {
//                    Profile p = dataSnapshot1.getValue(Profile.class);
//                    list.add(p);
//                }
//                adapter = new MyAdapter(realtime.this,list);
//                recyclerView.setAdapter(adapter);
//
//
//         }

  //          @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//                Toast.makeText(realtime.this, "Opsss.... Something is wrong", Toast.LENGTH_SHORT).show();
//            }
//        });
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //Toast.makeText(, "toast success", Toast.LENGTH_SHORT).show();
            }
        });

        adRequest = new AdRequest.Builder().build();

        // mAdView = commentdialog.findViewById(R.id.adView);

        mAdViews.loadAd(adRequest);
        mAdViews.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                try {
                    mAdViews.setVisibility(View.VISIBLE);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                try {
                    AdRequest adRequest = new AdRequest.Builder().build();

                    mAdViews.loadAd(adRequest);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onAdClosed() {
                try {
                    AdRequest adRequest = new AdRequest.Builder().build();

                    mAdViews.loadAd(adRequest);
                } catch (Exception ignored) {
                }
            }
        });


        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("USER").orderBy("timestamp", Query.Direction.DESCENDING);
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Toast.makeText(realtime.this, "Error occured : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                  list = new ArrayList<>();

                    if (!queryDocumentSnapshots.isEmpty()) {

                        String id = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            id = documentSnapshot.getId();
                            Profile profile = documentSnapshot.toObject(Profile.class);
                            list.add(profile);
                            //String name = myListData.getUsername();

                        }


                        if (queryDocumentSnapshots.size() > 10) {

                            firebaseFirestore.collection("USERS").document(id).delete()
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {

                                            Toast.makeText(realtime.this, "Deleted.", Toast.LENGTH_SHORT).show();
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                    Toast.makeText(realtime.this, "Delete Failed.", Toast.LENGTH_SHORT).show();
                                }
                            });

                        }

                        if (isNetworkAvailable()) {


                            recyclerView.setAdapter(null);
                            adapter = new MyAdapter(realtime.this,list);
                            recyclerView.setHasFixedSize(true);
                            recyclerView.setLayoutManager(new LinearLayoutManager(realtime.this));
                            recyclerView.setAdapter(adapter);


                        } else {
                            Toast.makeText(realtime.this, "onClick：" + "network unavailable.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }
        });
   }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
        finish();
    }
    public boolean isNetworkAvailable() {


            ConnectivityManager connectionManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            try {
                if (connectionManager.getActiveNetworkInfo().isConnected()) {
                    Toast.makeText(realtime.this, "Data Connection On", Toast.LENGTH_SHORT).show();

                    return true;
                } else {
                    Toast.makeText(realtime.this, "Data Connection off", Toast.LENGTH_SHORT).show();

                    return false;
                }
            } catch (NullPointerException e) {
                Toast.makeText(realtime.this, "No Active Connection", Toast.LENGTH_SHORT).show();
                return false;
            }
    }
}