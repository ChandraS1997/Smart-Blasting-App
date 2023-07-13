package com.mineexcellence.sblastingapp.utils;

import android.content.Context;
import android.os.Environment;

import com.mineexcellence.sblastingapp.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

public class FileStoreUtils {

    Context context;

    public FileStoreUtils(Context context) {
        this.context = context;
    }

    public void addFileIntoDirectory(String filename) {
        try {
            File appSpecificExternalDir = new File(context.getExternalFilesDir(context.getString(R.string.app_name)), filename);
            FileInputStream fis = new FileInputStream(filename);
            InputStreamReader inputStreamReader =
                    new InputStreamReader(fis, StandardCharsets.UTF_8);
            StringBuilder stringBuilder = new StringBuilder();
            try (BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String line = reader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append('\n');
                    line = reader.readLine();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                String contents = stringBuilder.toString();
                String[] docSplit = filename.split("/");
                String[] fileNameArr = docSplit[docSplit.length - 1].split("\\.");
                writeFileOnInternalStorage(String.format("%s.%s", fileNameArr[0], fileNameArr[1]), contents);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void writeFileOnInternalStorage(String sFileName, String sBody){
        File dir = new File(context.getFilesDir(), context.getString(R.string.app_name));
        if(!dir.exists()){
            dir.mkdir();
        }

        try {
            File gpxfile = new File(dir, sFileName);
            FileWriter writer = new FileWriter(gpxfile);
            writer.append(sBody);
            writer.flush();
            writer.close();
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    // Checks if a volume containing external storage is available to at least read.
    public boolean isExternalStorageReadable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ||
                Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED_READ_ONLY);
    }

}
