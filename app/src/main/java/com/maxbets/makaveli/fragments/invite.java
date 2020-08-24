package com.maxbets.makaveli.fragments;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.maxbets.makaveli.R;


public class invite extends Fragment {
          Button feedback;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_disclaimer, container, false);
        feedback= feedback.findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try
                {
                    Intent toEmail = new Intent("android.intent.action.VIEW", Uri.parse("mailto:pedrompinchez100@gmail.com"));
                    startActivity(toEmail);

                }
                catch (ActivityNotFoundException paramAnonymousView) {

                    Toast.makeText(getActivity(), "Unable to connect to Email", Toast.LENGTH_LONG).show();

                }
            }
        });









        return  view;
    }

}
