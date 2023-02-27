package com.smart_blasting_drilling.android.dialogs;

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

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.DialogSyncProjectOptionViewBinding;

public class SyncProjectOptionDialog extends BaseDialogFragment {

    DialogSyncProjectOptionViewBinding binding;
    public static final String TAG = "SyncProjectOptionDialog";
    Dialog dialog;
    SyncProjectOptionDialog _self;
    private SyncProjectListener mListener;

    public SyncProjectOptionDialog() {
        _self = this;
    }

    public static SyncProjectOptionDialog getInstance(SyncProjectListener listener) {
        SyncProjectOptionDialog frag = new SyncProjectOptionDialog();
        frag.setUpListener(listener);
        return frag;
    }

    public void setUpListener(SyncProjectListener mListener) {
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        return dialog;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.dialog_sync_project_option_view, container, false);
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

        binding.syncProjectWithDrims.setOnClickListener(view -> {
            SyncProjectListener listener = getListener();
            if (listener != null) {
                listener.syncWithDrims();
            }
            dismiss();
        });

        binding.syncProjectWithBims.setOnClickListener(view -> {
            SyncProjectListener listener = getListener();
            if (listener != null) {
                listener.syncWithBims();
            }
            dismiss();
        });

        binding.syncProjectWithBlades.setOnClickListener(view -> {
            SyncProjectListener listener = getListener();
            if (listener != null) {
                listener.syncWithBlades();
            }
            dismiss();
        });

    }

    private SyncProjectListener getListener() {
        SyncProjectListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof SyncProjectListener)
            listener = (SyncProjectListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof SyncProjectListener)
            listener = (SyncProjectListener) getActivity();

        return listener;
    }

    public interface SyncProjectListener {
        void syncWithDrims();
        void syncWithBims();
        void syncWithBlades();
    }

}
