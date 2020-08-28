package com.maxbets.makaveli;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;

public class filechooser extends AppCompatActivity {
         Button submit,submitm,submit_post,home;
    InterstitialAd interstitialAd;
    AdView adView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filechooser);
        submit=findViewById(R.id.submit);
        submit_post=findViewById(R.id.submit_post);
        submitm=findViewById(R.id.submitm);
        home=findViewById(R.id.home);

        MobileAds.initialize(this, "ca-app-pub-5425147727091345~6303301938");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-5425147727091345/3036646786");
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);
          home.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  startActivity(new Intent(getApplicationContext(),MainActivity.class));
              }
          });
        submitm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),postdata.class));
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),AddActivity.class));
            }
        });
        submit_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(),Upload.class));
            }
        });
    }



    @Override
    public void onBackPressed() {

        interstitialAd.setAdListener(new AdListener(){
            public void onAdLoaded(){
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
                else
                {
                   filechooser.super.onBackPressed();
                }
            }


            @Override
            public void onAdFailedToLoad(int i) {
                super.onAdFailedToLoad(i);
                startActivity(new Intent(getApplicationContext(),MainActivity.class));
            }

            @Override
            public void onAdClosed() {


                filechooser.super.onBackPressed();
            }
        });


    }
}
