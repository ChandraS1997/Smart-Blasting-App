package com.smart_blasting_drilling.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
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
import com.smart_blasting_drilling.android.databinding.FragmentLoginBinding;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.HashMap;
import java.util.Map;

import okhttp3.RequestBody;

public class LoginFragment extends BaseFragment implements View.OnClickListener {

    FragmentLoginBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false);

            binding.loginbtn.setOnClickListener(this);
            binding.showPassBtn.setOnClickListener(this);
            binding.requestaccess.setOnClickListener(this);

        }
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginbtn:
                if (checkValidation()) {
                    loginApiCaller();
                }
                break;

            case R.id.requestaccess:
           /*     if (checkValidation()) {
                    loginApiCaller();
                }*/
                Navigation.findNavController(binding.getRoot()).navigate(R.id.signUpFragment);
                break;
            case R.id.show_pass_btn:
                if (binding.passwordEdt.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
                    ((ImageView) (view)).setImageResource(R.drawable.icn_password_show);
                    ((ImageView) (view)).setColorFilter(ContextCompat.getColor(requireContext(), R.color._192d4d));
                    binding.passwordEdt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    binding.passwordEdt.setSelection(binding.passwordEdt.getText().length());
                } else {
                    ((ImageView) (view)).setImageResource(R.drawable.icn_password_hide);
                    ((ImageView) (view)).setColorFilter(ContextCompat.getColor(requireContext(), R.color._A8A8A8));
                    binding.passwordEdt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.passwordEdt.setSelection(binding.passwordEdt.getText().length());
                }
                break;
        }
    }

    public boolean checkValidation() {
        if (TextUtils.isEmpty(binding.emailEdt.getText().toString())) {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.enter_email));
            return false;
        }
        if (TextUtils.isEmpty(binding.passwordEdt.getText().toString())) {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.please_enter_password));
            return false;
        }
        if (binding.passwordEdt.getText().toString().trim().length() < 6) {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.pass_six_greater));
            return false;
        }
        return true;
    }

    private void loginApiCaller() {
        showLoader();
        Map<String, RequestBody> map = new HashMap<>();
        map.put("Login", toRequestBody(String.valueOf(true)));
        map.put("Email", toRequestBody(StringUtill.getString(binding.emailEdt.getText().toString())));
        map.put("Password", toRequestBody(StringUtill.getString(binding.passwordEdt.getText().toString())));
        map.put("appVersion", toRequestBody(BuildConfig.VERSION_NAME));
        map.put("Platform", toRequestBody("android"));

        MainService.loginApiCaller(mContext, map).observe((LifecycleOwner) mContext, responseLogin -> {
            if (responseLogin == null) {
                showSnackBar(binding.getRoot(), SOMETHING_WENT_WRONG);
            } else {
                if (!(responseLogin.isJsonNull())) {
                    try {
                        JsonObject jsonObject = responseLogin.getAsJsonObject();
                        if (jsonObject != null) {
                            try {
                                ResponseLoginData loginResponse = new Gson().fromJson(responseLogin, ResponseLoginData.class);
                                if (responseLogin.getAsJsonObject().has("errorcode") && responseLogin.getAsJsonObject().has("response")) {
                                    if (responseLogin.getAsJsonObject().get("errorcode").getAsString().equalsIgnoreCase("0") && responseLogin.getAsJsonObject().get("response").getAsString().equalsIgnoreCase("fail")) {
                                        showSnackBar(binding.getRoot(), StringUtill.getString(responseLogin.getAsJsonObject().get("Message").getAsString()));
                                    }
                                } else {
                                    manger.putUserDetails(loginResponse);
                                    mContext.startActivity(new Intent(mContext, HomeActivity.class));
                                    ((AuthActivity) mContext).finishAffinity();
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
