package com.smart_blasting_drilling.android.utils;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Camera;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.smart_blasting_drilling.android.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class BitmapUtils {
    private Camera mCamera;
    private static String imageFileName;
    private static String videoFileName;
    private static final String FILE_PROVIDER_AUTHORITY = "com.example.android.fileprovider";
    public static Bitmap resamplePic(Context context, String imagePath) {

        System.out.println("inside resample");
        // Get device screen size information
        DisplayMetrics metrics = new DisplayMetrics();
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        manager.getDefaultDisplay().getMetrics(metrics);

        int targetH = metrics.heightPixels;
        int targetW = metrics.widthPixels;

        // Get the dimensions of the original bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeFile(imagePath);
    }
    static File createTempImageFile(Context context) throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalCacheDir();

        return File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
    }
    static boolean deleteImageFile(Context context, String imagePath) {

        // Get the file
        File imageFile = new File(imagePath);

        // Delete the image
        boolean deleted = imageFile.delete();

        // If there is an error deleting the file, show a Toast
        if (!deleted) {
            String errorMessage = context.getString(R.string.error);

        }

        return deleted;
    }
    private static void galleryAddPic(Context context, String imagePath) {

        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        context.sendBroadcast(mediaScanIntent);

        //MediaPlayer mediaPlayer = MediaPlayer.create(context, Uri.parse(imagePath));
       // holder.duration.setText(getDuration(mediaPlayer.getDuration()));
    }

    public static String saveImage(Context context, Bitmap image,String  extension, Uri path) {
        String savedImagePath = null;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        if(extension.equals(".mp4")) {
            videoFileName = "VIDEO_" + timeStamp + ".mp4";
        }
        else {
          imageFileName = "JPEG_" + timeStamp + ".jpg";}
        File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/MyCamera");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {

            try {

                if(extension.equals(".mp4")) {
                    AssetFileDescriptor videoAsset = context.getContentResolver().openAssetFileDescriptor(path, "r");
                    FileInputStream in = videoAsset.createInputStream();

                    File newfile = new File(storageDir, videoFileName);
                    OutputStream out = new FileOutputStream(newfile);

                    // Copy the bits from instream to outstream
                    byte[] buf = new byte[1024];
                    int len;

                    while ((len = in.read(buf)) > 0) {
                        out.write(buf, 0, len);
                    }
                    in.close();
                    out.close();

                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    mediaScanIntent.setData(path);
                    context.sendBroadcast(mediaScanIntent);

                }else {
                    File imageFile = new File(storageDir, imageFileName);
                    savedImagePath = imageFile.getAbsolutePath();
                    OutputStream fOut = new FileOutputStream(imageFile);
                    image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                    fOut.close();
                    galleryAddPic(context, savedImagePath);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return savedImagePath;
    }



    private void saveVideoToInternalStorage (Context context,String filePath) {

        File newfile;

        try {

            File currentFile = new File(filePath);
            String fileName = currentFile.getName();

            ContextWrapper cw = new ContextWrapper(context);
            File directory = cw.getDir("videoDir", Context.MODE_PRIVATE);


            newfile = new File(directory, fileName);

            if(currentFile.exists()){

                InputStream in = new FileInputStream(currentFile);
                OutputStream out = new FileOutputStream(newfile);

                // Copy the bits from instream to outstream
                byte[] buf = new byte[1024];
                int len;

                while ((len = in.read(buf)) > 0) {
                    out.write(buf, 0, len);
                }

                in.close();
                out.close();

                Log.v("", "Video file saved successfully.");

            }else{
                Log.v("", "Video saving failed. Source file missing.");
            }



        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void loadVideoFromInternalStorage(String filePath){

        Uri uri = Uri.parse(Environment.getExternalStorageDirectory()+filePath);
        //myVideoView.setVideoURI(uri);

    }
    static void shareImage(Context context, String imagePath) {
        // Create the share intent and start the share activity
        File imageFile = new File(imagePath);
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("image/*");
        Uri photoURI = FileProvider.getUriForFile(context, FILE_PROVIDER_AUTHORITY, imageFile);
        shareIntent.putExtra(Intent.EXTRA_STREAM, photoURI);
        context.startActivity(shareIntent);
    }


}
