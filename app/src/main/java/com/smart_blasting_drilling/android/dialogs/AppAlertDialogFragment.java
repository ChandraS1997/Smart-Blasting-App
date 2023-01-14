package com.smart_blasting_drilling.android.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.CustomDialogBinding;

import org.apache.commons.lang3.StringUtils;


public class AppAlertDialogFragment extends BaseDialogFragment {
    public static final String TAG = "AppAlertDialogFragment";
    AppAlertDialogFragment _self;
    CustomDialogBinding binding;
    AppAlertDialogListener mListener;

    public static String ARG_POSITIVE_BTN = "ARG_POSITIVE_BTN";
    public static String ARG_NEGATIVE_BTN = "ARG_NEGATIVE_BTN";
    public static String ARG_TITLE = "ARG_TITLE";
    public static String ARG_MSG = "ARG_MSG";

    String title, msg, positiveBtn, negativeBtn;

    public AppAlertDialogFragment() {
        _self = this;
    }

    public static AppAlertDialogFragment getInstance(String title, String msg, String positiveBtn, String negativeBtn, AppAlertDialogListener listener) {
        AppAlertDialogFragment frag = new AppAlertDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_POSITIVE_BTN, positiveBtn);
        bundle.putString(ARG_NEGATIVE_BTN, negativeBtn);
        bundle.putString(ARG_TITLE, title);
        bundle.putString(ARG_MSG, msg);
        frag.setupListener(listener);
        frag.setArguments(bundle);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback( new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed()
            {


            }
        });
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            msg = getArguments().getString(ARG_MSG);
            positiveBtn = getArguments().getString(ARG_POSITIVE_BTN);
            negativeBtn = getArguments().getString(ARG_NEGATIVE_BTN);
        }

    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {
        if (!TextUtils.isEmpty(title)) {
            binding.heading.setText(StringUtils.capitalize(title));
        } else {
            binding.heading.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(msg)) {
            binding.message.setText(StringUtils.capitalize(msg));
        } else {
            binding.message.setVisibility(View.GONE);
        }
        if (!TextUtils.isEmpty(positiveBtn)) {
            binding.okBtn.setText(positiveBtn);
        } else {
            binding.okBtn.setVisibility(View.VISIBLE);
        }
        if (!TextUtils.isEmpty(negativeBtn)) {
            binding.cancelBtn.setText(negativeBtn);
        } else {
            binding.cancelBtn.setVisibility(View.GONE);
        }
    }

    @Override
    public void setupClickListener() {
        binding.okBtn.setOnClickListener(v -> {
            AppAlertDialogListener listener = getListener();
            if (listener != null) {
                listener.onOk(_self);
            } else {
                dismiss();
            }
        });
        binding.cancelBtn.setOnClickListener(v -> {
            AppAlertDialogListener listener = getListener();
            if (listener != null) {
                listener.onCancel(_self);
            } else {
                dismiss();
            }
        });
    }

    @Override
    public void loadData() {

    }

    public interface AppAlertDialogListener {
        void onOk(AppAlertDialogFragment dialogFragment);
      default  void onCancel(AppAlertDialogFragment dialogFragment){}
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState)
    {
        Dialog dialog = new Dialog(getActivity(),R.style.Theme_Dialog_Short);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        if (dialog.getWindow() != null)
        {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.custom_dialog, container, false);
        return binding.getRoot();
    }

    public void setupListener(AppAlertDialogListener listener) {
        mListener = listener;
    }

    private AppAlertDialogListener getListener()
    {
        AppAlertDialogListener listener = mListener;

        if (listener==null && getTargetFragment()!=null && getTargetFragment() instanceof AppAlertDialogListener)
            listener = (AppAlertDialogListener) getTargetFragment();

        if (listener==null && getActivity()!=null && getActivity() instanceof AppAlertDialogListener)
            listener = (AppAlertDialogListener) getActivity();

        return listener;
    }

}
