package com.maxbets.makaveli.fragments;


import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.maxbets.makaveli.MainActivity;
import com.maxbets.makaveli.MyListAdapter;
import com.maxbets.makaveli.MyListData;
import com.maxbets.makaveli.R;
import com.maxbets.makaveli.Utils.SharedPref;
import com.maxbets.makaveli.filechooser;
import com.maxbets.makaveli.login;

import java.util.ArrayList;

import static androidx.constraintlayout.widget.Constraints.TAG;


public class Home extends Fragment {
    ArrayList<MyListData> listdata;

    ArrayList<String> documentIds;
    Button add;

    FirebaseFirestore firebaseFirestore;
    RecyclerView recyclerView;
    CollectionReference collectionReference;
    Query query;
    AdView mAdView, mAdViews;
    AdRequest adRequest;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = view.findViewById(R.id.recycler);
        mAdView = view.findViewById(R.id.ad);

        listdata = new ArrayList<>();
        documentIds = new ArrayList<>();
        firebaseFirestore = FirebaseFirestore.getInstance();
        query = firebaseFirestore.collection("USERS").orderBy("timestamp", Query.Direction.DESCENDING);
         firebaseFirestore.collection("USERS");

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }

                @Override
                public void onSwiped(final RecyclerView.ViewHolder viewHolder, int direction) {
                    firebaseFirestore.collection("USERS")
                            .document(documentIds.get(viewHolder.getAdapterPosition()))
                            .delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {


                                    Toast.makeText(getActivity(), "deleted"+ viewHolder.getAdapterPosition(), Toast.LENGTH_SHORT).show();
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getActivity(), "failure", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }).attachToRecyclerView(recyclerView);
        }
        else{



        }

        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
            }
        });

        adRequest = new AdRequest.Builder().build();

        // mAdView = commentdialog.findViewById(R.id.adView);
        mAdView.loadAd(adRequest);
        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                try {
                    mAdView.setVisibility(View.VISIBLE);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                try {
                    AdRequest adRequest = new AdRequest.Builder().build();

                    mAdView.loadAd(adRequest);
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onAdClosed() {
                try {
                    AdRequest adRequest = new AdRequest.Builder().build();

                    mAdView.loadAd(adRequest);
                } catch (Exception ignored) {
                }
            }
        });


        return view;


    }


    public void loadData() {

        if (isAdded() && getActivity() != null) {

            query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                @Override
                public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Toast.makeText(getActivity(), "Error occured : " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    } else {
                        listdata = new ArrayList<>();

                        if (!queryDocumentSnapshots.isEmpty()) {

                            String id = "";

                            for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                                id = documentSnapshot.getId();
                                MyListData myListData = documentSnapshot.toObject(MyListData.class);
                                listdata.add(myListData);
                                //String name = myListData.getUsername();

                                documentIds.add(id);

//                                SharedPref sharedPref=new SharedPref(getActivity());
//                                int previouscount=sharedPref.getCount();
//                                int newcounts=queryDocumentSnapshots.size();
//                                int difference =newcounts-previouscount;
//                               // Toast.makeText(getActivity(), "new count" +difference, Toast.LENGTH_SHORT).show();
//                                Log.e(TAG, "onEvent: "+previouscount );
//                                Log.e(TAG, "onEvent: "+newcounts );
//                                int count = queryDocumentSnapshots.size();
//                                sharedPref.setCount(count);
                            }

                            if (IsConnected()) {


                                recyclerView.setAdapter(null);
                                MyListAdapter adapter = new MyListAdapter(listdata, getActivity());
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(adapter);

                            } else {
                                recyclerView.setAdapter(null);
                                MyListAdapter adapter = new MyListAdapter(listdata, getActivity());
                                recyclerView.setHasFixedSize(true);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                                recyclerView.setAdapter(adapter);
                                Toast.makeText(getActivity(), "Cant Update：" + "network unavailable.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            });
        }

    }

    @Override
    public void onStart() {
        super.onStart();

        loadData();
    }

    private boolean IsConnected() {

        try {


            ConnectivityManager connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
            return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                    connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
        } catch (Exception ignored) {
            return true;
        }
    }


    
    private void buildLocalNotification(String title, String message) {

        Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getActivity().getApplicationContext(), 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(getActivity(), channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setSound(defaultSoundUri)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);


        /**
         * Since Android Oreo
         */

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {

            createNotificationChannel(channelId, notificationManager);

        }

        assert notificationManager != null;
        notificationManager.notify((int) System.currentTimeMillis(), notificationBuilder.build());
    }

    @SuppressLint("NewApi")
    public void createNotificationChannel(String channelId, NotificationManager notificationManager) {


        @SuppressLint("WrongConstant")
        NotificationChannel channel = new NotificationChannel(channelId, getString(R.string.app_name),
                NotificationManager.IMPORTANCE_MAX);
        AudioAttributes att = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SPEECH)
                .build();
        assert notificationManager != null;

        channel.setLightColor(Color.parseColor("#F1E605"));

        channel.canShowBadge();
        channel.enableVibration(true);
        notificationManager = (NotificationManager) getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }
    public void dialogBoxGuestLogin() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
        alertDialog.setTitle("Access Denied")
                .setMessage("You are logged in as a guest user. Access is only allowed to Admin")

                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                    }
                }).setNeutralButton("I am admin", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


                startActivity(new Intent(getActivity(), login.class));

            }
        })
                .setCancelable(false)
                .show();

    }


}



