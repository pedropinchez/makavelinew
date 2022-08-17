package com.maxbets.makaveli.fragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.maxbets.makaveli.R;

import java.util.ArrayList;
import java.util.List;


public class Share extends Fragment {
    AdView mAdView, mAdViews;
    AdRequest adRequest;
    InterstitialAd interstitialAd;
    AdView adView;
    TextView buy;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_share, null);
        super.onCreate(savedInstanceState);
         buy=view.findViewById(R.id.buy);
        
        MobileAds.initialize(getActivity(), "ca-app-pub-5425147727091345~6303301938");
        interstitialAd = new InterstitialAd(getActivity());
        interstitialAd.setAdUnitId("ca-app-pub-5425147727091345/5264490348");
        AdRequest request = new AdRequest.Builder().build();
        interstitialAd.loadAd(request);
        interstitialAd.setAdListener(new AdListener(){
            public void onAdLoaded(){
                if (interstitialAd.isLoaded()) {
                    interstitialAd.show();
                }
            }
        });
        buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                PurchasesUpdatedListener purchasesUpdatedListener = new PurchasesUpdatedListener() {
                    @Override
                    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
                        // To be implemented in a later section.
                        List<String> skuList = new ArrayList<>();
                        skuList.add("premium_upgrade");
                        skuList.add("gas");
                        SkuDetailsParams.Builder params = SkuDetailsParams.newBuilder();
                        params.setSkusList(skuList).setType(BillingClient.SkuType.INAPP);
                        BillingClient billingClient = BillingClient.newBuilder(getActivity())
                                .enablePendingPurchases()
                                .build();
                        billingClient.querySkuDetailsAsync(params.build(),
                                new SkuDetailsResponseListener() {
                                    @Override
                                    public void onSkuDetailsResponse(BillingResult billingResult,
                                                                     List<SkuDetails> skuDetailsList) {
                                        // Process the result.
                                    }
                                });
                    }
                };

                BillingClient billingClient = BillingClient.newBuilder(getActivity())
                        .setListener(purchasesUpdatedListener)
                        .enablePendingPurchases()
                        .build();
                billingClient.startConnection(new BillingClientStateListener() {
                    @Override
                    public void onBillingSetupFinished(BillingResult billingResult) {
                        if (billingResult.getResponseCode() ==  BillingClient.BillingResponseCode.OK) {
                            // The BillingClient is ready. You can query purchases here.
                        }
                    }
                    @Override
                    public void onBillingServiceDisconnected() {
                        // Try to restart the connection on the next request to
                        // Google Play by calling the startConnection() method.
                    }
                });
                
            }
        });


        SharedPreferences sharedPref = this.getActivity().getSharedPreferences("", Context.MODE_PRIVATE);
        sharedPref = PreferenceManager.getDefaultSharedPreferences(getActivity());


        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        String shareBody = "Download2 ODDS Maxbet App from the playstore https://play.google.com/store/apps/details?id=" + getActivity();
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Kibabii App");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share via"));





        return  view;
    }

}
