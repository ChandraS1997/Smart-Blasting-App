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
import com.smart_blasting_drilling.android.databinding.ProjectInfoDialogBinding;

public class ProjectInfoDialog extends BaseDialogFragment
{
    public static final String TAG = "ProjectInfoDialog";
    ProjectInfoDialogListener mListener;
    ProjectInfoDialogBinding binding;
    Dialog dialog;
    ProjectInfoDialog _self;
    public static ProjectInfoDialog getInstance(ProjectInfoDialog.ProjectInfoDialogListener listener)
    {
        ProjectInfoDialog frag = new ProjectInfoDialog();
        frag.setupListener(listener);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        binding = DataBindingUtil.inflate(inflater, R.layout.project_info_dialog, container, false);
        return binding.getRoot();
    }

    @Override
    public void initViews(View view, Bundle savedInstanceState) {

    }

    @Override
    public void setupClickListener() {
       /* binding.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownlodeLIstDialogListener listener = DowblodeListDialog.this.getListener();
                if (listener != null)
                {
                   *//* if(binding.serviceNameEdt.getText().toString().equals(""))
                    {
                        showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_service_name));

                    }
                    else if(binding.unitEdt.getText().toString().equals(""))
                    {
                        showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_unit));
                    }

                    else if(binding.priceEdt.getText().toString().equals(""))
                    {
                        showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_price));
                    }
                    else if(binding.gstEdt.getText().toString().equals(""))
                    {
                        showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_gst));
                    }
                    else
                    {
                        ServiceModel obj=new ServiceModel();
                   *//**//* if (checkValidation())
                    {*//**//*

                        obj.setGst(binding.gstEdt.getText().toString());
                        obj.setServiceName(binding.serviceNameEdt.getText().toString());
                        obj.setUnit(binding.unitEdt.getText().toString());
                        obj.setPrice(binding.priceEdt.getText().toString());
                        obj.setTotal(binding.TotalEdt.getText().toString());
                        // }
                        listener.onOk(_self, obj);
                    }*//*
                }
                else
                {
                    DowblodeListDialog.this.dismiss();
                }
            }
        });*/
       /* binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DownlodeLIstDialogListener listener = DowblodeListDialog.this.getListener();
                if (listener != null) {
                    listener.onCancel(_self);
                } else {
                    DowblodeListDialog.this.dismiss();
                }
            }
        });*/


    }

    @Override
    public void loadData() {

    }
    public ProjectInfoDialog() {
        _self = this;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState)
    {
        dialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
       /* dialog.setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                return keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP;
            }
        });*/




        if (dialog.getWindow() != null)
        {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            dialog.getWindow().setGravity(Gravity.CENTER);
            //    dialog.getWindow().setLayout(500,500);
            //dialog.getWindow().addFlags(WindowManager.LayoutParams.FLAG_BLUR_BEHIND);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        }



        return dialog;
    }

    public void setupListener(ProjectInfoDialogListener listener)
    {
        mListener = listener;
    }

    private ProjectInfoDialogListener getListener()
    {
        ProjectInfoDialogListener listener = mListener;

        if (listener==null && getTargetFragment()!=null && getTargetFragment() instanceof ProjectInfoDialogListener)
            listener = (ProjectInfoDialogListener) getTargetFragment();

        if (listener==null && getActivity()!=null && getActivity() instanceof ProjectInfoDialogListener)
            listener = (ProjectInfoDialogListener) getActivity();

        return listener;
    }

    public interface ProjectInfoDialogListener {
        void onOk(ProjectInfoDialog dialogFragment);
        default void onCancel(ProjectInfoDialog dialogFragment){}
    }


   /* public boolean checkValidation()
    {
        if (TextUtils.isEmpty(binding.serviceNameEdt.getText().toString()))
        {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_service_name));
            // showSnackBar(binding.getRoot(), mContext.getString(R.string.please_enter_valid_email));
            return false;
        }
        if (TextUtils.isEmpty(binding.unitEdt.getText().toString()))
        {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_unit));
            return false;
        }

        return true;
    }*/

  /*  private void showSnackBar(View view, String msg)
    {

        System.out.println("inside showsnackbar"+"msg"+msg  +"view"+view);
        Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
    }*/


}
