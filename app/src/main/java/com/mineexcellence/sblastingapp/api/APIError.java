package com.mineexcellence.sblastingapp.api;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class APIError implements Serializable{

    @SerializedName("errorMessage")
    @Expose
    private String errorMessage;

    @SerializedName("Message")
    @Expose
    private String message;

    @SerializedName("errorCode")
    @Expose
    private int errorCode;

    @SerializedName("statusCode")
    @Expose
    private int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }
}
