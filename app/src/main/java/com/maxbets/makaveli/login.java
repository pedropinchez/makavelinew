package com.maxbets.makaveli;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class login extends AppCompatActivity {
    EditText password,email;
    Button login;
    TextView tcreate;
    private FirebaseAuth mAuth;
    ProgressDialog pdialog;
    AdView mAdView, mAdViews;
    AdRequest adRequest;
    InterstitialAd interstitialAd;
    AdView adView;

    private static final String EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        password = findViewById(R.id.password);
           mAdViews=findViewById(R.id.ads);
        email = findViewById(R.id.email);
        login = findViewById(R.id.login);
        //pdialog=findViewById(R.id.login_progress);

        mAuth = FirebaseAuth.getInstance();
        MobileAds.initialize(this, "ca-app-pub-5425147727091345~6303301938");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-5425147727091345/4361143221");
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);
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


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
                pdialog = new ProgressDialog(login.this);
                pdialog.setMessage("Please wait...");
                pdialog.setIndeterminate(true);
                pdialog.setCanceledOnTouchOutside(false);
                pdialog.setCancelable(false);
                pdialog.show();


            }
        });
    }
    public void InitializeAds() {
        MobileAds.initialize(this, "ca-app-pub-5425147727091345~6303301938");
        interstitialAd = new InterstitialAd(this);
        interstitialAd.setAdUnitId("ca-app-pub-5425147727091345/4361143221");
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);




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
                }
            });
        }else{
            super.onBackPressed();
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        }


    }
    private void login(){
        String Email=email.getText().toString();
        String Password=password.getText().toString();
        if(!TextUtils.isEmpty(Email)&& !TextUtils.isEmpty(Password)) {

            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(login.this, "successful", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                InitializeAds();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.

                                Toast.makeText(login.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                                updateUI(null);
                            }

                            // ...
                        }
                    });
        }
        else{
            Toast.makeText(login.this,"empty inputs",Toast.LENGTH_SHORT).show();
        }
    }
    public void  updateUI(FirebaseUser account) {
        if (account != null) {
            Toast.makeText(this, "You Signed In successfully", Toast.LENGTH_LONG).show();
            startActivity(new Intent(this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "You arent registered,kindly sign up", Toast.LENGTH_LONG).show();
            //startActivity(new Intent(getApplicationContext(),MainActivity.class));
            pdialog.dismiss();
        }
    }
    @Override
    protected void onStart() {
        super.onStart();

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {

            startActivity(new Intent(login.this, MainActivity.class));
            finish();

        }
    }


}