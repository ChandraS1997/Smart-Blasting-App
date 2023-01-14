package com.smart_blasting_drilling.android.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.ActivityHomeBinding;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.TextUtil;
import com.smart_blasting_drilling.android.utils.ViewUtil;

import java.util.Objects;

public class HomeActivity extends BaseActivity
{
    public NavController navController;
    public ActivityHomeBinding binding;
    public static void openHomeActivity(Context context)
    {
        context.startActivity(new Intent(context, HomeActivity.class));
        ((Activity) context).finishAffinity();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_home);
        StatusBarUtils.statusBarColor(this, R.color.white);
        setPageTitle("Dashboard");
        navController = Navigation.findNavController(this, R.id.nav_host_main);
        binding.appLayout.headerLayout.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                System.out.println("inside back");
               // navController.navigateUp();

                if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.homeFragment)
                    //{
                    // navController.navigate(R.id.groceryListFragment);
                      finish();
                // }
            }
        });
    /*    binding.appLayout.headerLayout.backBtn.setOnClickListener(view -> {
           // if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.homeFragment)
            //{
               // navController.navigate(R.id.groceryListFragment);
                navController.navigateUp();
           // }
           *//* else
                if (Objects.requireNonNull(navController.getCurrentDestination()).getId() == R.id.groceryListFragment) {
                navController.navigate(R.id.homeFragment);
            } else {
                navController.navigateUp();
            }*//*
        });*/
        binding.appLayout.bottomNavigation.designBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {

                binding.appLayout.bottomNavigation.designBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color._FFA722));
               // binding.appLayout.bottomNavigation.designBtn.setBackgroundResource(R.color.white);
                binding.appLayout.bottomNavigation.designTxt.setTextColor(getColor(R.color.white));
                binding.appLayout.bottomNavigation.drillingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
              //  binding.appLayout.bottomNavigation.drillingTxt.setBackgroundResource(R.color.black);
                binding.appLayout.bottomNavigation.drillingTxt.setTextColor(getColor(R.color.black));
                binding.appLayout.bottomNavigation.blasstingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
               // binding.appLayout.bottomNavigation.blastingTxt.setBackgroundResource(R.color.black);
                binding.appLayout.bottomNavigation.blastingTxt.setTextColor(getColor(R.color.black));



            }
        });
        binding.appLayout.bottomNavigation.drillingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.appLayout.bottomNavigation.drillingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color._FFA722));
               // binding.appLayout.bottomNavigation.designTxt.setBackgroundResource(R.color.white);
                binding.appLayout.bottomNavigation.drillingTxt.setTextColor(getColor(R.color.white));
                binding.appLayout.bottomNavigation.designBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
               // binding.appLayout.bottomNavigation.designTxt.setBackgroundResource(R.color.black);
                binding.appLayout.bottomNavigation.designTxt.setTextColor(getColor(R.color.black));

                binding.appLayout.bottomNavigation.blasstingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
              //  binding.appLayout.bottomNavigation.blastingTxt.setBackgroundResource(R.color.black);
                binding.appLayout.bottomNavigation.blastingTxt.setTextColor(getColor(R.color.black));

            }
        });

        binding.appLayout.bottomNavigation.blasstingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.appLayout.bottomNavigation.blasstingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color._FFA722));
               // binding.appLayout.bottomNavigation.blastingTxt.setBackgroundResource(R.color.white);
                binding.appLayout.bottomNavigation.blastingTxt.setTextColor(getColor(R.color.white));
               // binding.appLayout.bottomNavigation.blastingTxt.setBackgroundColor(getColor(R.color.white));
                binding.appLayout.bottomNavigation.designBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
                binding.appLayout.bottomNavigation.designTxt.setTextColor(getColor(R.color.black));
               // binding.appLayout.bottomNavigation.designTxt.setBackgroundResource(R.color.black);
                binding.appLayout.bottomNavigation.drillingBtn.setBackground(ViewUtil.setViewBg(HomeActivity.this, 0, R.color.white));
              //  binding.appLayout.bottomNavigation.drillingTxt.setBackgroundResource(R.color.black);
                binding.appLayout.bottomNavigation.drillingTxt.setTextColor(getColor(R.color.black));

            }
        });



    }
    public void setPageTitle(String title) {
        if (!TextUtil.isEmpty(title)) {
            binding.appLayout.headerLayout.pageTitle.setText(TextUtil.getString(title));
        }
    }

}
