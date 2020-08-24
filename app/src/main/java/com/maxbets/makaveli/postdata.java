package com.maxbets.makaveli;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.media.RingtoneManager;
import android.os.Bundle;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class postdata  extends AppCompatActivity {

    public static final int PICK_IMAGE = 1;
    private ImageView post_image;
    private EditText post_title, post_details;
    private ProgressDialog pdialog;
    private FirebaseAuth mAuth;
    private String finalUriList = null;
                          private List<Uri> uriList;
    Button submit_post_btn;
    private long ServerTimestamp;
    AdView mAdView, mAdViews;
    AdRequest adRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        //Finding View By Id
        mAuth = FirebaseAuth.getInstance();

        uriList = new ArrayList<>();

        Toolbar toolbar = findViewById(R.id.toolbar);
        post_title = findViewById(R.id.post_title);
        post_details = findViewById(R.id.post_details);
        post_image = findViewById(R.id.post_image);


        submit_post_btn = findViewById(R.id.submit_post_btn);


        submit_post_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit_post_btn.setEnabled(false);
                if (allClear()){

                    String postId = generateRandomNumber(6)+generateRandomNumber(5);
                    uploadPostImage(postId);
                }else{

                    submit_post_btn.setEnabled(true);
                }
            }
        });


        post_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent=new Intent();
//                intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
//                startActivityForResult(intent, Constants.REQUEST_CODE);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(postdata.this, android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(postdata.this, new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        pickimage();
                    }
                } else {
                    pickimage();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            super.onBackPressed();
        }

        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data != null && data.getClipData() != null) {
                int count = data.getClipData().getItemCount();
                for (int i = 0; i < count; i++) {

                    Uri selectedImage = data.getClipData().getItemAt(i).getUri();//As of now use static position 0 use as per itemcount.

                    uriList.add(selectedImage);
                    post_image.setImageURI(selectedImage);


                }
            }
        }

    }


    private void pickimage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE);
        

    }



    private boolean allClear() {
        if (uriList.size() == 0) {
            toast("Please select an Image");
            return false;
        }else if (TextUtils.isEmpty(post_title.getText().toString())){
            post_title.setError("Title can't be Empty");
            return false;
        }else if (TextUtils.isEmpty(post_details.getText().toString())){
            post_details.setError("Detail can't be Empty");
            return false;
        }else {
            return true;
        }
    }

    private void uploadPostImage(final String randomId) {

        submit_post_btn.setEnabled(false);

        pdialog = new ProgressDialog(postdata.this);
        pdialog.setMessage("Please wait...");
        pdialog.setIndeterminate(true);
        pdialog.setCanceledOnTouchOutside(false);
        pdialog.setCancelable(false);
        pdialog.show();



        if (uriList.size() != 0) {
            for (int i = 0; i < uriList.size(); i++) {

             final    int count = i;

                Uri singleUri = uriList.get(i);
                final StorageReference filePath = FirebaseStorage.getInstance().getReference().child("post_image/" + randomId + i+ ".png");

                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getApplication().getContentResolver(), singleUri);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                byte[] data = baos.toByteArray();
                UploadTask uploadTask = filePath.putBytes(data);

                uploadTask.addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(postdata.this, "Failed to upload your post. Please try again", Toast.LENGTH_SHORT).show();
                        submit_post_btn.setEnabled(true);
                    }
                });
                uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {

                                String uriString = uri.toString();
                                if (finalUriList == null){
                                    finalUriList = uriString;
                                }  else {
                                    finalUriList = finalUriList + "," + uriString;
                                }

                                List<String> items = Arrays.asList(finalUriList.split("\\s*,\\s*"));
                                for (int j = 0; j < items.size(); j++) {
                                    String link = items.get(j);
                                }
                                if (items.size() == uriList.size()) {
                                    savePostDetails(finalUriList);
                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(postdata.this, "Failed to upload your post. Please try again", Toast.LENGTH_SHORT).show();

                            }
                        });


                    }
                });
            }

        }else{
            Toast.makeText(this, "no image", Toast.LENGTH_SHORT).show();

            submit_post_btn.setEnabled(true);
        }
    }

    private void savePostDetails(String imageUrl) {
        String title = post_title.getText().toString();
        String details = post_details.getText().toString();
        String desc = details.substring(0, Math.min(details.length(), 250));

        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss yyyy-MM-dd ");

        String date = df.format(c);
        FirebaseFirestore firestore;

        long tsLong =System.currentTimeMillis();
        String ts = String.valueOf(tsLong);

//        String postId =  generateRandomNumber(6)+generateRandomNumber(5);
        Map<String, Object> post = new HashMap<>();


        post.put("imageUrl", imageUrl);
        post.put("timestamp", ts);
        post.put("date", date);
        post.put("age", details);
        post.put("username", title);



        FirebaseFirestore.getInstance().collection("USERS").document()
                .set(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        pdialog.dismiss();
                        post_title.setText("");
                        post_details.setText("");
                        post_image.setImageResource(R.color.grey);
                        buildLocalNotification("NEW MESSAGE ADDED" ,"Check out to our latest updates");
                        startActivity(new Intent(postdata.this, MainActivity.class));
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "Something went wrong :(", Toast.LENGTH_LONG).show();
                        finish();

                        submit_post_btn.setEnabled(true);
                    }
                });
    }

    private void toast(String message){
        Toast.makeText(postdata.this, message, Toast.LENGTH_SHORT).show();
    }

    public String generateRandomNumber(int lenght){
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder stringBuilder = new StringBuilder();
        Random random = new Random();

        while (stringBuilder.length() < lenght){
            int index = (int) (random.nextFloat() * characters.length());
            stringBuilder.append(characters.charAt(index));
        }
        return stringBuilder.toString();

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