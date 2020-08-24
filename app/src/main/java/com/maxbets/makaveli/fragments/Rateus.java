package com.maxbets.makaveli.fragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;


import com.maxbets.makaveli.R;
import com.maxbets.makaveli.Utils.SharedPref;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;


public class Rateus extends Fragment {
private SharedPref sharedPref;
    AdView mAdView, mAdViews;
    AdRequest adRequest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_rate, container, false);
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_rate, null);

        mAdViews= view.findViewById(R.id.ads);
        super.onCreate(savedInstanceState);
      //  startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getActivity())));
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                Toast.makeText(getActivity(), "toast success", Toast.LENGTH_SHORT).show();
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


        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()));
        intent.setPackage("com.android.vending");



        startActivity(intent);








        return  view;
    }
    public void RateUsRequest(String title, String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(false);
        builder.setPositiveButton("Rate", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                rateUs();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                System.exit(0);
                getActivity().finish();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();

    }
    private void rateUs() {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(
                "https://play.google.com/store/apps/details?id=" + getActivity().getPackageName()));
        intent.setPackage("com.android.vending");

        sharedPref.setRatings(1);

        startActivity(intent);


    }
}
