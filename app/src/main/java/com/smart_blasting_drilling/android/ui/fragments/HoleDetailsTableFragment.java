package com.smart_blasting_drilling.android.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.smart_blasting_drilling.android.R;
import com.smart_blasting_drilling.android.helper.Constants;
import com.smart_blasting_drilling.android.interfaces.OnDataEditTable;
import com.smart_blasting_drilling.android.ui.activity.HoleDetailActivity;
import com.smart_blasting_drilling.android.ui.adapter.TableViewAdapter;
import com.smart_blasting_drilling.android.app.BaseFragment;
import com.smart_blasting_drilling.android.databinding.FragmentHoleDetailsTableBinding;
import com.smart_blasting_drilling.android.ui.models.TableEditModel;

import java.util.ArrayList;
import java.util.List;

public class HoleDetailsTableFragment extends BaseFragment implements OnDataEditTable {

    FragmentHoleDetailsTableBinding binding;
    List<String> tabList = new ArrayList<>();
    TableViewAdapter tableViewAdapter;
    ArrayList<TableEditModel> tableEditModelArrayList = new ArrayList<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (getView() != null)
                    ((HoleDetailActivity) mContext).finish();
            }
        });
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (binding == null) {
            binding = DataBindingUtil.inflate(inflater, R.layout.fragment_hole_details_table, container, false);

            Constants.onDataEditTable = this;

            tabList.add("Hole No");
            tabList.add("Hole Id ");
            tabList.add("Hole Depth");
            tabList.add("Hole Status");
            tabList.add("Hole Depth");
            tabList.add("Hole Angle");
            tabList.add("Diameter");
            tabList.add("Burden");
            tabList.add("Spacing");
            tabList.add("X");
            tabList.add("Y");
            tabList.add("Z");
            tabList.add("Charging");

            tableViewAdapter = new TableViewAdapter(mContext, tabList,tableEditModelArrayList);
            binding.tableRv.setAdapter(tableViewAdapter);
        }
        return binding.getRoot();
    }

    @Override
    public void editDataTable(ArrayList<TableEditModel> arrayList) {
        tableEditModelArrayList.clear();
        tableEditModelArrayList.addAll(arrayList);
        tableViewAdapter.notifyDataSetChanged();
    }
}