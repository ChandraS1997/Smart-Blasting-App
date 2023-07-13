package com.mineexcellence.sblastingapp.helper;

import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.util.Log;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.mineexcellence.sblastingapp.R;

public class InAppInstallUtil {

    AppUpdateManager appUpdateManager;
    InstallStateUpdatedListener installStateUpdatedListener;

    public InAppInstallUtil _self;
    Context mContext;
    int UPDATE_REQUEST_CODE = 13;
    View mView;

    // Attr
    @UpdateAvailability int updateAvailability;
    @AppUpdateType int appUpdateType;

    public InAppInstallUtil(Context mContext) {
        this.mContext = mContext;
        _self = this;
    }

    public static InAppInstallUtil getInstance(Context context, View view) {
//        new InAppInstallUtil(context).mView = view;
        return new InAppInstallUtil(context);
    }

    public void appUpdateManager() {
        installStateUpdatedListener = installState -> {
            if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                popupSnackBarForCompleteUpdate(mView);
            } else
                Log.e("UPDATE", "Not downloaded yet");
        };

        inAppUpdate();
    }

    public void init() {
        appUpdateManager = AppUpdateManagerFactory.create(mContext);

        appUpdateManager();
    }

    public InAppInstallUtil setUpdateAvailability(@UpdateAvailability int updateAvailability) {
        this.updateAvailability = updateAvailability;
        return this;
    }

    public InAppInstallUtil setAppUpdateType(@AppUpdateType int appUpdateType) {
        this.appUpdateType = appUpdateType;
        return this;
    }

    private void inAppUpdate() {

        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {

            Log.e("AVAILABLE_VERSION_CODE", appUpdateInfo.availableVersionCode() + "");

            if (appUpdateInfo.updateAvailability() == updateAvailability
                    && appUpdateInfo.isUpdateTypeAllowed(appUpdateType)) {
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo,
                            appUpdateType,
                            (Activity) mContext,
                            UPDATE_REQUEST_CODE);

                } catch (IntentSender.SendIntentException ignored) {

                }
            }
        });

        appUpdateManager.registerListener(installStateUpdatedListener);
    }

    public void removeListener() {
        appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    private void popupSnackBarForCompleteUpdate(View view) {
        Snackbar snackbar =
                Snackbar.make(
                        view.findViewById(android.R.id.content),
                        "Update almost finished!",
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(mContext.getString(R.string.install), v ->
                appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(mContext.getResources().getColor(R.color._f1e60e));
        snackbar.show();
    }

}
