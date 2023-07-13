package com.mineexcellence.sblastingapp.app_utils;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{

    public abstract RecyclerView.ViewHolder getViewHolder(LayoutInflater inflater, ViewGroup group);
    public RecyclerView.ViewHolder getLoadingViewHolder(LayoutInflater inflater, ViewGroup group)
    {
        return getViewHolder(inflater, group);
    };

}
