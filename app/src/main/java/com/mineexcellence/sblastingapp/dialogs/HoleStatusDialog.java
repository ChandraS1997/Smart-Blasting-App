package com.mineexcellence.sblastingapp.dialogs;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.databinding.DownloadListDialogBinding;
import com.mineexcellence.sblastingapp.databinding.HoleStatusListDialogBinding;
import com.mineexcellence.sblastingapp.ui.adapter.HoleStatusAdapter;

import java.util.ArrayList;
import java.util.List;

public class HoleStatusDialog extends BaseDialogFragment {

    public static final String TAG = "HoleStatusDialog";
    HoleStatusListDialogBinding binding;
    Dialog dialog;
    HoleStatusDialog _self;
    private HoleStatusListener mListener;

    public static HoleStatusDialog getInstance() {
        HoleStatusDialog frag = new HoleStatusDialog();
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.hole_status_list_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {

    }

    @Override
    public void setupClickListener() {

    }

    @Override
    public void loadData() {
        List<String> holeStatusList = new ArrayList<>();
        holeStatusList.add("Pending Hole");
        holeStatusList.add("Work in Progress");
        holeStatusList.add("Completed");
        holeStatusList.add("Deleted/ Blocked holes/ Do not blast");
        HoleStatusAdapter adapter = new HoleStatusAdapter(mContext, holeStatusList);
        binding.chargingList.setAdapter(adapter);

        binding.closeBtn.setOnClickListener(view -> dismiss());
        binding.saveProceedBtn.setOnClickListener(view -> {
            HoleStatusListener listener = getListener();
            if (listener != null)
                listener.holeStatusCallBack(adapter.getSelectedStatus());
            dismiss();
        });
    }

    public interface HoleStatusListener {
        void holeStatusCallBack(String status);
    }

    public void setupListener(HoleStatusDialog.HoleStatusListener listener) {
        mListener = listener;
    }

    private HoleStatusDialog.HoleStatusListener getListener() {
        HoleStatusDialog.HoleStatusListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof HoleStatusDialog.HoleStatusListener)
            listener = (HoleStatusDialog.HoleStatusListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof HoleStatusDialog.HoleStatusListener)
            listener = (HoleStatusDialog.HoleStatusListener) getActivity();

        return listener;
    }

}
