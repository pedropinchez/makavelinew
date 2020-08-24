package com.maxbets.makaveli;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddActivity extends AppCompatActivity {

    FirebaseFirestore db;

    EditText name_et, age_et;
    Button add_btn;
    private ProgressDialog pdialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        db = FirebaseFirestore.getInstance();

        name_et = findViewById(R.id.post_title);
        age_et = findViewById(R.id.post_details);
        add_btn = findViewById(R.id.submit);

        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String title = name_et.getText().toString();
                String desc = age_et.getText().toString();

                if(TextUtils.isEmpty(title) || TextUtils.isEmpty(desc)){
                    Toast.makeText(AddActivity.this, "All fields are mandatory.", Toast.LENGTH_SHORT).show();
                } else {
                    Date c = Calendar.getInstance().getTime();
                    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

                    String date = df.format(c);
                    long tsLong = System.currentTimeMillis() ;
                    String ts = String.valueOf(tsLong);
                    pdialog = new ProgressDialog(AddActivity.this);
                    pdialog.setMessage("Please wait...");
                    pdialog.setIndeterminate(true);
                    pdialog.setCanceledOnTouchOutside(false);
                    pdialog.setCancelable(false);
                    pdialog.show();
                    Map<String, Object> post = new HashMap<>();



                    post.put("timestamp", ts);
                    post.put("date", date);
                    post.put("age", desc);
                    post.put("username", title);



                    FirebaseFirestore.getInstance().collection("USERS").document()
                            .set(post)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    //buildLocalNotification("NEW MESSAGE ADDED" ,"Check out to our latest updates");
                                    startActivity(new Intent(AddActivity.this, MainActivity.class));
                                    buildLocalNotification("NEW MESSAGE ADDED" ,"Check out to our latest updates");
                                    finish();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                                    finish();


                                }
                            });

                   // MyListData note = new MyListData(name,age,date,ts,"");


//                    db.collection("USER").document().set(note)
//                            .addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void aVoid) {
//
//                                    Toast.makeText(AddActivity.this, "Add Successful.", Toast.LENGTH_SHORT).show();
//                                    pdialog.dismiss();
//                                    finish();
//
//
//                                }
//                            }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                                              pdialog.dismiss();
//                            Toast.makeText(AddActivity.this, "Add Failed.", Toast.LENGTH_SHORT).show();
//
//                        }
//                    });
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(getApplicationContext(),MainActivity.class));

    }
    private void buildLocalNotification(String title, String message) {

        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        String channelId = getString(R.string.default_notification_channel_id);
        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, channelId)
                        .setSmallIcon(R.drawable.logo)
                        .setSound(defaultSoundUri)
                        .setContentTitle(title)
                        .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);


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
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.createNotificationChannel(channel);

    }
}
