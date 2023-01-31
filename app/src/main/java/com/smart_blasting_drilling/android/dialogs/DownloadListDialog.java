package com.smart_blasting_drilling.android.dialogs;

import static com.smart_blasting_drilling.android.app.BaseFragment.ERROR;
import static com.smart_blasting_drilling.android.app.BaseFragment.NODATAFOUND;
import static com.smart_blasting_drilling.android.app.BaseFragment.SOMETHING_WENT_WRONG;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.activity.AuthActivity;
import com.smart_blasting_drilling.android.activity.BaseActivity;
import com.smart_blasting_drilling.android.activity.HomeActivity;
import com.smart_blasting_drilling.android.adapter.ProjectDialogListAdapter;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseLoginData;
import com.smart_blasting_drilling.android.databinding.DownloadListDialogBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.utils.DateUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DownloadListDialog extends BaseDialogFragment {
    public static final String TAG = "DownloadListDialog";
    DownloadLIstDialogListener mListener;
    DownloadListDialogBinding binding;
    Dialog dialog;
    DownloadListDialog _self;
    ProjectDialogListAdapter projectDialogListAdapter;
    List<ResponseBladesRetrieveData> projectList = new ArrayList<>();
    String startDate = "", endDate = "";

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

        endDate = DateUtils.getDate(System.currentTimeMillis(), "yyyy-MM-dd");
        startDate = DateUtils.getLastMonthDateFromCurDate("yyyy-MM-dd");
        binding.showEndDateTxt.setText(DateUtils.getDate(System.currentTimeMillis(), "yyyy/MM/dd"));
        binding.showStartDateTxt.setText(DateUtils.getLastMonthDateFromCurDate("yyyy/MM/dd"));

        binding.showStartDateTxt.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            new DatePickerDialog(mContext, setDataWithDatePicker(binding.showStartDateTxt, true), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
        binding.showEndDateTxt.setOnClickListener(view -> {
            calendar = Calendar.getInstance();
            new DatePickerDialog(mContext, setDataWithDatePicker(binding.showEndDateTxt, false), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        binding.goBtn.setOnClickListener(view -> {
            retrieveByDateApiCaller(startDate, endDate);
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

    Calendar calendar= Calendar.getInstance();

    private DatePickerDialog.OnDateSetListener setDataWithDatePicker(TextView textView, boolean isStartDate) {
        return (view, year, month, day) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH,month);
            calendar.set(Calendar.DAY_OF_MONTH,day);
            if (isStartDate) {
                startDate = String.format("%s-%s-%s", year, month, year);
            } else {
                endDate = String.format("%s-%s-%s", year, month, year);
            }
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
        void onOk(List<ResponseBladesRetrieveData> bladesRetrieveDataList, DownloadListDialog dialogFragment);

        default void onCancel(DownloadListDialog dialogFragment) {
        }
    }

    private void retrieveByDateApiCaller(String startDate, String endDate) {
        showLoader();
        MainService.retrieveByDateApiCaller(mContext, startDate, endDate).observe((LifecycleOwner) mContext, responseLogin -> {
            if (responseLogin == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (!(responseLogin.isJsonNull())) {
                    try {
                        JsonObject jsonObject = responseLogin.getAsJsonObject();
                        if (jsonObject != null) {
                            try {
                                String data = jsonObject.get("RetrieveByDateResult").getAsString();
                                Type itemList = new TypeToken<List<ResponseBladesRetrieveData>>(){}.getType();
                                List<ResponseBladesRetrieveData> bladesRetrieveDataList = new Gson().fromJson(data, itemList);
                                projectList.clear();
                                if (!Constants.isListEmpty(bladesRetrieveDataList)) {
                                    projectList.addAll(bladesRetrieveDataList);
                                    projectDialogListAdapter.notifyDataSetChanged();
                                }
                            } catch (Exception e) {
                                Log.e(NODATAFOUND, e.getMessage());
                            }
                        } else {
                            ((BaseActivity) requireActivity()).showAlertDialog(ERROR, SOMETHING_WENT_WRONG, "OK", "Cancel");
                        }
                    } catch (Exception e) {
                        Log.e(NODATAFOUND, e.getMessage());
                    }
                    hideLoader();
                }
            }
            hideLoader();
        });
    }

}
