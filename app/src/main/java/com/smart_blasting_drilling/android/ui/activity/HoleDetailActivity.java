package com.smart_blasting_drilling.android.ui.activity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.databinding.DialogSelectionFieldLayoutBinding;
import com.smart_blasting_drilling.android.databinding.HoleDetailActivityBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.OnHoleClickListener;
import com.smart_blasting_drilling.android.ui.adapter.AdapterEditTableFields;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;
import com.smart_blasting_drilling.android.utils.StatusBarUtils;

import java.util.ArrayList;

public class HoleDetailActivity extends BaseActivity implements View.OnClickListener, OnHoleClickListener {
    public NavController navController;
    HoleDetailActivityBinding binding;
    ArrayList<TableEditModel> editModelArrayList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.hole_detail_activity);

        editModelArrayList.add(new TableEditModel("Hole No"));
        editModelArrayList.add(new TableEditModel("Hole Id "));
        editModelArrayList.add(new TableEditModel("Hole Depth"));
        editModelArrayList.add(new TableEditModel("Hole Status"));
        editModelArrayList.add(new TableEditModel("Hole Depth"));
        editModelArrayList.add(new TableEditModel("Hole Angle"));
        editModelArrayList.add(new TableEditModel("Diameter"));
        editModelArrayList.add(new TableEditModel("Burden"));
        editModelArrayList.add(new TableEditModel("Spacing"));
        editModelArrayList.add(new TableEditModel("X"));
        editModelArrayList.add(new TableEditModel("Y"));
        editModelArrayList.add(new TableEditModel("Z"));
        editModelArrayList.add(new TableEditModel("Charging"));

        navController = Navigation.findNavController(this, R.id.nav_host_hole);
        Constants.onHoleClickListener = this;

        StatusBarUtils.statusBarColor(this, R.color._FFA722);

        binding.headerLayHole.pageTitle.setText(getString(R.string.hole_detail));
        binding.headerLayHole.projectInfo.setText(getString(R.string.project_info));
        binding.headerLayHole.camIcon.setOnClickListener(this);
        binding.bottomHoleNavigation.mapBtn.setOnClickListener(this);
        binding.bottomHoleNavigation.listBtn.setOnClickListener(this);
        binding.headerLayHole.homeBtn.setOnClickListener(this);

        binding.headerLayHole.projectInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                binding.projectInfoContainer.setVisibility(View.VISIBLE);
                binding.holeParaLay.setVisibility(View.GONE);
            }
        });
        binding.headerLayHole.editTable.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.homeBtn:
                finish();
                break;
            case R.id.mapBtn:
                binding.headerLayHole.projectInfo.setVisibility(View.GONE);
                binding.projectInfoContainer.setVisibility(View.GONE);
                binding.holeParaLay.setVisibility(View.GONE);
                navController.navigate(R.id.mapViewFrament);
                break;
            case R.id.camIcon:
                startActivity(new Intent(this, MediaActivity.class));
                //finishAffinity();
                break;
            case R.id.listBtn:
                binding.headerLayHole.projectInfo.setVisibility(View.VISIBLE);
                binding.projectInfoContainer.setVisibility(View.GONE);
                binding.holeParaLay.setVisibility(View.GONE);
                navController.navigate(R.id.holeDetailsTableViewFragment);
                break;
            case R.id.editTable:
                editTable();
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }

    }

    @SuppressLint("SetTextI18n")
    public void editTable() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        DialogSelectionFieldLayoutBinding layoutBinding = DataBindingUtil.inflate(LayoutInflater.from(this), R.layout.dialog_selection_field_layout, (ViewGroup) binding.getRoot(), false);
        layoutBinding.headerLayHole.pageTitle.setVisibility(View.VISIBLE);
        layoutBinding.headerLayHole.pageTitle.setText("Edit Fields");
        layoutBinding.headerLayHole.projectInfo.setVisibility(View.GONE);
        layoutBinding.headerLayHole.camIcon.setVisibility(View.GONE);
        layoutBinding.headerLayHole.homeBtn.setVisibility(View.GONE);
        builder.setView(layoutBinding.getRoot());

        AdapterEditTableFields adapterEditTableFields = new AdapterEditTableFields(this, editModelArrayList);
        layoutBinding.tableEditRecycler.setAdapter(adapterEditTableFields);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (Constants.onDataEditTable != null) {
                    Constants.onDataEditTable.editDataTable(adapterEditTableFields.getSelectedData());
                }
                Log.e("15614564", new Gson().toJson(adapterEditTableFields.getSelectedData()));
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
               dialogInterface.dismiss();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }


    @Override
    public void onHoleClick(String frommapview) {
        if (frommapview.equals("mapviewFragment")) {
            binding.holeParaLay.setVisibility(View.VISIBLE);
            binding.projectInfoContainer.setVisibility(View.GONE);
        } else {
            binding.holeParaLay.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (binding.holeParaLay.getVisibility() == View.VISIBLE)
            binding.holeParaLay.setVisibility(View.GONE);
        if (binding.projectInfoContainer.getVisibility() == View.VISIBLE)
            binding.projectInfoContainer.setVisibility(View.GONE);
        binding.headerLayHole.projectInfo.setVisibility(View.VISIBLE);
    }


}
