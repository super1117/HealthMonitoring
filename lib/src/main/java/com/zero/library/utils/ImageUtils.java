package com.zero.library.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.core.graphics.drawable.DrawableCompat;
import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.zero.library.Library;
import com.ubzx.library.R;

import java.io.File;
import java.util.concurrent.ExecutionException;

import jp.wasabeef.glide.transformations.BlurTransformation;
import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

import static com.bumptech.glide.request.RequestOptions.bitmapTransform;

/**
 * create by szl on 2017/8/14
 */

public class ImageUtils {

    private ImageUtils() {
        /* cannot be instantiated */
        throw new UnsupportedOperationException("cannot be instantiated");
    }

    /**
     * 以高斯模糊显示。
     *
     * @param draweeView View。
     * @param url        url.
     * @param blurRadius 模糊图半径，必须大于0，越大越模糊。
     */
    public static void showUrlBlur(Context context, ImageView draweeView, String url, int blurRadius) {
        Glide.with(context)
                .load(url)
                .apply(bitmapTransform(new BlurTransformation(15)))
                .into(draweeView);
    }

    /**
     * 低版本设置 Vector
     * @param draw
     * @param tint
     */
    public static void setDrawableTint(Drawable draw, int tint){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Drawable wrappedDrawable = DrawableCompat.wrap(draw);
            DrawableCompat.setTint(wrappedDrawable, tint);
        }
    }

    /**
     * 获取图片宽高
     * @param context
     * @param src
     * @return
     */
    public static int[] getSrcSize(Context context, int src){
        Bitmap mBitmap = BitmapFactory.decodeResource(context.getResources(), src);
        int[] size = {mBitmap.getWidth(), mBitmap.getHeight()};
        return size;
    }

    /**
     * 获取SD卡图片的尺寸
     * @param path
     * @return
     */
    public static int[] getSdpicSize(String path){
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(path, options); // 此时返回的bitmap为null
        int[] size = {options.outWidth, options.outHeight};
        return size;
    }

    /**
     * glide加载网络图
     * @param img
     * @param url
     */
    @BindingAdapter({"image_url"})
    public static void imageLoader(ImageView img, String url){
        if(!TextUtils.isEmpty(url)){
            Glide.with(Library.CONTEXT)
                    .load(url)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(img);
        }
    }

    public static void imageLoader(ImageView img, String url, int size){
        if(!TextUtils.isEmpty(url)){
            RequestOptions options = new RequestOptions();
            options.placeholder(R.mipmap.bgd);
            options.override(size, size);
            Glide.with(Library.CONTEXT)
                    .load(url)
                    .apply(options)
//                    .thumbnail(0.2f)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .into(img);
        }
    }

    public static void imageRoundLoader(ImageView img, String url, RoundedCornersTransformation.CornerType type){
        MultiTransformation multi = new MultiTransformation(new CenterCrop(),
                    type == null ? new RoundedCornersTransformation(0, 0, RoundedCornersTransformation.CornerType.ALL) : new RoundedCornersTransformation(10, 0, type));
        Glide.with(Library.CONTEXT)
                .load(url)
                .apply(bitmapTransform(multi))
                .transition(new DrawableTransitionOptions().crossFade(500))
                .into(img);
    }

    public static void imageLoaderNone(ImageView img, String url){
        MultiTransformation multi = new MultiTransformation( new RoundedCornersTransformation(10, 0, RoundedCornersTransformation.CornerType.ALL));
        Glide.with(Library.CONTEXT)
                .load(url)
                .apply(bitmapTransform(multi))
                .transition(new DrawableTransitionOptions().crossFade(500))
                .into(img);
    }

    /**
     * 圆形图片加载
     * @param img
     * @param url
     */
    @BindingAdapter({"circle_image_url"})
    public static void imageCircleLoader(ImageView img, String url){
        if(!TextUtils.isEmpty(url)){
            Glide.with(Library.CONTEXT)
                    .load(url)
                    .transition(new DrawableTransitionOptions().crossFade())
                    .apply(bitmapTransform(new CircleCrop()))
                    .into(img);
        }
    }

    /**
     * 将图片缓存到本地
     * @param imgUrl
     * @return
     */
    public static String getImagePath(String imgUrl) {
        String path = null;
        FutureTarget<File> future = Glide.with(Library.CONTEXT)
                .load(imgUrl)
                .downloadOnly(Target.SIZE_ORIGINAL,Target.SIZE_ORIGINAL);
        try {
            File cacheFile = future.get();
            path = cacheFile.getAbsolutePath();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return path;
    }
}
