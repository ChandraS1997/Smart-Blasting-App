package com.smart_blasting_drilling.android.app;

import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.text.TextUtils;
import android.util.Log;
import androidx.multidex.MultiDexApplication;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.activity.BaseActivity;
import com.smart_blasting_drilling.android.helper.CheckInternetConnection;
import com.smart_blasting_drilling.android.helper.ConnectivityReceiver;
import com.smart_blasting_drilling.android.helper.InAppInstallUtil;
import com.smart_blasting_drilling.android.helper.PreferenceManger;

import org.apache.commons.lang3.StringUtils;
import java.util.HashMap;
import java.util.Objects;
import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class BaseApplication extends MultiDexApplication {

    public static final String HYPER_VISA = "hyper_VISA";
    public static final String HYPER_MADA = "hyper_MADA";
    public static final String HYPER_MASTER = "hyper_MASTER";
    private static final String TAG = "WayremApplication";
    private static BaseApplication instance;
    private static final String VISA = "8ac7a4c9767473e401767eff42341943";
    private static final String MADA = "8ac7a4c9767473e401767effaf171947";
    private static PreferenceManger preferenceManger;
    private static InAppInstallUtil inAppInstallUtil;
    private ConnectivityReceiver receiver;

    public static BaseApplication getInstance() {
        return instance;
    }

    public static PreferenceManger getPreferenceManger() {
        if (preferenceManger == null && getInstance() != null) {
            preferenceManger = new PreferenceManger(getInstance().getSharedPreferences(PreferenceManger.PREF_KEY, Context.MODE_PRIVATE));
        }
        return preferenceManger;
    }

//    public static InAppInstallUtil getInAppInstallUtil() {
//        if (inAppInstallUtil == null && getInstance() != null) {
//            inAppInstallUtil = new PreferenceManger(getInstance().getSharedPreferences(PreferenceManger.PREF_KEY, Context.MODE_PRIVATE));
//        }
//        return preferenceManger;
//    }

    /*@Override
    public void onLowMemory() {
        super.onLowMemory();
        Glide.get(this).clearMemory();
    }*/

    public static HashMap<String, Object> getDefaultHeaders() {
        HashMap<String, Object> hashMap = new HashMap<>();
        if (preferenceManger != null && !TextUtils.isEmpty(preferenceManger.getAuthToken())) {
            hashMap.put("Authorization", StringUtils.capitalize(preferenceManger.getAuthToken()));
        }
        hashMap.put("Accept", "application/json");
        return hashMap;
    }

    public static String getEntID(int id) {
        if (id == 1) {
            return VISA;
        } else if (id == 2) {
            return VISA;
        } else if (id == 3) {
            return MADA;
        }
        return "WRONG KEY";
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        registerConnectivityReceiver();
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

    }

    private void registerConnectivityReceiver() {
        try {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            registerReceiver(getReceiverObserver(), filter);
        } catch (Exception e) {
            Log.e(TAG, Objects.requireNonNull(e.getMessage()));
        }
    }

    private ConnectivityReceiver getReceiverObserver() {
        if (receiver == null)
            receiver = new ConnectivityReceiver();
        return receiver;
    }

    public boolean isInternetConnected(Context context) {
        if (new CheckInternetConnection(this).isConnected())
            return true;
        else {
            ((BaseActivity) context).noInternetDialog();
        }
        return false;
    }

    public boolean isInternet(Context context) {
        return new CheckInternetConnection(this).isConnected();
    }
}
