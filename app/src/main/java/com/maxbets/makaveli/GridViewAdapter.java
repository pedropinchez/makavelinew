package com.maxbets.makaveli;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.squareup.picasso.Picasso;

import java.util.List;

public class GridViewAdapter extends ArrayAdapter {

    private List<String> uriStringList;
    private Context context;


    public GridViewAdapter(@NonNull Context context, int resource, List<String> uriStringList) {
        super(context, resource);
        this.uriStringList = uriStringList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return uriStringList.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        v = inflater.inflate(R.layout.grid_view_items, null);
        ImageView imageView = v.findViewById(R.id.icon);
        String imageLink = uriStringList.get(position);
        Log.d("sssssssss"+position, imageLink);
        try {
            if (uriStringList != null) {
                Picasso.get().load(imageLink).into(imageView);;
            }
            else{
           imageView.setVisibility(View.GONE);
            }
        }
        catch (Exception e){

        }
       

        
        return v;

    }

}