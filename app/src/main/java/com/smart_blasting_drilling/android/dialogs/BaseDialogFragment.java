package com.smart_blasting_drilling.android.dialogs;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.DialogFragment;
import com.smart_blasting_drilling.android.activity.BaseActivity;

public abstract class BaseDialogFragment extends DialogFragment {

    Context mContext;

    public BaseDialogFragment() {
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        initViews(view, savedInstanceState);
        setupClickListener();
        loadData();
    }

    public abstract void initViews(View view, Bundle savedInstanceState);
    public abstract void setupClickListener();
    public abstract void loadData();

    public String getStringRes(@StringRes int resId)
    {
        if(getContext()!=null)
            return getResources().getString(resId);
        return "";
    }

    public String getStringRes(@StringRes int resId, Object... formatArgs)
    {
        if(getContext()!=null)
            return getResources().getString(resId, formatArgs);
        return "";
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

    public void showAlertDialog(String title, String msg, String positiveBtn, String negativeBtn, AppAlertDialogFragment.AppAlertDialogListener listener) {
        if (getActivity() != null) {
            ((BaseActivity) getActivity()).showAlertDialog(title, msg, positiveBtn, negativeBtn, listener);
        }
    }


}

