package com.zero.library.mvp.presenter;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zero.library.mvp.view.IDelegate;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public abstract class FragmentPresenter<T extends IDelegate> extends Fragment {

    public T viewDelegate;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            this.viewDelegate = getT(this, 0);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        this.viewDelegate.create(inflater, container, savedInstanceState);
        return this.viewDelegate.getRootView();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.viewDelegate.initWidget();
        this.bindEvenListener();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        if(this.viewDelegate.getOptionMenuId() != 0){
            inflater.inflate(this.viewDelegate.getOptionMenuId(), menu);
        }
    }

    @Override
    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if(this.viewDelegate == null){
            try {
                this.viewDelegate = getT(this, 0);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onDestroy() {
        this.viewDelegate = null;
        super.onDestroy();
    }

    protected void bindEvenListener(){}

//    protected abstract Class<T> getDelegateClass();

    public <T> T getT(Object object, int i){
        Type type = object.getClass().getGenericSuperclass();
        if(type instanceof ParameterizedType){
            try{
                return ((Class<T>)((ParameterizedType)type).getActualTypeArguments()[i]).newInstance();
            } catch (java.lang.InstantiationException e){
                e.printStackTrace();
            } catch (IllegalAccessException e){
                e.printStackTrace();
            }
        }
        return null;
    }
}
