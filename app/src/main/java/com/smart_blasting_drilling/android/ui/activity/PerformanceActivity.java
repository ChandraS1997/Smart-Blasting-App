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
import com.smart_blasting_drilling.android.utils.StatusBarUtils;
import com.smart_blasting_drilling.android.utils.StringUtill;

public class PerformanceActivity extends BaseActivity {
    PerformancFragmentBinding binding;
    ResponseBladesRetrieveData bladesRetrieveData;
    String designId = "";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.performanc_fragment);
        StatusBarUtils.statusBarColor(this, R.color._FFA722);

        if (isBundleIntentNotEmpty()) {
            designId = getIntent().getExtras().getString("blades_data");
            setPerformanceData();
        }

        binding.headerPerformance.perfrmanceTitle.setText(getString(R.string.performance));

        binding.headerPerformance.backImg.setOnClickListener(view -> finish());

        String[] yesNoStr = new String[]{"Yes", "No"};
        String[] goodBadStr = new String[]{"Good", "Average", "Medium", "Poor"};
        String[] muckProfile = new String[]{"Scattered", "Shallow & Disperesed", "Tight"};

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
                entity.designId = designId;
                entity.setData(data);
                if (!appDatabase.blastPerformanceDao().isExistItem(designId)) {
                    appDatabase.blastPerformanceDao().insertItem(entity);
                    showToast("Data added successfully");
                } else {
                    appDatabase.blastPerformanceDao().updateItem(designId, data);
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

    private void setPerformanceData() {
        try {
            if (appDatabase.blastPerformanceDao().isExistItem(designId)) {
                String data = appDatabase.blastPerformanceDao().getSingleItemEntity(designId).getData();
                JsonObject jsonObject = new Gson().fromJson(data, JsonObject.class);
                if (jsonObject != null) {
                    binding.flyrocksEdt.setText(StringUtill.getString(jsonObject.get("FlyRock").getAsString()));
                    binding.displaceEdt.setText(StringUtill.getString(jsonObject.get("DisPlace").getAsString()));
                    binding.noEdtSpinner.setText(StringUtill.getString(jsonObject.get("BlastingFumes").getAsString()));
                    binding.mukProfileSpinner.setText(StringUtill.getString(jsonObject.get("MukeProfile").getAsString()));
                    binding.heaveSwellSpinner.setText(StringUtill.getString(jsonObject.get("HeaveSwell").getAsString()));
                    binding.streamingInjectionSpinner.setText(StringUtill.getString(jsonObject.get("SteamingEject").getAsString()));
                    binding.bsckbreakEdt.setText(StringUtill.getString(jsonObject.get("BackBreak").getAsString()));
                    binding.bounderCountEdt.setText(StringUtill.getString(jsonObject.get("BounderCount").getAsString()));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}