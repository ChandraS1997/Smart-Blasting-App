package com.mineexcellence.sblastingapp.ui.fragments;

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
import androidx.recyclerview.widget.RecyclerView;

import com.mineexcellence.sblastingapp.R;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseBladesRetrieveData;
import com.mineexcellence.sblastingapp.api.apis.response.ResponseHoleDetailData;
import com.mineexcellence.sblastingapp.app.BaseFragment;
import com.mineexcellence.sblastingapp.app.CoordinationHoleHelperKt;
import com.mineexcellence.sblastingapp.databinding.FragmentMapviewBinding;
import com.mineexcellence.sblastingapp.helper.Constants;
import com.mineexcellence.sblastingapp.ui.activity.BaseActivity;
import com.mineexcellence.sblastingapp.ui.activity.HoleDetailActivity;
import com.mineexcellence.sblastingapp.ui.adapter.MapHolePointAdapter;
import com.mineexcellence.sblastingapp.ui.models.MapHoleDataModel;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MapViewFragment extends BaseFragment implements HoleDetailActivity.HoleDetailCallBackListener {
    FragmentMapviewBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (((HoleDetailActivity) mContext).binding.holeDetailLayoutContainer.getVisibility() == View.VISIBLE) {
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
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mapview, container, false);

            ((HoleDetailActivity) mContext).holeDetailCallBackListener = this;

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

    @Override
    public void onResume() {
        super.onResume();
        getMapHoleDataList();
    }

    void getMapHoleDataList() {
        List<ResponseHoleDetailData> holeDetailDataList = ((HoleDetailActivity) mContext).holeDetailDataList;
        ResponseBladesRetrieveData bladesRetrieveData = ((HoleDetailActivity) mContext).bladesRetrieveData;
        List<MapHoleDataModel> colHoleDetailDataList = new ArrayList<>();

        CoordinationHoleHelperKt.mapCoordinatesAndroid(holeDetailDataList.get(0).getX(), holeDetailDataList.get(0).getY(), holeDetailDataList);

        if (!Constants.isListEmpty(holeDetailDataList)) {
            colHoleDetailDataList = ((BaseActivity) mContext).getRowWiseHoleList(holeDetailDataList);

            MapHolePointAdapter adapter = new MapHolePointAdapter(mContext, colHoleDetailDataList);
            binding.rowHolePoint.setAdapter(adapter);

            int dp2px = 0;

            for (MapHoleDataModel model : colHoleDetailDataList) {
                if (model.getHoleDetailDataList().size() > dp2px) {
                    dp2px = model.getHoleDetailDataList().size();
                }
            }

            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(dp2px * (137 + 35), RecyclerView.LayoutParams.WRAP_CONTENT);
            binding.rowHolePoint.setLayoutParams(layoutParams);
        }
    }

    @Override
    public void setHoleDetailCallBack(ResponseBladesRetrieveData data, ResponseHoleDetailData detailData) {
        ((HoleDetailActivity) mContext).binding.holeDetailLayoutContainer.setVisibility(View.VISIBLE);
        ((HoleDetailActivity) mContext).setHoleDetailDialog(data, detailData);
    }

    @Override
    public void saveAndCloseHoleDetailCallBack() {
        if (Constants.holeBgListener != null)
            Constants.holeBgListener.setBackgroundRefresh();
        ((HoleDetailActivity) mContext).binding.holeDetailLayoutContainer.setVisibility(View.GONE);
    }

    @Override
    public void setBgOfHoleOnNewRowChange(int row, int hole, int pos) {
        if (((HoleDetailActivity) mContext).updateRowNo == -1) {
            ((HoleDetailActivity) mContext).updateRowNo = row;
        } else {
            if (row != ((HoleDetailActivity) mContext).updateRowNo) {
                if (Constants.hole3DBgListener != null)
                    Constants.hole3DBgListener.setBackgroundRefresh();
            }
        }
    }
}
