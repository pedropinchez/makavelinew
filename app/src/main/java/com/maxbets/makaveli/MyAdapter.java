package com.maxbets.makaveli;

import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Profile> profiles;
    View view;
    String timeString = "";

    public MyAdapter(Context c , ArrayList<Profile> p)
    {
        context = c;
        profiles = p;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.post,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.title.setText(profiles.get(position).getUsername());
        holder.content.setText(profiles.get(position).getAge());
        holder.date.setText(profiles.get(position).getDate());
        String dbTimestamp = profiles.get(position).getTimestamp();
        long currentTimestamp = System.currentTimeMillis() ;
        long dbTimestampLong = Long.parseLong(dbTimestamp);
        long timestampDifference = currentTimestamp - dbTimestampLong;


        final String timeDifference = getTimeDifference(timestampDifference);

        holder.timestamp.setText(timeDifference);
        Picasso.get().load(profiles.get(position).getImageUrl()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder
    {
        TextView title,content,timestamp,date;
        ImageView image;
        Button btn;
        public MyViewHolder(View itemView) {
            super(itemView);
            view = itemView;
            title =  itemView.findViewById(R.id.title);
            content =  itemView.findViewById(R.id.content);
            timestamp =  itemView.findViewById(R.id.timestamp);
            date=  itemView.findViewById(R.id.date);
            image=itemView.findViewById(R.id.image);
        }
        public void onClick(final int position)
        {
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, position+" is clicked", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    public String getTimeDifference(long time){

        long currentTime = System.currentTimeMillis();
        long timeDifference = currentTime-time;

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
