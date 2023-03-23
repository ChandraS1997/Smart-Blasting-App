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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.ResponseBladesRetrieveData;
import com.smart_blasting_drilling.android.api.apis.response.ResponseHoleDetailData;
import com.smart_blasting_drilling.android.api.apis.response.hole_tables.AllTablesData;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentMapviewBinding;
import com.smart_blasting_drilling.android.dialogs.HoleDetailDialog;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.adapter.HoleItemAdapter;
import com.smart_blasting_drilling.android.ui.adapter.MapHolePointAdapter;
import com.smart_blasting_drilling.android.ui.models.MapHole3DDataModel;
import com.smart_blasting_drilling.android.ui.models.MapHoleDataModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

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

        ((BaseActivity) mContext).mapCoordinatesAndroid(holeDetailDataList.get(0).getX(), holeDetailDataList.get(0).getY(), holeDetailDataList);

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

            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(dp2px * (137 + 5), RecyclerView.LayoutParams.WRAP_CONTENT);
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
