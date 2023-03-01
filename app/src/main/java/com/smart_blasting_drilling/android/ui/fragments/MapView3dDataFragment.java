package com.smart_blasting_drilling.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentMapviewBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.adapter.MapHolePointAdapter;
import com.smart_blasting_drilling.android.ui.models.MapHoleDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MapView3dDataFragment extends BaseFragment {
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

            ((HoleDetail3DModelActivity) mContext).mapViewDataUpdateLiveData.observe((LifecycleOwner) mContext, aBoolean -> {
                if (aBoolean) {
                    try {
                        ((HoleDetail3DModelActivity) mContext).runOnUiThread(new TimerTask() {
                            @Override
                            public void run() {
                                getMapHoleDataList();
                            }
                        });
                    } catch (Exception e) {
                        e.getLocalizedMessage();
                    }
                    ((HoleDetail3DModelActivity) mContext).mapViewDataUpdateLiveData.setValue(false);
                }
            });

        }

        return binding.getRoot();
    }

    void getMapHoleDataList() {
        List<ResponseHoleDetailData> holeDetailDataList = ((HoleDetail3DModelActivity) mContext).holeDetailDataList;
        List<MapHoleDataModel> colHoleDetailDataList = new ArrayList<>();

        if (!Constants.isListEmpty(holeDetailDataList)) {
            colHoleDetailDataList = ((BaseActivity) mContext).getRowWiseHoleList(holeDetailDataList);

            MapHolePointAdapter adapter = new MapHolePointAdapter(mContext, colHoleDetailDataList);
            binding.rowHolePoint.setAdapter(adapter);
        }
    }

}