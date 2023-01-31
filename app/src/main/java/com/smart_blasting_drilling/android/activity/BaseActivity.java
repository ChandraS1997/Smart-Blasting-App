package com.smart_blasting_drilling.android.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.snackbar.Snackbar;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.NoInternetBinding;
import com.smart_blasting_drilling.android.dialogs.AppAlertDialogFragment;
import com.smart_blasting_drilling.android.dialogs.AppProgressBar;
import com.smart_blasting_drilling.android.helper.ConnectivityReceiver;
import com.smart_blasting_drilling.android.helper.PreferenceManger;


import okhttp3.MediaType;
import okhttp3.RequestBody;

public class BaseActivity extends AppCompatActivity {
    Toast toast;
    public Dialog noInternetdialog;

    ConnectivityReceiver receiver;

    public PreferenceManger manger;
    public static final String SOMETHING_WENT_WRONG = "Something went wrong!";
    public static final String ERROR = "Error!";
    public static final String API_RESPONSE = "api response";
    public static final String APILOADINGTEXT = "Please wait...";
    public static final String NODATAFOUND = "Nothing to show here yet!";
    public static final String SESSION_EXPIRED_TEXT = "Session expired,Please Login Again.";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        noInternetdialog = new Dialog(this);

        receiver = ConnectivityReceiver.getInstance();
        manger = BaseApplication.getPreferenceManger();

    }
    public void showToast(String msg) {
        try {
            toast.getView().isShown();
            toast.setText(msg);
        } catch (Exception e) {
            toast = Toast.makeText(this, msg, Toast.LENGTH_SHORT);
        }
        toast.show();
    }


    public void showLog(String tag, String msg) {
        Log.e(tag, msg);
    }

    public void showDebug(String tag, String msg) {
        Log.d(tag, msg);
    }

    public void showSnackBar(View v, String msg) {
        Snackbar.make(v, msg, Snackbar.LENGTH_SHORT).show();
    }

    public void showActionSnackBar(Context context, View view, String msg, String action, View.OnClickListener clickListener) {
        Snackbar snackbar =
                Snackbar.make(
                        view,
                        msg,
                        Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(action, clickListener);
        snackbar.setActionTextColor(
                context.getResources().getColor(R.color.F1E60E));
        snackbar.show();
    }




    public void hideLoader() {
        AppProgressBar.hideLoaderDialog();
    }

    public void showLoader() {
        AppProgressBar.showLoaderDialog(this);
    }

    public void showAlertDialog(String title, String msg, String positiveBtn, String negativeBtn) {
        showAlertDialog(title, msg, positiveBtn, negativeBtn, null);
    }

    public void showAlertDialog(String title, String msg, String positiveBtn, String negativeBtn, AppAlertDialogFragment.AppAlertDialogListener listener) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        AppAlertDialogFragment infoDialogFragment = AppAlertDialogFragment.getInstance(title, msg, positiveBtn, negativeBtn, listener);
        infoDialogFragment.setupListener(listener);
        ft.add(infoDialogFragment, AppAlertDialogFragment.TAG);
        ft.commitAllowingStateLoss();
    }

    public void logoutFromApp() {
        logOutNotification();
    }

/*    public void showMediaFile(Context context, String url, int type) {
        Intent intent = new Intent(context, MediaPreview.class);
        intent.putExtra("fileUrl", url);
        intent.putExtra("fileType", type);
        startActivity(intent);
    }*/





    public void hideKeyboard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void logOutNotification() {
        showLoader();
       /* MainService.userLogoutApiCaller(this, Constants.getUserAuthToken()).observe((LifecycleOwner) this, response -> {
            if (response == null) {
                showToast(getResources().getString(R.string.something_wrong));
            } else {
                showToast(TextUtil.getString(response.getMessage()));
                BaseApplication.getPreferenceManger().logoutUser();
                AppDelegate.setInstance(new AppDelegate());
                startActivity(new Intent(this, HomeActivity.class));
                finishAffinity();
            }
            hideLoader();
        });*/
    }

    public void noInternetDialog() {
        if(!noInternetdialog.isShowing())
        {
            NoInternetBinding sDialog = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.no_internet, null, false);
            noInternetdialog = new Dialog(this);
            noInternetdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            noInternetdialog.setContentView(sDialog.getRoot());
            noInternetdialog.setCancelable(false);
            noInternetdialog.setCanceledOnTouchOutside(false);
            noInternetdialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            noInternetdialog.getWindow().setGravity(Gravity.CENTER);
            noInternetdialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            sDialog.retry.setOnClickListener(v ->
            {
                if (BaseApplication.getInstance().isInternet(this)) {
                    noInternetdialog.dismiss();
                    hideLoader();
                } else {
                    showToast("Please enable Data or Wifi connection");
                }
            });
            noInternetdialog.show();
        }
    }



    public boolean isCurrentLangArabic() {
//        return manger.getStringValue(PreferenceManger.LANGUAGE_TYPE, Constants.LANGUAGE_TYPE.ENGLISH.toString()).equalsIgnoreCase(Constants.LANGUAGE_TYPE.ARABIC.toString());
        return true;
    }

    public RequestBody toRequestBody(String value) {
        return !TextUtils.isEmpty(value) ? RequestBody.create(MediaType.parse("text/plain"), value) : null;
    }



}
