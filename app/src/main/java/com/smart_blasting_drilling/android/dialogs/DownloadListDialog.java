package com.smart_blasting_drilling.android.dialogs;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.DatePicker;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.smart_blasting_drilling.android.adapter.ProjectDialogListAdapter;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.DownloadListDialogBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DownloadListDialog extends BaseDialogFragment {
    public static final String TAG = "DownlodeListDialog";
    DownloadLIstDialogListener mListener;
    DownloadListDialogBinding binding;
    Dialog dialog;
    DownloadListDialog _self;
    ProjectDialogListAdapter projectDialogListAdapter;
    List<String> projectList = new ArrayList<>();

    public static DownloadListDialog getInstance(DownloadListDialog.DownloadLIstDialogListener listener) {
        DownloadListDialog frag = new DownloadListDialog();
        frag.setupListener(listener);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.download_list_dialog, container, false);
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
        projectDialogListAdapter = new ProjectDialogListAdapter(mContext, projectList);
        binding.projectListRv.setAdapter(projectDialogListAdapter);

        binding.showStartDateTxt.setOnClickListener(view -> {
            new DatePickerDialog(mContext, setDataWithDatePicker(binding.showStartDateTxt), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        binding.showEndDateTxt.setOnClickListener(view -> new DatePickerDialog(mContext, setDataWithDatePicker(binding.showEndDateTxt), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show());

        binding.goBtn.setOnClickListener(view -> {

        });
    }

    public DownloadListDialog() {
        _self = this;
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

    Calendar calendar= Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener setDataWithDatePicker(TextView textView) {
        return (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            updateLabel(textView);
        };
    }

    private void updateLabel(TextView textView){
        String myFormat="yyyy/MM/dd";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
        textView.setText(dateFormat.format(calendar.getTime()));
    }

    public void setupListener(DownloadLIstDialogListener listener) {
        mListener = listener;
    }

    private DownloadLIstDialogListener getListener() {
        DownloadLIstDialogListener listener = mListener;

        if (listener == null && getTargetFragment() != null && getTargetFragment() instanceof DownloadLIstDialogListener)
            listener = (DownloadLIstDialogListener) getTargetFragment();

        if (listener == null && getActivity() != null && getActivity() instanceof DownloadLIstDialogListener)
            listener = (DownloadLIstDialogListener) getActivity();

        return listener;
    }

    public interface DownloadLIstDialogListener {
        void onOk(DownloadListDialog dialogFragment);

        default void onCancel(DownloadListDialog dialogFragment) {
        }
    }

}
