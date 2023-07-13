package com.mineexcellence.sblastingapp.utils;

import static android.content.Context.DOWNLOAD_SERVICE;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.mineexcellence.sblastingapp.R;

import java.io.File;

public class DonwloadFileManagerUtils {

    DownloadManager downloadManager;
    Context context;

    public DonwloadFileManagerUtils(Context context){
        this.context = context;
    }

    public long downloadData(Uri uri, String fileName, File file) {

        long downloadReference;

        downloadManager = (DownloadManager) context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Downloading " + fileName);
        request.setDescription(fileName);
        // request.setMimeType(mimeType);
        // request.setVisibleInDownloadsUi(true);
        // request.allowScanningByMediaScanner();

        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,context.getString(R.string.app_name)+File.separator+fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);
        downloadReference = downloadManager.enqueue(request);


        return downloadReference;
    }

    public long downloadDataWithoutSave(Uri uri, String fileName, File file) {

        long downloadReference;

        downloadManager = (DownloadManager)context.getSystemService(DOWNLOAD_SERVICE);
        DownloadManager.Request request = new DownloadManager.Request(uri);
        request.setTitle("Downloading " + fileName);
        request.setDescription(fileName);
        // request.setMimeType(mimeType);
        // request.setVisibleInDownloadsUi(true);
        // request.allowScanningByMediaScanner();

        String name = fileName.replace("_dummy.mp3","");
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,"Rawa/Temp/Dummy_"+name+File.separator+fileName);
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI
                | DownloadManager.Request.NETWORK_MOBILE);
        downloadReference = downloadManager.enqueue(request);


        return downloadReference;
    }


}