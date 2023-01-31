package com.smart_blasting_drilling.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smart_blasting_drilling.android.BuildConfig;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.activity.AuthActivity;
import com.smart_blasting_drilling.android.activity.BaseActivity;
import com.smart_blasting_drilling.android.activity.HomeActivity;
import com.smart_blasting_drilling.android.api.apis.Service.MainService;
import com.smart_blasting_drilling.android.api.apis.response.ResponseLoginData;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.SignupFragmentBinding;
import com.smart_blasting_drilling.android.utils.StringUtill;
import com.smart_blasting_drilling.android.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class SignUpFragment extends BaseFragment implements View.OnClickListener
{
    SignupFragmentBinding binding;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.signup_fragment, container, false);
            binding.signupbtn.setOnClickListener(this);
        }
        return binding.getRoot();
    }


    @Override
    public void onClick(View view) {
       // Navigation.findNavController(binding.getRoot()).navigate(R.id.loginFragment);
        if (checkValidation()) {
            registerApiCaller();
        }

    }

    private boolean checkValidation()
    {
        if (TextUtils.isEmpty(binding.nameEdt.getText().toString()))
        {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.entername));
            return false;
        }
        if (TextUtils.isEmpty(binding.emailEdt.getText().toString()))
        {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_emai));
            return false;
        }
        if (!ValidationUtils.validateEmail(binding.emailEdt.getText().toString())) {
            showSnackBar(binding.getRoot(), getString(R.string.enter_valid_email));
            return false;
        }
        if (TextUtils.isEmpty(binding.phoneNumberEdt.getText().toString()))
        {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_number));
            return false;
        }
        if (binding.phoneNumberEdt.getText().toString().trim().length() != 10) {
            showSnackBar(binding.getRoot(), getString(R.string.valid_number));
            return false;
        }
        if (TextUtils.isEmpty(binding.companyEdt.getText().toString()))
        {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.entercompany));
            return false;
        }


       /* if (TextUtils.isEmpty(binding.companyEdt.getText().toString()))
        {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_company));
            return false;
        }*/
        return  true;
    }
    private void registerApiCaller()
    {

        showLoader();
        Map<String, RequestBody> map = new HashMap<>();
        map.put("sendemail_getaccess", toRequestBody(String.valueOf(true)));
        map.put("name", toRequestBody(StringUtill.getString(binding.nameEdt.getText().toString())));
        map.put("email", toRequestBody(StringUtill.getString(binding.emailEdt.getText().toString())));
        map.put("phoneno", toRequestBody(StringUtill.getString(binding.phoneNumberEdt.getText().toString())));
        map.put("country", toRequestBody("india"));
        map.put("company", toRequestBody(StringUtill.getString(binding.companyEdt.getText().toString())));
        map.put("comment", toRequestBody(StringUtill.getString(binding.commentEdt.getText().toString())));
        MainService.RegisterApiCaller(mContext, map).observe((LifecycleOwner) mContext, responsesignup -> {
            if (responsesignup == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {

                if (!(responsesignup.isJsonNull())) {

                    try {

                        JsonObject jsonObject = responsesignup.getAsJsonObject();
                        if (jsonObject != null) {

                            try {

                                ResponseLoginData signupResponse = new Gson().fromJson(responsesignup, ResponseLoginData.class);

                                showSnackBar(binding.getRoot(), StringUtill.getString(responsesignup.getAsJsonObject().get("Message").getAsString()));
                               if (responsesignup.getAsJsonObject().has("response"))
                                {

                                    if (responsesignup.getAsJsonObject().get("response").getAsString().equals("fail")) {
                                        System.out.println("inside register api if if");
                                        showSnackBar(binding.getRoot(), StringUtill.getString(responsesignup.getAsJsonObject().get("Message").getAsString()));
                                        mContext.startActivity(new Intent(mContext, AuthActivity.class));
                                        ((AuthActivity) mContext).finishAffinity();
                                    }
                                } else {
                                   /* System.out.println("inside register api else");
                                    manger.putUserDetails(signupResponse);
                                    mContext.startActivity(new Intent(mContext, HomeActivity.class));
                                    ((AuthActivity) mContext).finishAffinity();*/
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
