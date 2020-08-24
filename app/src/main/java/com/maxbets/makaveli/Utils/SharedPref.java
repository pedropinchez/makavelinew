package com.maxbets.makaveli.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPref {

    private Context ctx;
    private SharedPreferences default_prefence;

    public SharedPref(Context context) {
        this.ctx = context;
        default_prefence = context.getSharedPreferences("makaveli", Context.MODE_PRIVATE);
    }







    public void setRatings(int ratings) {
        default_prefence.edit().putInt("ratings", ratings).apply();
    }

    public int getCount() {
        return default_prefence.getInt("count", 0);
    }

    public void setCount(int count) {
        default_prefence.edit().putInt("count", count).apply();
    }

  


}
