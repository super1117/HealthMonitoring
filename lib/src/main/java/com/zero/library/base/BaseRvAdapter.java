package com.zero.library.base;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;

import com.ubzx.library.R;
import com.zero.library.listener.*;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseRvAdapter<T> extends RecyclerView.Adapter<BaseViewHolder> {

    private RecyclerView recyclerView;

    protected Context mContext;

    protected List<T> mData;

    private LayoutInflater inflater;

    private boolean showFooterView = true;

    private boolean needLoadMore = true;

    private final static int FOOTER = R.layout.view_footer;

    private OnItemChildCheckChangeListener childCheckChangeListener;

    private OnItemChildClickListener childClickListener;

    private OnItemLongClickListener longClickListener;

    private OnItemClickListener clickListener;

    private OnLoadListener loadListener;

    public BaseRvAdapter(RecyclerView recyclerView){
        this.recyclerView = recyclerView;
        this.mContext = this.recyclerView.getContext();
        this.mData = new ArrayList<>();
        this.inflater = LayoutInflater.from(this.mContext);
    }

    @Override
    public int getItemCount() {
        return showFooterView ? this.mData.size() + 1 : this.mData.size();//(this.mData.size() > 0 ? this.mData.size() + 1 : this.mData.size())
    }

    @CallSuper
    @Override
    public int getItemViewType(int position) {
        return showFooterView && position == (getItemCount() - 1) ? FOOTER : onLayoutRes(position);
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(viewType, parent, false);
        BaseViewHolder holder = new BaseViewHolder(this.recyclerView, view);
        if(showFooterView && viewType == FOOTER) return holder;
        holder.setOnItemChildCheckChangeListener(this.childCheckChangeListener);
        holder.setOnItemChildClickListener(this.childClickListener);
        holder.setOnItemClickListener(this.clickListener);
        holder.setOnItemLongClickListener(this.longClickListener);
        this.setItemChildListener(holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        if(this.showFooterView && position == (getItemCount() - 1)){
            if(this.loadListener != null && this.needLoadMore && position >= 20){
                this.loadListener.onLoad();
            }
        } else {
            bindData(holder, position, getItemData(position));
        }
    }

    /**
     * 设置子控件的点击事件
     * @param holder
     * @param viewType
     */
    protected void setItemChildListener(BaseViewHolder holder, int viewType) {}

    /**
     * list item layout
     * @param position
     * @return
     */
    protected abstract int onLayoutRes(int position);

    /**
     * 给item绑定数据
     * @param holder
     * @param position
     * @param data
     */
    protected abstract void bindData(BaseViewHolder holder, int position, T data);

    /**
     * 设置列表数据
     * @param data
     */
    public void setData(List<T> data){
        this.mData.clear();
        this.mData.addAll(data);
        this.notifyDataSetChanged();
    }

    /**
     * 获取列表数据
     * @return
     */
    public List<T> getData(){
        return this.mData;
    }

    /**
     * 设置是否展示加载更多布局
     * @param showFooterView
     */
    public void setShowFooterView(boolean showFooterView){
        this.showFooterView = showFooterView;
    }

    public void setNeedLoadMore(boolean isNeed){
        this.needLoadMore = isNeed;
    }

    /**
     * 获取指定位置的数据
     * @param position
     * @return
     */
    public T getItemData(int position){
        return this.mData.get(position);
    }

    /**
     * item点击事件
     * @param clickListener
     */
    public void setOnItemClickListener(OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    /**
     * item 长按事件
     * @param longClickListener
     */
    public void setOnItemLongClickListener(OnItemLongClickListener longClickListener){
        this.longClickListener = longClickListener;
    }

    /**
     * item子控件点击事件
     * @param childClickListener
     */
    public void setOnItemChildClickListener(OnItemChildClickListener childClickListener){
        this.childClickListener = childClickListener;
    }

    /**
     * item子控件check事件
     * @param childChangeListener
     */
    public void setOnItemChildCheckChangeListener(OnItemChildCheckChangeListener childChangeListener){
        this.childCheckChangeListener = childChangeListener;
    }

    /**
     * 加载更多监听
     * @param listener
     */
    public void setOnLoadListener(OnLoadListener listener){
        this.loadListener = listener;
    }
}