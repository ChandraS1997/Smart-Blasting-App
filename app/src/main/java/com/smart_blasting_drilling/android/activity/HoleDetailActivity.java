package com.smart_blasting_drilling.android.activity;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.HoleDetailActivityBinding;
import com.smart_blasting_drilling.android.dialogs.DowblodeListDialog;
import com.smart_blasting_drilling.android.dialogs.ProjectInfoDialog;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.onHoleClickListener;

public class HoleDetailActivity extends BaseActivity implements View.OnClickListener, onHoleClickListener {
    HoleDetailActivityBinding binding;
    public NavController navController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.hole_detail_activity);
        navController = Navigation.findNavController(this, R.id.nav_host_hole);
        Constants.onHoleClickListener = this;
        //setPageTitle(getString(R.string.hole_detail));
        binding.headerLayHole.pageTitle.setText(getString(R.string.hole_detail));
        binding.headerLayHole.projectInfo.setText(getString(R.string.project_info));
        binding.bottomHoleNavigation.mapBtn.setOnClickListener(this);
        binding.bottomHoleNavigation.listBtn.setOnClickListener(this);

     /*   if (binding.holeParaLay.getVisibility() == View.VISIBLE) {
            binding.holeParaLay.setVisibility(View.GONE);
        } else if (binding.holeParaLay.getVisibility() == View.GONE) {
            binding.holeParaLay.setVisibility(View.VISIBLE);
        }*/
        binding.headerLayHole.projectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               /* showProjectInfoDialog(new ProjectInfoDialog.ProjectInfoDialogListener() {
                    @Override
                    public void onOk(ProjectInfoDialog dialogFragment) {
                        dialogFragment.dismiss();
                        //servicelist.add(obj);
                        //  serviceListAdapters.notifyDataSetChanged();
                        // dialogFragment.dismiss();
                    }

                    @Override
                    public void onCancel(ProjectInfoDialog dialogFragment) {
                        //AddServiceDialog.ServiceDialogListener.super.onCancel(dialogFragment);
                        dialogFragment.dismiss();
                    }
                });*/

               // if (frommapview.equals("mapviewFragment")) {
                    binding.projectInfoContainer.setVisibility(View.VISIBLE);
                binding.holeParaLay.setVisibility(View.GONE);
               // } else {
                //    binding.holeParaLay.setVisibility(View.GONE);
              //  }

            }


        });
       /* binding.bottomHoleNavigation.mapBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {

            }
        });*/
        // binding.headerLayHole.project_info.setText(getString(R.string.project_info));

    }

    private void showProjectInfoDialog(ProjectInfoDialog.ProjectInfoDialogListener projectInfoDialogListener) {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ProjectInfoDialog infoDialogFragment = ProjectInfoDialog.getInstance(projectInfoDialogListener);
        infoDialogFragment.setupListener(projectInfoDialogListener);
        ft.add(infoDialogFragment, DowblodeListDialog.TAG);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.mapBtn:
        binding.headerLayHole.projectInfo.setVisibility(View.GONE);
           binding.projectInfoContainer.setVisibility(View.GONE);
           binding.holeParaLay.setVisibility(View.GONE);
                navController.navigate(R.id.MapViewFrament);
                //  finish();
                break;
            /*    binding.bottomHoleNavigation.closeDrawer(GravityCompat.START);
                if (Objects.requireNonNull(navController.getCurrentDestination()).getId() != R.id.homeFragment) {
                    navController.navigate(R.id.homeFragment);
                }
                break;*/
            case R.id.listBtn:
                binding.headerLayHole.projectInfo.setVisibility(View.VISIBLE);
                binding.projectInfoContainer.setVisibility(View.GONE);
                binding.holeParaLay.setVisibility(View.GONE);
                navController.navigate(R.id.HoleDetailsTableViewFragment);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }

    @Override
    public void onHoleClicik(String frommapview) {
        //binding.holeParameteresLayout.
        if (frommapview.equals("mapviewFragment")) {
            binding.holeParaLay.setVisibility(View.VISIBLE);
            binding.projectInfoContainer.setVisibility(View.GONE);
        } else {
            binding.holeParaLay.setVisibility(View.GONE);
        }

        // v.setVisibility(View.VISIBLE);
        // holeParaLay


    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // if (binding.holeParaLay.getVisibility() == View.VISIBLE) {
            binding.holeParaLay.setVisibility(View.GONE);
        binding.headerLayHole.projectInfo.setVisibility(View.VISIBLE);
        //}
        /*else if (binding.holeParaLay.getVisibility() == View.GONE) {
            binding.holeParaLay.setVisibility(View.VISIBLE);
        }*/
        //finish();
    }
}
