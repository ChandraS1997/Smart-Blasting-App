package com.smart_blasting_drilling.android.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.Navigation;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.activity.AuthActivity;
import com.smart_blasting_drilling.android.activity.HomeActivity;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentLoginBinding;
import com.smart_blasting_drilling.android.utils.ValidationUtils;

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

            if (getArguments() != null) {
                if (getArguments().containsKey("from")) {
                    //from = getArguments().getString("from", "");
                }
            }
            binding.loginbtn.setOnClickListener(this);

        }
        return binding.getRoot();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.loginbtn:
                if (checkValidation()) {
                    String email = binding.emailEdt.getText().toString();
                    String password = binding.passwordEdt.getText().toString();
                    mContext.startActivity(new Intent(mContext, HomeActivity.class));
                    ((AuthActivity) mContext).finishAffinity();
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
        if (binding.passwordEdt.getText().toString().trim().length() < 8) {
            showSnackBar(binding.getRoot(), mContext.getString(R.string.pass_six_greater));
            return false;
        }
        return true;
    }
}
