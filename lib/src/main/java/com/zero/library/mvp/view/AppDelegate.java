package com.zero.library.mvp.view;

import android.app.Activity;
import android.os.Bundle;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.CallSuper;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.ubzx.library.R;
import com.zero.library.widget.progress.ProgressDialog;
import com.zero.library.widget.snakebar.Prompt;
import com.zero.library.widget.snakebar.TSnackbar;

public abstract class AppDelegate implements IDelegate{

    protected SparseArray<View> mViews = new SparseArray<>();

    protected View rootView;

    public ProgressDialog progressDialog;

    private boolean destroyed;

    public abstract int getRootLayoutId();

    @Override
    public void create(LayoutInflater inflater, ViewGroup container, Bundle saveInstanceState) {
        rootView = inflater.inflate(getRootLayoutId(), container, false);
    }

    @Override
    public int getOptionMenuId() {
        return 0;
    }

    public Toolbar getToolbar() {
        return null;
    }

    public void setRootView(View view){
        this.rootView = view;
    }

    @Override
    public View getRootView() {
        return rootView;
    }

    @CallSuper
    @Override
    public void initWidget() {
        this.progressDialog = new ProgressDialog(this.getActivity());
        this.progressDialog.builder();
        this.progressDialog.setCanceledOnTouchOutside(false);
    }

    public <T extends View> T bindView(int id){
        T view = (T) mViews.get(id);
        if(view == null){
            view = rootView.findViewById(id);
            this.mViews.put(id, view);
        }
        return view;
    }

    public <T extends View> T get(int id){
        return this.bindView(id);
    }

    public void setOnClickListener(View.OnClickListener listener, int... ids){
        if(ids == null){
            return;
        }
        for(int id : ids){
            this.get(id).setOnClickListener(listener);
        }
    }

    public void snakebar(String message, Prompt prompt){
        TSnackbar snackBar = TSnackbar.make(this.getActivity().findViewById(android.R.id.content), message, TSnackbar.LENGTH_LONG);
        snackBar.setAction(null, null);
        snackBar.setPromptThemBackground(prompt);
        //snackBar.addIconProgressLoading(0,true,false);
        snackBar.show();
    }

    public void showLoading(){
        if(this.progressDialog != null && !this.progressDialog.isShowing()){
            this.progressDialog.show();
        }
    }

    public void dismissLoading(){
        if(this.progressDialog != null && this.progressDialog.isShowing()){
            this.progressDialog.dismiss();
        }
    }

    public void prompt(String message){
        new AlertDialog.Builder(this.getActivity())
                .setTitle("提示")
                .setMessage(message)
                .setPositiveButton(getActivity().getResources().getString(R.string.ok), null)
                .show();
    }

    public <T extends Activity> T getActivity(){
        return (T) this.rootView.getContext();
    }

    @Override
    public void onDestroyed() {
        this.destroyed = false;
        if(this.progressDialog != null && this.progressDialog.isShowing()){
            this.progressDialog.dismiss();
        }
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void toast(String message){
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
