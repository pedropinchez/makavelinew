package com.maxbets.makaveli.fragments;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.textfield.TextInputEditText;
import com.maxbets.makaveli.R;

import java.net.URLEncoder;

public class VipOdds extends Fragment {
    AdView mAdView, mAdViews;
    AdRequest adRequest;
    Button telegram,whatsapp;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_vipsodds, container, false);
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_vipsodds, null);
        super.onCreate(savedInstanceState);
        mAdView=view.findViewById(R.id.ad);

        telegram=view.findViewById(R.id.telegram);
        whatsapp=view.findViewById(R.id.whatsapp);
        whatsapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context;
                PackageManager packageManager = getActivity().getPackageManager();
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

        telegram.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent waIntent = new Intent(Intent.ACTION_SEND);
                waIntent.setType("text/plain");
                waIntent.setPackage("com.telegram");
                if (waIntent != null) {

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/joinchat/AAAAAD71gpgOmWG1E3SxVw"));
                    startActivity(intent);
                }
                else
                {
                    Toast.makeText(getActivity(), "Telegram is not installed", Toast.LENGTH_SHORT).show();
                }
            }
        });

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









        return  view;
    }
}
