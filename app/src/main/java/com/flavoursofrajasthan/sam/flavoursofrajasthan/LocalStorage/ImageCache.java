package com.flavoursofrajasthan.sam.flavoursofrajasthan.LocalStorage;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.StreamCorruptedException;
import java.text.SimpleDateFormat;
import java.util.HashMap;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import static android.content.ContentValues.TAG;

public class ImageCache {
    private static ImageCache INSTANCE = null;
    private HashMap<String, String> cacheMap;
    private HashMap<String, Bitmap> bitmapMap;
    private static final String cacheDir = "/Android/data/com.flavoursofrajasthan.sam.flavoursofrajasthan/cache/";
    private static final String CACHE_FILENAME = ".cache";

    @SuppressWarnings("unchecked")
    private ImageCache() {

        cacheMap = new HashMap<String, String>();
        bitmapMap = new HashMap<String, Bitmap>();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),cacheDir);
        if(!fullCacheDir.exists()) {
            Log.i("CACHE", "Directory doesn't exist");
            cleanCacheStart();
            return;
        }
        try {
            ObjectInputStream is = new ObjectInputStream(new BufferedInputStream(new FileInputStream(new File(fullCacheDir.toString(), CACHE_FILENAME))));
            cacheMap = (HashMap<String,String>)is.readObject();
            is.close();
        } catch (StreamCorruptedException e) {
            Log.i("CACHE", "Corrupted stream");
            cleanCacheStart();
        } catch (FileNotFoundException e) {
            Log.i("CACHE", "File not found");
            cleanCacheStart();
        } catch (IOException e) {
            Log.i("CACHE", "Input/Output error");
            cleanCacheStart();
        } catch (ClassNotFoundException e) {
            Log.i("CACHE", "Class not found");
            cleanCacheStart();
        }
    }

    private void cleanCacheStart() {
        cacheMap = new HashMap<String, String>();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),cacheDir);
        fullCacheDir.mkdirs();
        File noMedia = new File(fullCacheDir.toString(), ".nomedia");
        try {
            noMedia.createNewFile();
            Log.i("CACHE", "Cache created");
        } catch (IOException e) {
            Log.i("CACHE", "Couldn't create .nomedia file");
            e.printStackTrace();
        }
    }

    private synchronized static void createInstance() {
        if(INSTANCE == null) {
            INSTANCE = new ImageCache();
        }
    }

    public static ImageCache getInstance() {
        if(INSTANCE == null) createInstance();
        return INSTANCE;
    }

    public void saveCacheFile(String cacheUri, Bitmap image) {
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),cacheDir);
        String fileLocalName = new SimpleDateFormat("ddMMyyhhmmssSSS").format(new java.util.Date())+".PNG";
        File fileUri = new File(fullCacheDir.toString(), fileLocalName);
        FileOutputStream outStream = null;

        try {
            outStream = new FileOutputStream(fileUri);
            image.compress(Bitmap.CompressFormat.PNG, 100, outStream);
            outStream.flush();
            outStream.close();
            cacheMap.put(cacheUri, fileLocalName);
            Log.i("CACHE", "Saved file "+cacheUri+" (which is now "+fileUri.toString()+") correctly");
            bitmapMap.put(cacheUri, image);
            ObjectOutputStream os = new ObjectOutputStream(new BufferedOutputStream(
                    new FileOutputStream(new File(fullCacheDir.toString(), CACHE_FILENAME))));
            os.writeObject(cacheMap);
            os.close();
        } catch (FileNotFoundException e) {
            Log.i("CACHE", "Error: File "+cacheUri+" was not found!");
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("CACHE", "Error: File could not be stuffed!");
            e.printStackTrace();
        }
    }

    public Bitmap getCacheFile(String cacheUri) {
        if(bitmapMap.containsKey(cacheUri)) return (Bitmap)bitmapMap.get(cacheUri);

        if(!cacheMap.containsKey(cacheUri)) return null;
        String fileLocalName = cacheMap.get(cacheUri).toString();
        File fullCacheDir = new File(Environment.getExternalStorageDirectory().toString(),cacheDir);
        File fileUri = new File(fullCacheDir.toString(), fileLocalName);
        if(!fileUri.exists()) return null;

        Log.i("CACHE", "File "+cacheUri+" has been found in the Cache");
        Bitmap bm = BitmapFactory.decodeFile(fileUri.toString());
        bitmapMap.put(cacheUri, bm);
        return bm;
    }

}