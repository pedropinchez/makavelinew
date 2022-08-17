package com.maxbets.makaveli.fragments;


import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.maxbets.makaveli.R;

import java.util.Calendar;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class ContactUs extends Fragment {
    AdView mAdView, mAdViews;
    AdRequest adRequest;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_contact, null);
        super.onCreate(savedInstanceState);
        mAdView=view.findViewById(R.id.ad);
        mAdViews= view.findViewById(R.id.ads);
        MobileAds.initialize(getActivity(), new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
              //  Toast.makeText(getActivity(), "toast success", Toast.LENGTH_SHORT).show();
            }
        });

        adRequest = new AdRequest.Builder().build();

        // mAdView = commentdialog.findViewById(R.id.adView);
       // mAdView.loadAd(adRequest);
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


        Element adsElement = new Element();
        adsElement.setTitle("Bet with us");

        View aboutPage = new AboutPage(getActivity())
                .isRTL(false)
                .enableDarkMode(true)
                .setDescription("The Tipstar You Can Trust. \n Bet Responsibly")
                .setCustomFont("fonts/slimjim.ttf") // or Typeface
                .addItem(new Element().setTitle("Version 6.2"))
                .addItem(new Element().setTitle("2 ODDS MAXBET"))
                .addItem(getCopyRightsElement())
                .addItem(adsElement)
                .addGroup("CLick the website to join our telegram")
                .addItem(new Element().setTitle("Telegram Link below"))
                .addWebsite("https://makaveliodds.com/")

                .addEmail("pedrompinchez100@gmail.com.com")
                .addFacebook("pedro m pinchez")
                .addTwitter("pedro m pinchez")
                .addYoutube("UCdPQtdWIsg7_pi4mrRu46vA")
                .addPlayStore("com.ideashower.readitlater.pro")
                .addInstagram("pedro m pinchez")
                .create();
        getActivity().setContentView(aboutPage);









        return  view;
    }
    Element getCopyRightsElement() {
        Element copyRightsElement = new Element();
        final String copyrights = String.format("COPYRIGHT@MAKAVELI", Calendar.getInstance().get(Calendar.YEAR));
        copyRightsElement.setTitle(copyrights);
        copyRightsElement.setIconDrawable(R.drawable.support);
        copyRightsElement.setAutoApplyIconTint(true);
        copyRightsElement.setIconTint(mehdi.sakout.aboutpage.R.color.about_item_icon_color);
        copyRightsElement.setIconNightTint(android.R.color.white);
        copyRightsElement.setGravity(Gravity.CENTER);
        copyRightsElement.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(), copyrights, Toast.LENGTH_SHORT).show();
            }
        });
        return copyRightsElement;
    }
    

}
