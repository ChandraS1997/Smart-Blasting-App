package com.smart_blasting_drilling.android.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.AsyncTask;

import com.smart_blasting_drilling.android.interfaces.OnImageCompressedListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;


public class ImageCompressAsync extends AsyncTask<File, Void, Bitmap> {
    private static final String TAG = "ImageCompressAsync";

    private Context mContext;
    private OnImageCompressedListener onImageCompressedListener;

    private int imageSize;
    private String type;

    public ImageCompressAsync(Context mContext, String type, int imageSize, OnImageCompressedListener onImageCompressedListener) {
        this.mContext = mContext;
        this.imageSize = imageSize;
        this.type = type;
        this.onImageCompressedListener = onImageCompressedListener;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //TGHCProgressBar.showLoaderDialog(mContext);
    }

    @Override
    protected Bitmap doInBackground(File... files) {
        File file = files[0];

        try {
            Bitmap bitmapImage = loadImageBitmap(file.getAbsolutePath());

            int nh = (int) (bitmapImage.getHeight() * (((float) this.imageSize) / bitmapImage.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmapImage, this.imageSize, nh, true);

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            scaled.compress(Bitmap.CompressFormat.PNG, 50, stream);
            byte[] byteArray = stream.toByteArray();

            Logger.DebugLog(TAG, "IMAGE_SIZE : " + byteArray.length + " from " + bitmapImage.getByteCount());

           // RequestBody requestFile = RequestBody.create(MediaType.parse(this.type), byteArray);

            return bitmapImage;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(Bitmap part) {
        super.onPostExecute(part);
       // TGHCProgressBar.hideLoaderDialog();

        if (this.onImageCompressedListener != null) {
            this.onImageCompressedListener.OnImageCompressed(part);
        }
    }

    public Bitmap loadImageBitmap(String picturePath) {
        Bitmap loadedBitmap = BitmapFactory.decodeFile(picturePath);

        int nh = (int) (loadedBitmap.getHeight() * (((float) this.imageSize) / loadedBitmap.getWidth()));
        Bitmap scaled = Bitmap.createScaledBitmap(loadedBitmap, this.imageSize, nh, true);

        ExifInterface exif = null;
        try {
            File pictureFile = new File(picturePath);
            exif = new ExifInterface(pictureFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        int orientation = ExifInterface.ORIENTATION_NORMAL;

        if (exif != null)
            orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                scaled = rotateBitmap(scaled, 90);
                break;
            case ExifInterface.ORIENTATION_ROTATE_180:
                scaled = rotateBitmap(scaled, 180);
                break;

            case ExifInterface.ORIENTATION_ROTATE_270:
                scaled = rotateBitmap(scaled, 270);
                break;
        }

        return scaled;
    }

    public static Bitmap rotateBitmap(Bitmap bitmap, int degrees) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

}
