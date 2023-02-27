package com.smart_blasting_drilling.android.ui.activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.databinding.PerformancFragmentBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.entities.BlastPerformanceEntity;
import com.smart_blasting_drilling.android.utils.StringUtill;

public class PerformanceActivity extends BaseActivity {
    PerformancFragmentBinding binding;
    ResponseBladesRetrieveData bladesRetrieveData;
    AllTablesData allTablesData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.performanc_fragment);

        if (isBundleIntentNotEmpty()) {
            bladesRetrieveData = (ResponseBladesRetrieveData) getIntent().getExtras().getSerializable("blades_data");
            allTablesData = (AllTablesData) getIntent().getExtras().getSerializable("all_table_Data");
        }

        binding.headerPerformance.perfrmanceTitle.setText(getString(R.string.performance));

        binding.headerPerformance.backImg.setOnClickListener(view -> finish());

        String[] yesNoStr = new String[]{"Yes", "No"};
        String[] goodBadStr = new String[]{"Good", "Bad"};
        String[] muckProfile = new String[]{"Staggered"};

        binding.noEdtSpinner.setAdapter(Constants.getAdapter(this, yesNoStr));
        binding.streamingInjectionSpinner.setAdapter(Constants.getAdapter(this, yesNoStr));
        binding.mukProfileSpinner.setAdapter(Constants.getAdapter(this, muckProfile));
        binding.heaveSwellSpinner.setAdapter(Constants.getAdapter(this, goodBadStr));

        binding.saveBtn.setOnClickListener(view -> {
            if (isValidate()) {
                JsonObject jsonObject = new JsonObject();
                jsonObject.addProperty("FlyRock", binding.flyrocksEdt.getText().toString());
                jsonObject.addProperty("DisPlace", binding.displaceEdt.getText().toString());
                jsonObject.addProperty("BlastingFumes", binding.noEdtSpinner.getText().toString());
                jsonObject.addProperty("MukeProfile", binding.mukProfileSpinner.getText().toString());
                jsonObject.addProperty("HeaveSwell", binding.heaveSwellSpinner.getText().toString());
                jsonObject.addProperty("SteamingEject", binding.streamingInjectionSpinner.getText().toString());
                jsonObject.addProperty("BackBreak", binding.bsckbreakEdt.getText().toString());
                jsonObject.addProperty("BounderCount", binding.bounderCountEdt.getText().toString());

                String data = new Gson().toJson(jsonObject);
                BlastPerformanceEntity entity = new BlastPerformanceEntity();
                entity.designId = bladesRetrieveData.getDesignId();
                entity.setData(data);
                if (!appDatabase.blastPerformanceDao().isExistItem(bladesRetrieveData.getDesignId())) {
                    appDatabase.blastPerformanceDao().insertItem(entity);
                    showToast("Data added successfully");
                } else {
                    appDatabase.blastPerformanceDao().updateItem(bladesRetrieveData.getDesignId(), data);
                    showToast("Data insert successfully");
                }
            }
        });

    }

    public boolean isValidate() {
        if (StringUtill.isEmpty(binding.flyrocksEdt.getText().toString())) {
            showSnackBar(binding.getRoot(), "Please fill fly rock field");
            return false;
        }
        if (StringUtill.isEmpty(binding.displaceEdt.getText().toString())) {
            showSnackBar(binding.getRoot(), "Please fill displace field");
            return false;
        }
        if (StringUtill.isEmpty(binding.bsckbreakEdt.getText().toString())) {
            showSnackBar(binding.getRoot(), "Please fill back break field");
            return false;
        }
        if (StringUtill.isEmpty(binding.bounderCountEdt.getText().toString())) {
            showSnackBar(binding.getRoot(), "Please fill bounder count field");
            return false;
        }
        return true;
    }

}