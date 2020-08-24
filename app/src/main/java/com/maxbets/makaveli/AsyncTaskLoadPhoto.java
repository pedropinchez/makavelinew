package com.maxbets.makaveli;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.widget.ImageView;


import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;



public class AsyncTaskLoadPhoto extends AsyncTask<String, String, Bitmap>
{
    private final WeakReference<ImageView> imageViewReference;
    Bitmap bitmap;

    public AsyncTaskLoadPhoto(ImageView imageView)
    {
        imageViewReference = new WeakReference<ImageView>(imageView);
    }

    @Override
    protected void onPreExecute()
    {
        super.onPreExecute();
    }
    protected Bitmap doInBackground(String... args) {
        try
        {
            bitmap = BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return bitmap;
    }

    protected void onPostExecute(Bitmap image)
    {
        if (imageViewReference != null)
        {
            ImageView imageView = imageViewReference.get();
            if (imageView != null)
            {
                if (bitmap != null)
                {
                    imageView.setImageBitmap(bitmap);
                }
                else
                {
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.support);
                    imageView.setImageDrawable(placeholder);
                }
            }
        }
    }
}
