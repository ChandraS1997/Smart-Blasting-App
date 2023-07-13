package com.mineexcellence.sblastingapp.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.mineexcellence.sblastingapp.app.BaseFragment;
import com.mineexcellence.sblastingapp.databinding.FragmentMapview3dBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetail3DModelActivity;
import com.mineexcellence.sblastingapp.ui.adapter.MapHolePoint3dAdapter;
import com.mineexcellence.sblastingapp.ui.models.MapHole3DDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MapView3dDataFragment extends BaseFragment implements HoleDetail3DModelActivity.HoleDetailCallBackListener {
    FragmentMapview3dBinding binding;
    MapHolePoint3dAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (((HoleDetail3DModelActivity) mContext).binding.holeDetailLayoutContainer.getVisibility() == View.VISIBLE) {
                    saveAndCloseHoleDetailCallBack();
                } else {
                    if (getView() != null)
                        Navigation.findNavController(getView()).navigateUp();
                }
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mapview_3d, container, false);

            ((HoleDetail3DModelActivity) mContext).holeDetailCallBackListener = this;

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

    @Override
    public void onResume() {
        super.onResume();
        getMapHoleDataList();
    }

    void getMapHoleDataList() {
        List<Response3DTable4HoleChargingDataModel> holeDetailDataList = ((HoleDetail3DModelActivity) mContext).holeDetailDataList;
        List<MapHole3DDataModel> colHoleDetailDataList = new ArrayList<>();

        if (!Constants.isListEmpty(holeDetailDataList)) {
            colHoleDetailDataList = ((BaseActivity) mContext).getRowWiseHoleIn3dList(holeDetailDataList);

            MapHolePoint3dAdapter adapter = new MapHolePoint3dAdapter(mContext, colHoleDetailDataList);
            binding.rowHolePoint.setAdapter(adapter);

            int dp2px = 0;

            for (MapHole3DDataModel model : colHoleDetailDataList) {
                if (model.getHoleDetailDataList().size() > dp2px) {
                    dp2px = model.getHoleDetailDataList().size();
                }
            }

            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(dp2px * (137 + 15), RecyclerView.LayoutParams.WRAP_CONTENT);
            binding.rowHolePoint.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void setHoleDetailCallBack(Response3DTable4HoleChargingDataModel detailData) {
        ((HoleDetail3DModelActivity) mContext).binding.holeDetailLayoutContainer.setVisibility(View.VISIBLE);
        ((HoleDetail3DModelActivity) mContext).binding.holeDetailLayout.scrollView.fullScroll(ScrollView.FOCUS_UP);
        ((HoleDetail3DModelActivity) mContext).set3dHoleDetail(((HoleDetail3DModelActivity) mContext).bladesRetrieveData.get(0), detailData);
    }

    @Override
    public void saveAndCloseHoleDetailCallBack() {
        if (Constants.hole3DBgListener != null)
            Constants.hole3DBgListener.setBackgroundRefresh();
        ((HoleDetail3DModelActivity) mContext).binding.holeDetailLayoutContainer.setVisibility(View.GONE);
    }

    @Override
    public void setBgOfHoleOnNewRowChange(int row, int hole, int pos) {
        if (((HoleDetail3DModelActivity) mContext).updateRowNo == -1) {
            ((HoleDetail3DModelActivity) mContext).updateRowNo = row;
        } else {
            if (row != ((HoleDetail3DModelActivity) mContext).updateRowNo) {
                if (Constants.hole3DBgListener != null)
                    Constants.hole3DBgListener.setBackgroundRefresh();
            }
        }
    }
}