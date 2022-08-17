package com.maxbets.makaveli;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AppCompatActivity;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Dets extends AppCompatActivity {
    TextView ttle,cont,time,date;
    ImageView img;

    ScrollableGridView simpleGridView;
    Intent parse;
    Intent bund;
    String tt,cn,tm,pm,jc;
    private ArrayList<MyListData> listData;
    AdView mAdView, mAdViews;
    AdRequest adRequest;
    Button telegram, whatsapp;
    TextView template;
    private static Context context;
    InterstitialAd interstitialAd;
    AdView adView;
    Toolbar toolbar;
    Button website;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailes);
        mAdViews= findViewById(R.id.ads);
        ttle = findViewById(R.id.txtTitle);
        cont = findViewById(R.id.txtcontent);
        time = findViewById(R.id.txttime);
        date=findViewById(R.id.date);
        whatsapp=findViewById(R.id.whatsapp);
        website=findViewById(R.id.website);
        template=findViewById(R.id.template);
        simpleGridView = findViewById(R.id.simpleGridView);
        telegram=findViewById(R.id.telegram);
        // Create an object of CustomAdapter and set Adapter to GirdView
        template.setMovementMethod(LinkMovementMethod.getInstance());
        MobileAds.initialize(this, "ca-app-pub-5425147727091345~6303301938");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-5425147727091345/4050684864");
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);
        interstitialAd.show();

        bund = getIntent();
            tt=bund.getStringExtra("Title");
            cn=bund.getStringExtra("Content");
            tm=bund.getStringExtra("Time");
            pm=bund.getStringExtra("date");
            jc=bund.getStringExtra("imageUrl") ;
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                //Toast.makeText(, "toast success", Toast.LENGTH_SHORT).show();
            }
        });
        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                waIntent.setPackage("com.telegram");
                if (waIntent != null) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/AAAAAD71gph5ijYEJTOLCg"));
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(Dets.this, "Telegram is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context;
                PackageManager packageManager = getPackageManager();
                Intent i = new Intent(Intent.ACTION_VIEW);
                String smsNumber = "254723395234";
                String message="I want to join 2 odds maxbet";

                try {
                    String url = "https://api.whatsapp.com/send?phone="+ smsNumber +"&text=" + URLEncoder.encode(message, "UTF-8");
                    i.setPackage("com.whatsapp");
                    i.setData(Uri.parse(url));
                    if (i.resolveActivity(packageManager) != null) {
                        startActivity(i);
                    }
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        });
        website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://makaveliodds.com/"));
                startActivity(intent);

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
      
         try {
             if (jc != null) {
                 List<String> items = Arrays.asList(jc.split("\\s*,\\s*"));

                 GridViewAdapter customAdapter = new GridViewAdapter(getApplicationContext(), R.layout.grid_view_items, items);
                 simpleGridView.setAdapter(customAdapter);
             } else {
                 
             }
         }
         catch (Exception e)
         {

         }
            //bund.getString("Img");
      

        ttle.setText(tt);
        cont.setText(cn);
        time.setText(tm);
        date.setText(pm);
    }

    @Override
    public void onBackPressed() {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            interstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdClosed() {
                    super.onAdClosed();
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            });
        }else{
            super.onBackPressed();
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }




          


    }
    @Override
    protected void onRestart() {
        super.onRestart();
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }
}
