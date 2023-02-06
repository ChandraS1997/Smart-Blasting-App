package com.smart_blasting_drilling.android.ui.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.ui.adapter.ProjectLIstAdapter;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.app.BaseApplication;
import com.smart_blasting_drilling.android.databinding.ActivityHomeBinding;
import com.smart_blasting_drilling.android.dialogs.DownloadListDialog;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.AppDatabase;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.TextUtil;
import com.smart_blasting_drilling.android.utils.ViewUtil;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends BaseActivity {
    public NavController navController;
    public ActivityHomeBinding binding;
    int projectFragType = 0;
    AppDatabase appDatabase;
    List<ResponseBladesRetrieveData> projectList = new ArrayList<>();
    ProjectLIstAdapter projectLIstAdapter;

    public static void openHomeActivity(Context context) {
        context.startActivity(new Intent(context, HomeActivity.class));
        ((Activity) context).finishAffinity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);

        appDatabase = BaseApplication.getAppDatabase(this, Constants.DATABASE_NAME);

        hideKeyboard(this);

        projectList.clear();
        Type projectListType = new TypeToken<List<ResponseBladesRetrieveData>>(){}.getType();
        projectList.addAll(new Gson().fromJson(new Gson().toJson(appDatabase.project2DBladesDao().getAllBladesProject()), projectListType));
        projectList.addAll(new Gson().fromJson(new Gson().toJson(appDatabase.project3DBladesDao().getAllBladesProject()), projectListType));

        projectLIstAdapter = new ProjectLIstAdapter(this, projectList);
        binding.appLayout.projectListRv.setAdapter(projectLIstAdapter);

        StatusBarUtils.statusBarColor(this, R.color._FFA722);
        setPageTitle(getString(R.string.pro_title_list));
//        navController = Navigation.findNavController(this, R.id.nav_host_main);

//        setBottomUiNavigation(binding.appLayout.bottomNavigation.blastingBtn.getRootView());
        binding.appLayout.headerLayout.homeBtn.setVisibility(View.GONE);

        binding.appLayout.headerLayout.downBtn.setOnClickListener(view -> {
            showDownloadListDialog((is3DBlades, dialogFragment) -> {
                projectList.clear();
//                if (is3DBlades) {
                    projectList.addAll(new Gson().fromJson(new Gson().toJson(appDatabase.project3DBladesDao().getAllBladesProject()), projectListType));
//                } else {
                    projectList.addAll(new Gson().fromJson(new Gson().toJson(appDatabase.project2DBladesDao().getAllBladesProject()), projectListType));
//                }
                projectLIstAdapter.notifyDataSetChanged();
                dialogFragment.dismiss();
            });
        });

        binding.appLayout.bottomNavigation.designBtn.setOnClickListener(this::setBottomUiNavigation);
        binding.appLayout.bottomNavigation.drillingBtn.setOnClickListener(this::setBottomUiNavigation);
        binding.appLayout.bottomNavigation.blastingBtn.setOnClickListener(this::setBottomUiNavigation);

//        setNavigationView();
    }

    private void setNavigationView() {
        navController.addOnDestinationChangedListener((navController, navDestination, bundle) -> {
            if (navDestination.getId() == R.id.drillingFragment) {
                projectFragType = 0;
            } else if (navDestination.getId() == R.id.blastingFragment) {
                projectFragType = 1;
            }
        });
    }

    private void setBottomUiNavigation(View view) {
        switch (view.getId()) {
            case R.id.designBtn:
                binding.appLayout.bottomNavigation.designBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color._FFA722));
                binding.appLayout.bottomNavigation.designTxt.setTextColor(getColor(R.color.white));
                binding.appLayout.bottomNavigation.drillingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.drillingTxt.setTextColor(getColor(R.color.black));
                binding.appLayout.bottomNavigation.blastingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.blastingTxt.setTextColor(getColor(R.color.black));
                break;
            case R.id.drillingBtn:
                binding.appLayout.bottomNavigation.drillingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color._FFA722));
                binding.appLayout.bottomNavigation.drillingTxt.setTextColor(getColor(R.color.white));
                binding.appLayout.bottomNavigation.designBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.designTxt.setTextColor(getColor(R.color.black));
                binding.appLayout.bottomNavigation.blastingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.blastingTxt.setTextColor(getColor(R.color.black));
                navController.navigate(R.id.drillingFragment);
                break;
            case R.id.blastingBtn:
                binding.appLayout.bottomNavigation.blastingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color._FFA722));
                binding.appLayout.bottomNavigation.blastingTxt.setTextColor(getColor(R.color.white));
                binding.appLayout.bottomNavigation.designBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.designTxt.setTextColor(getColor(R.color.black));
                binding.appLayout.bottomNavigation.drillingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.drillingTxt.setTextColor(getColor(R.color.black));
                navController.navigate(R.id.blastingFragment);
                break;
        }
    }

    private void showDownloadListDialog(DownloadListDialog.DownloadLIstDialogListener downlodeLIstDialogListener) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        DownloadListDialog infoDialogFragment = DownloadListDialog.getInstance(downlodeLIstDialogListener);
        infoDialogFragment.setupListener(downlodeLIstDialogListener);
        ft.add(infoDialogFragment, DownloadListDialog.TAG);
        ft.commitAllowingStateLoss();
    }

    public void setPageTitle(String title) {
        if (!TextUtil.isEmpty(title)) {
            binding.appLayout.headerLayout.pageTitle.setText(TextUtil.getString(title));
        }
    }

}
