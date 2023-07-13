package com.mineexcellence.sblastingapp.ui.models;

public class MediaDataModel {

    String fileName, filePath, extension, type;
    boolean isSelection, isSynced;

    public MediaDataModel() {
    }

    public MediaDataModel(String fileName, String filePath, String extension, String type, boolean isSelection) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.extension = extension;
        this.type = type;
        this.isSelection = isSelection;
    }

    public MediaDataModel(String fileName, String filePath, String extension, String type, boolean isSelection, boolean isSynced) {
        this.fileName = fileName;
        this.filePath = filePath;
        this.extension = extension;
        this.type = type;
        this.isSelection = isSelection;
        this.isSynced = isSynced;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isSelection() {
        return isSelection;
    }

    public void setSelection(boolean selection) {
        isSelection = selection;
    }

    public boolean isSynced() {
        return isSynced;
    }

    public void setSynced(boolean synced) {
        isSynced = synced;
    }
}
