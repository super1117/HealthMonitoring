package com.zero.library.widget;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.view.menu.MenuBuilder;

import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ubzx.library.R;
import com.zero.library.utils.DensityUtils;
import com.zero.library.utils.ImageUtils;

import java.lang.reflect.Field;

import q.rorbin.badgeview.Badge;
import q.rorbin.badgeview.QBadgeView;

/**
 * create by szl on 2017/7/28
 */

public class PBottomNavigationView extends BottomNavigationView {

    private int mShiftAmount;
    private float mScaleUpFactor;
    private float mScaleDownFactor;
    private boolean animationRecord;

    private float mLargeLabelSize;
    private float mSmallLabelSize;

    private BottomNavigationMenuView mMenuView;
    private BottomNavigationItemView[] mButtons;

    private QBadgeView badgeView;

    public PBottomNavigationView(Context context) {
        super(context);
        this.init();
    }

    public PBottomNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public PBottomNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init(){
        this.enableAnimation(false);
        this.enableShiftingMode(false);
        this.enableItemShiftingMode(false);
        this.setCenterIcon();

        //badge
        badgeView = new QBadgeView(this.getContext());
        badgeView.setGravityOffset(12, 3, true);
//        ImageView img = getField(BottomNavigationItemView.class, getBottomNavigationItemView(3), "mIcon");
        badgeView.bindTarget(getBottomNavigationItemView(4));//绑定消息按钮
    }

    public void setMessageBadgeNum(int num){

        badgeView.setBadgeNumber(num);
    }

    public int getMessageBageNum(){
        return badgeView.getBadgeNumber();
    }

    public void setOnDragStateChanged(Badge.OnDragStateChangedListener l){
        badgeView.setOnDragStateChangedListener(l);
    }

    private void enableAnimation(boolean enable) {
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        for (BottomNavigationItemView button : mButtons) {
            TextView mLargeLabel = getField(button.getClass(), button, "mLargeLabel");
            TextView mSmallLabel = getField(button.getClass(), button, "mSmallLabel");
            if (!enable) {
                if (!animationRecord) {
                    animationRecord = true;
                    mShiftAmount = getField(button.getClass(), button, "mShiftAmount");
                    mScaleUpFactor = getField(button.getClass(), button, "mScaleUpFactor");
                    mScaleDownFactor = getField(button.getClass(), button, "mScaleDownFactor");

                    mLargeLabelSize = mLargeLabel.getTextSize();
                    mSmallLabelSize = mSmallLabel.getTextSize();
                }
                setField(button.getClass(), button, "mShiftAmount", 0);
                setField(button.getClass(), button, "mScaleUpFactor", 1);
                setField(button.getClass(), button, "mScaleDownFactor", 1);

                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mSmallLabelSize);
            } else {
                if (!animationRecord)
                    return;
                setField(button.getClass(), button, "mShiftAmount", mShiftAmount);
                setField(button.getClass(), button, "mScaleUpFactor", mScaleUpFactor);
                setField(button.getClass(), button, "mScaleDownFactor", mScaleDownFactor);

                mLargeLabel.setTextSize(TypedValue.COMPLEX_UNIT_PX, mLargeLabelSize);
            }
        }
        mMenuView.updateMenuView();
    }

    private void enableShiftingMode(boolean enable) {
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        setField(mMenuView.getClass(), mMenuView, "mShiftingMode", enable);

        mMenuView.updateMenuView();
    }

    private void enableItemShiftingMode(boolean enable) {
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        BottomNavigationItemView[] mButtons = getBottomNavigationItemViews();
        for (BottomNavigationItemView button : mButtons) {
            setField(button.getClass(), button, "mShiftingMode", enable);
        }
        mMenuView.updateMenuView();
    }

    private void setCenterIcon(){
        BottomNavigationItemView itemView = getBottomNavigationItemView(2);
        itemView.setBackgroundColor(Color.TRANSPARENT);
        itemView.setIconTintList(this.getResources().getColorStateList(R.color.colorAccent));
        setField(BottomNavigationItemView.class, itemView, "mDefaultMargin", DensityUtils.dp2px(getContext(), 6));
        ImageView img = getField(BottomNavigationItemView.class, itemView, "mIcon");
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams)img.getLayoutParams();
        layoutParams.width = DensityUtils.dp2px(getContext(), 48);
        layoutParams.height = DensityUtils.dp2px(getContext(), 48);
        img.setLayoutParams(layoutParams);

        mMenuView.updateMenuView();
    }

    public void setCenterIcon(String url){
        MenuBuilder menuBuilder = getField(BottomNavigationView.class, this, "mMenu");
        menuBuilder.getItem(2).setIcon(null);
        ImageView img = getField(BottomNavigationItemView.class, getBottomNavigationItemView(2), "mIcon");
        ImageUtils.imageCircleLoader(img, url);
        mMenuView.updateMenuView();
    }

    private BottomNavigationMenuView getBottomNavigationMenuView() {
        if (null == mMenuView)
            mMenuView = getField(BottomNavigationView.class, this, "mMenuView");
        return mMenuView;
    }

    private BottomNavigationItemView[] getBottomNavigationItemViews() {
        if (null != mButtons)
            return mButtons;
        BottomNavigationMenuView mMenuView = getBottomNavigationMenuView();
        mButtons = getField(mMenuView.getClass(), mMenuView, "mButtons");
        return mButtons;
    }

    private BottomNavigationItemView getBottomNavigationItemView(int position) {
        return getBottomNavigationItemViews()[position];
    }

    private <T> T getField(Class targetClass, Object instance, String fieldName) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            return (T) field.get(instance);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void setField(Class targetClass, Object instance, String fieldName, Object value) {
        try {
            Field field = targetClass.getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(instance, value);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setBadgeViewGone(){
        if(badgeView.getBadgeNumber()>0){
            badgeView.setBadgeNumber(0);
        }
    }
}
