package com.krev.trycrypt.asynctasks;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;

public class DownloadImageTask extends AsyncTask<String, Void, Bitmap> {
    private ImageView bmImage;
    private Consumer<?> consumer;

    public DownloadImageTask(ImageView bmImage) {
        this.bmImage = bmImage;
        this.consumer = null;
    }

    public DownloadImageTask(ImageView bmImage, Consumer<?> consumer) {
        this.bmImage = bmImage;
        this.consumer = consumer;
    }

    protected Bitmap doInBackground(String... urls) {
        String urldisplay = urls[0];
        Bitmap mIcon11 = null;
        try {
            InputStream in = new java.net.URL(urldisplay).openStream();
            mIcon11 = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
        }
        return mIcon11;
    }

    protected void onPostExecute(Bitmap result) {
        bmImage.setImageBitmap(result);
        if (consumer != null) {
            consumer.accept(null);
        }
    }
}