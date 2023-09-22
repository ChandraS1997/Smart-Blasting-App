package com.mineexcellence.sblastingapp.helper;

import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseLoginData;
import com.mineexcellence.sblastingapp.ui.models.TableEditModel;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class PreferenceManger {
    public static final String PREF_KEY = "waiter_preference";
    public static final String AUTH_TOKEN = "auth_token";
    public static final String USER_DETAILS = "user_details";
    public final String CHECK_USER_IS_LOGGED_IN = "user_logged_in";
    private final SharedPreferences mSharedPreferences;
    private static final String TABLE_FIELD = "table_field";
    private static final String TABLE_FIELD_3D = "table_field_3d";
    private static final String TABLE_PRE_SPLIT_FIELD_3D = "table_pre_split_field_3d";
    private static final String TABLE_PILOT_FIELD_3D = "table_pilot_field_3d";

    public PreferenceManger(SharedPreferences mSharedPreferences) {
        this.mSharedPreferences = mSharedPreferences;
    }

    private SharedPreferences.Editor getEditor() {
        return mSharedPreferences.edit();
    }

    public void remove(String key) {
        SharedPreferences.Editor editor = getEditor();
        editor.remove(key);
        editor.apply();
    }

    public void putString(String key, String value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putString(key, value);
        editor.commit();
    }


    public void putInt(String key, int value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putInt(key, value);
        editor.apply();
    }

    public void putBoolean(String key, boolean value) {
        SharedPreferences.Editor editor = getEditor();
        editor.putBoolean(key, value);
        editor.apply();
    }

    public String getStringValue(String key) {
        return mSharedPreferences.getString(key, "");
    }

    public String getStringValue(String key, String def) {
        return mSharedPreferences.getString(key, def);
    }

    public boolean getBooleanValue(String key) {
        return mSharedPreferences.getBoolean(key, false);
    }

    public boolean getBooleanValue(String key, boolean defaultValue) {
        return mSharedPreferences.getBoolean(key, defaultValue);
    }

    public int getIntegerValue(String key) {
        return mSharedPreferences.getInt(key, 0);
    }

    public String getAuthToken() {
        return getStringValue(AUTH_TOKEN);
    }

    public void verifyUserSession() {
        putBoolean(CHECK_USER_IS_LOGGED_IN, true);
    }

    public void putUserDetails(ResponseLoginData userDetails) {
        Gson gson = new Gson();
        String json = gson.toJson(userDetails);
        putString(USER_DETAILS, json);
    }

    public ResponseLoginData getUserDetails() {
        Gson gson = new Gson();
        String json = getStringValue(USER_DETAILS);
        return gson.fromJson(json, ResponseLoginData.class);
    }

    public void logoutUser() {
        SharedPreferences.Editor editor = getEditor();
        editor.clear();
        editor.commit();
    }

    public void saveArrayList(ArrayList<String> list, String key) {
        Gson gson = new Gson();
        String json = gson.toJson(list);
        putString(key, json);

    }

    public void putAuthToken(String token) {
        putString(AUTH_TOKEN, token);
    }

    public void setTableField(List<TableEditModel> tableField) {
        Gson gson = new Gson();
        String json = gson.toJson(tableField);
        putString(TABLE_FIELD, json);
    }

    public List<TableEditModel> getTableField() {
        Gson gson = new Gson();
        String json = getStringValue(TABLE_FIELD);
        Type typeToken = new TypeToken<List<TableEditModel>>(){}.getType();
        return new Gson().fromJson(json, typeToken);
    }


    public void set3dTableField(List<TableEditModel> tableField) {
        Gson gson = new Gson();
        String json = gson.toJson(tableField);
        putString(TABLE_FIELD_3D, json);
    }

    public List<TableEditModel> get3dTableField() {
        Gson gson = new Gson();
        String json = getStringValue(TABLE_FIELD_3D);
        Type typeToken = new TypeToken<List<TableEditModel>>(){}.getType();
        return new Gson().fromJson(json, typeToken);
    }

    public void set3dPreSplitTableField(List<TableEditModel> tableField) {
        Gson gson = new Gson();
        String json = gson.toJson(tableField);
        putString(TABLE_PRE_SPLIT_FIELD_3D, json);
    }

    public List<TableEditModel> get3dPreSplitTableField() {
        Gson gson = new Gson();
        String json = getStringValue(TABLE_PRE_SPLIT_FIELD_3D);
        Type typeToken = new TypeToken<List<TableEditModel>>(){}.getType();
        return new Gson().fromJson(json, typeToken);
    }


    public void set3dPilotTableField(List<TableEditModel> tableField) {
        Gson gson = new Gson();
        String json = gson.toJson(tableField);
        putString(TABLE_PILOT_FIELD_3D, json);
    }

    public List<TableEditModel> get3dPilotTableField() {
        Gson gson = new Gson();
        String json = getStringValue(TABLE_PILOT_FIELD_3D);
        Type typeToken = new TypeToken<List<TableEditModel>>(){}.getType();
        return new Gson().fromJson(json, typeToken);
    }
}
