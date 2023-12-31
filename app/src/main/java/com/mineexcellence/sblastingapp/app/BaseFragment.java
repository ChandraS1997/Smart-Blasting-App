package com.mineexcellence.sblastingapp.app;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mineexcellence.sblastingapp.room_database.AppDatabase;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.dialogs.AppAlertDialogFragment;
import com.mineexcellence.sblastingapp.dialogs.AppProgressBar;
import com.mineexcellence.sblastingapp.helper.PreferenceManger;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;


public class BaseFragment extends Fragment {

    public Context mContext;
    public PreferenceManger manger;

    public static final String SOMETHING_WENT_WRONG = "Something went wrong!";
    public static final String ERROR = "Error!";
    public static final String API_RESPONSE = "api response";
    public static final String APILOADINGTEXT = "Please wait...";
    public static final String NODATAFOUND = "Nothing to show here yet!";
    public static final String SESSION_EXPIRED_TEXT = "Session expired,Please Login Again.";

    public AppDatabase appDatabase;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
        manger = ((BaseActivity) context).manger;
        appDatabase = ((BaseActivity) context).appDatabase;
    }

    public void showToast(String msg) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showToast(msg);
        }
    }

    public void showLog(String tag, String msg) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showLog(tag, msg);
        }
    }

    public void showDebug(String tag, String msg) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showDebug(tag, msg);
        }
    }

    public void showSnackBar(View v, String msg) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showSnackBar(v, msg);
        }
    }

    public void showLoader() {
        AppProgressBar.showLoaderDialog(mContext);
    }

    public void hideLoader() {
        AppProgressBar.hideLoaderDialog();
    }

    public File createImageFile() throws IOException {
        String imageFileName = "Wayrem_Image-" + System.currentTimeMillis() + "_";
        File storageDir = mContext.getFilesDir();
        return File.createTempFile(imageFileName, ".png", storageDir);
    }


    public boolean haveWritePermission(Context context) {
        int result = context.checkCallingOrSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }


    public int dp2px(@NotNull Resources resource, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resource.getDisplayMetrics());
    }

   /* public static void downloadFile(String url, Context context) {
        File filePath = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/" + url.substring(url.lastIndexOf('/'))+ ".pdf");
        if (filePath.exists()) {
            Toast.makeText(context, "Invoice Already Downloaded!", Toast.LENGTH_SHORT).show();
         //   ((BaseActivity) context).showMediaFile(context, String.valueOf(filePath), 1);
        } else {
            String fileName = url.substring(url.lastIndexOf('/'))+".pdf";
            Uri uri = Uri.parse(url);
            RadioDownloadManager downloadHelper = new RadioDownloadManager(context);
            downloadHelper.downloadData(uri, fileName, filePath);
            Toast.makeText(context, "Invoice Downloaded!", Toast.LENGTH_SHORT).show();
        }
      //  ((BaseActivity) context).showMediaFile(context, String.valueOf(filePath), 1);
    }*/

    public int productAvailabilityCheck(int qty, int threshold) {
        if (qty - threshold == 0) {
            return qty;
        } else if (qty > threshold) {
            return qty;
        } else if (qty <= threshold) {
            return qty;
        }
        return qty;
    }

    public String productAvailabilityCheckLB(int qty, int threshold) {
        if (qty - threshold == 0) {
            //    return "Only " + qty + " left in stock";
            return "Out of Stock";
        } else if (qty <= 0 ) {
            return "Out of Stock";
        } else if (qty > threshold) {
            return "";
        } else if (qty <= threshold) {
            return "Only " + qty + " left in stock";
        }
        return "";
    }

    public void showAlertDialog(String title, String msg, String positiveBtn, String negativeBtn, AppAlertDialogFragment.AppAlertDialogListener listener) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showAlertDialog(title, msg, positiveBtn, negativeBtn, listener);
        }
    }

    public boolean isCurrentLangArabic() {
        /*if (getActivity() != null)
            return ((BaseActivity) mContext).isCurrentLangArabic();
        else*/ return false;
    }

    public RequestBody toRequestBody(String value) {
        return !TextUtils.isEmpty(value) ? RequestBody.create(MediaType.parse("text/plain"), value) : null;
    }
}
