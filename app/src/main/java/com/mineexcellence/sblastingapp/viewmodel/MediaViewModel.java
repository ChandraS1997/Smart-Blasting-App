package com.mineexcellence.sblastingapp.viewmodel;

import android.net.Uri;

public class MediaViewModel extends BaseViewModel {

    public Uri fileUri;
    public String filePath;

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public void onDestroy() {

    }
}