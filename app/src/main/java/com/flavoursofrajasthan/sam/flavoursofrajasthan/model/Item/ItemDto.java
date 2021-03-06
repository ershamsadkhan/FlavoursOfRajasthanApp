package com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Item;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage.ImageCache;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.adapter.CustomListAdapter;
import com.flavoursofrajasthan.sam.flavoursofrajasthan.model.Configuration.Settings;

import org.apache.http.HttpStatus;

import java.io.InputStream;
import java.io.Serializable;
import java.net.HttpURLConnection;
import java.net.URL;

import static android.R.attr.name;

public class ItemDto  {

    public Bitmap image;
    public CustomListAdapter sta;
    public Bitmap getImage() {
        return image;
    }
    public ImageCache imageCache;

    public CustomListAdapter getAdapter() {
        return sta;
    }

    public void setAdapter(CustomListAdapter sta) {
        this.sta = sta;
    }
    public void loadImage(CustomListAdapter sta) {
        // HOLD A REFERENCE TO THE ADAPTER
        this.sta = sta;
        imageCache=ImageCache.getInstance();

        if (ImageUrl != null && !ImageUrl.equals("")) {
            image=imageCache.getCacheFile(ImageUrl);
            if(image!=null){
                sta.notifyDataSetChanged();
            }
            else {
                new ImageLoadTask().execute(ImageUrl);
            }
        }
    }
    public long Itemid;
    public long Categoryid;
    public String ItemHeader;
    public String ItemDescription;
    public long QuaterPrice;
    public long HalfPrice;
    public long FullPrice;
    public String ImageUrl;


    // ASYNC TASK TO AVOID CHOKING UP UI THREAD
    private class ImageLoadTask extends AsyncTask<String, String, Bitmap> {

        @Override
        protected void onPreExecute() {
            Log.i("ImageLoadTask", "Loading image...");
        }

        // PARAM[0] IS IMG URL
        protected Bitmap doInBackground(String... param) {
            Log.i("ImageLoadTask", "Attempting to load image URL: " + param[0]);
            try {
                Bitmap b = downloadBitmap(Settings.BaseApiUrl+"/api/Image?ImageName="+param[0]);
                return b;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }

        protected void onProgressUpdate(String... progress) {
            // NO OP
        }

        protected void onPostExecute(Bitmap ret) {
            if (ret != null) {
                Log.i("ImageLoadTask", "Successfully loaded " + name + " image");
                image = ret;
                if (sta != null) {
                    // WHEN IMAGE IS LOADED NOTIFY THE ADAPTER
                        imageCache.saveCacheFile(ImageUrl, image);
                    sta.notifyDataSetChanged();
                }
            } else {
                Log.e("ImageLoadTask", "Failed to load " + name + " image");
            }
        }

        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();

                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }
    }
}
