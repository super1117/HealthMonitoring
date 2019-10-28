package com.zero.library.widget.viewpager.bgmove;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;

import androidx.viewpager.widget.ViewPager;

public class BgMoveViewPager extends ViewPager {

    private Bitmap bg;

    private Paint b = new Paint(Paint.ANTI_ALIAS_FLAG);

    public BgMoveViewPager(Context context) {
        super(context);
    }

    public BgMoveViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setBackGround(Bitmap background){
        this.bg = background;
        this.b.setFilterBitmap(true);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(this.bg != null){
            int width = this.bg.getWidth();
            int height = this.bg.getHeight();
            int count = getAdapter().getCount();
            int x = getScrollX();
            int n = height * getWidth() / getHeight();
            int w = (x + getWidth()) * n / count / getWidth();//(n - getWidth()) / (count - 1) / getWidth();

            canvas.drawBitmap(this.bg, new Rect(w, 0, n + w, getHeight()), new Rect(x, 0, x + getWidth(), getHeight()), this.b);
        }
        super.dispatchDraw(canvas);
    }
}
