package com.maxbets.makaveli.Utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class utils {
    public static String getUID(){
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(Constants.POSTS).push();
        String[] urlArray = databaseReference.toString().split("/");
        return urlArray[urlArray.length - 1];
    }
}