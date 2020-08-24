package com.maxbets.makaveli;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {

    private ArrayList<MyListData> listData;
    static Context cnt;
    String timeString = "";
    FirebaseFirestore firebaseFirestore;
    FirebaseStorage firebaseStorage;
    StorageReference storageReference;

    public MyListAdapter(ArrayList<MyListData> listData, Context cnt) {
        this.listData = listData;
        this.cnt = cnt;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View listItem= layoutInflater.inflate(R.layout.post, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, int position) {
        final MyListData myListData = listData.get(position);
        holder.title.setText(myListData.getUsername());
        holder.content.setText(myListData.getAge());
        holder.date.setText(myListData.getDate());
        String url = myListData.getImageUrl();
        try {
            if (url != null) {
                holder.image.setVisibility(View.VISIBLE);
                Picasso.get().load(myListData.getImageUrl()).into(holder.image);
                ;
            }
            else{
                holder.image.setVisibility(View.GONE);
            }
        }
        catch (Exception e){

        }
       


     



        String dbTimestamp = myListData.getTimestamp();
        long dbTimestampLong = Long.parseLong(dbTimestamp);

        
       final String timeDifference = getTimeDifference(dbTimestampLong);

       holder.timestamp.setText(timeDifference);



       holder.view.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {



               Intent go = new Intent(cnt, Dets.class);
               go.putExtra("Title", myListData.getUsername());
               go.putExtra("Content",myListData.getAge());
               go.putExtra("Time",timeDifference);
               go.putExtra("date", myListData.getDate())  ;
               go.putExtra("imageUrl",  myListData.getImageUrl());
               holder.title.setTextColor(cnt.getResources().getColor( R.color.grey));

               //go.putExtra("Img","");
               cnt.startActivity(new Intent(go));
           }
       });

    

    }




    @Override
    public int getItemCount() {
        return listData.size();
    }




    


    public static class ViewHolder extends RecyclerView.ViewHolder {

        View view;
        TextView title,content,timestamp,date;
        ImageView image;
        
        public ViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title = itemView.findViewById(R.id.title);
            content = itemView.findViewById(R.id.content);
            timestamp = itemView.findViewById(R.id.timestamp);
            date = itemView.findViewById(R.id.date);
            image = itemView.findViewById(R.id.image);
        }
    }

    public String getTimeDifference(long time){

        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime-time;

        Log.d("dddd",currentTime+"/"+time+"/"+timeDifference);
        SimpleDateFormat sdfMinute = new SimpleDateFormat("mm");
        SimpleDateFormat sdfHour = new SimpleDateFormat("HH");
        SimpleDateFormat sdfDay = new SimpleDateFormat("dd");
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yy");

        if (timeDifference <= 600000){
            return "Just now";
        }else if (timeDifference <= 3.6e+6){
            timeString = sdfMinute.format(timeDifference);
            return timeString.replaceFirst("0","") + " minutes ago";
        }else if (timeDifference <= 8.64e+7){
            timeString = sdfHour.format(timeDifference);
            return timeString.replaceFirst("0","") + " hours ago";
        }else if (timeDifference <= 2.592e+9){
            timeString = sdfDay.format(timeDifference);
            return timeString.replaceFirst("0","") + " days ago";
        }else if (timeDifference > 2.592e+9){
            timeString = sdf.format(time);
            return timeString;
        }


        return "";
    }
}