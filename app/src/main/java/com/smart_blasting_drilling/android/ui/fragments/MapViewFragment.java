package com.smart_blasting_drilling.android.ui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentMapviewBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.adapter.MapHolePointAdapter;
import com.smart_blasting_drilling.android.ui.models.MapHoleDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MapViewFragment extends BaseFragment {
    FragmentMapviewBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null)
                    Navigation.findNavController(getView()).navigateUp();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mapview, container, false);

            getMapHoleDataList();

            ((HoleDetailActivity) mContext).mapViewDataUpdateLiveData.observe((LifecycleOwner) mContext, aBoolean -> {
                if (aBoolean) {
                    try {
                        ((HoleDetailActivity) mContext).runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                getMapHoleDataList();
                            }
                        });
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                    ((HoleDetailActivity) mContext).mapViewDataUpdateLiveData.setValue(false);
                }
            });

        }

        return binding.getRoot();
    }

    void getMapHoleDataList() {
        List<ResponseHoleDetailData> holeDetailDataList = ((HoleDetailActivity) mContext).holeDetailDataList;
        ResponseBladesRetrieveData bladesRetrieveData = ((HoleDetailActivity) mContext).bladesRetrieveData;
        List<MapHoleDataModel> colHoleDetailDataList = new ArrayList<>();

        if (!Constants.isListEmpty(holeDetailDataList)) {
            colHoleDetailDataList = ((BaseActivity) mContext).getRowWiseHoleList(holeDetailDataList);

            MapHolePointAdapter adapter = new MapHolePointAdapter(mContext, colHoleDetailDataList);
            binding.rowHolePoint.setAdapter(adapter);
        }
    }

}
