package com.zero.library.mvp.databind;

import android.os.Bundle;

import androidx.annotation.Nullable;

import com.zero.library.mvp.model.IModel;
import com.zero.library.mvp.presenter.ActivityPresenter;
import com.zero.library.mvp.view.IDelegate;

public abstract class DataBindActivity<T extends IDelegate> extends ActivityPresenter<T> {

    protected DataBinder binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.binding = this.getDataBinder();
    }

    public abstract DataBinder getDataBinder();

    public <D extends IModel> void notifyModelChanged(D data){
        if(this.binding != null){
            this.binding.viewBindModel(this.viewDelegate, data);
        }
    }
}
