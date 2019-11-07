package com.zero.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.ubzx.library.R;
import com.zero.library.utils.DensityUtils;

public class DrawableView extends AppCompatTextView {

    private int drawableSize;

    private int drawableResource;

    public DrawableView(Context context) {
        this(context, null);
    }

    public DrawableView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DrawableView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.drawableview);
        this.drawableSize = (int) ta.getDimension(R.styleable.drawableview_drawableSize, DensityUtils.dp2px(context, 24f));
        Drawable[] drawables = this.getCompoundDrawables();
        this.setCompoundDrawables(drawables[0], drawables[1], drawables[2], drawables[3]);
    }

    @Override
    public void setCompoundDrawables(@Nullable Drawable left, @Nullable Drawable top, @Nullable Drawable right, @Nullable Drawable bottom) {
        if(left != null){
            left.setBounds(0, 0, drawableSize, drawableSize);
        }
        if(top != null){
            top.setBounds(0, 0, drawableSize, drawableSize);
        }
        if(right != null){
            right.setBounds(0, 0, drawableSize, drawableSize);
        }
        if(bottom != null){
            bottom.setBounds(0, 0, drawableSize, drawableSize);
        }
        super.setCompoundDrawables(left, top, right, bottom);
    }

    public void setDrawableSize(int drawableSize){
        this.drawableSize = drawableSize;
    }

    public int getDrawableSize(){
        return this.drawableSize;
    }

    public void setDrawableResource(int res, DrawablePosition drawablePosition){
        this.drawableResource = res;
        Drawable drawable = getContext().getResources().getDrawable(res);
        switch (drawablePosition){
            case LEFT:
                this.setCompoundDrawables(drawable, null, null, null);
                break;
            case TOP:
                this.setCompoundDrawables(null, drawable, null, null);
                break;
            case RIGHT:
                this.setCompoundDrawables(null, null, drawable, null);
                break;
            case BOTTOM:
                this.setCompoundDrawables(null, null, null, drawable);
                break;
            default:break;
        }
    }

    public void clearDrawable(){
        this.setCompoundDrawables(null, null, null, null);
    }

    public int getDrwableResource(){
        return this.drawableResource;
    }

    public enum DrawablePosition{
        LEFT,
        TOP,
        RIGHT,
        BOTTOM
    }
}
