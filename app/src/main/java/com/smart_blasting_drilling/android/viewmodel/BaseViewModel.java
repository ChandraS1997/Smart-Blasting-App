package com.smart_blasting_drilling.android.viewmodel;

import androidx.lifecycle.ViewModel;

public abstract class BaseViewModel<T extends BaseModel> extends ViewModel {
    private static final String TAG = "BaseViewModel";

    private BaseModel<? extends BaseModel> model;

    public void init(BaseModel<T> model) {
        this.model = model;
    }

    protected BaseModel<?> getModel() {
        return model;
    }

    public abstract void onDestroy();
}
