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
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable1DataModel;
import com.smart_blasting_drilling.android.api.apis.response.table_3d_models.Response3DTable4HoleChargingDataModel;
import com.smart_blasting_drilling.android.app.AppDelegate;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentMapview3dBinding;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.ProjectHoleDetailRowColDao;
import com.smart_blasting_drilling.android.room_database.dao_interfaces.UpdateProjectBladesDao;
import com.smart_blasting_drilling.android.room_database.entities.AllProjectBladesModelEntity;
import com.smart_blasting_drilling.android.room_database.entities.ProjectHoleDetailRowColEntity;
import com.smart_blasting_drilling.android.room_database.entities.UpdateProjectBladesEntity;
import com.smart_blasting_drilling.android.ui.activity.BaseActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetail3DModelActivity;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.adapter.MapHolePoint3dAdapter;
import com.smart_blasting_drilling.android.ui.models.MapHole3DDataModel;
import com.smart_blasting_drilling.android.utils.StringUtill;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

public class MapView3dDataFragment extends BaseFragment implements HoleDetail3DModelActivity.HoleDetailCallBackListener {
    FragmentMapview3dBinding binding;

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

        ((BaseActivity) mContext)._3dMapCoordinatesAndroid(Double.parseDouble(holeDetailDataList.get(0).getTopX()), Double.parseDouble(holeDetailDataList.get(0).getTopY()), holeDetailDataList);

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
        ((HoleDetail3DModelActivity) mContext).set3dHoleDetail(((HoleDetail3DModelActivity) mContext).bladesRetrieveData.get(0), detailData);
    }

    @Override
    public void saveAndCloseHoleDetailCallBack() {
        if (Constants.hole3DBgListener != null)
            Constants.hole3DBgListener.setBackgroundRefresh();
        ((HoleDetail3DModelActivity) mContext).binding.holeDetailLayoutContainer.setVisibility(View.GONE);
    }

}