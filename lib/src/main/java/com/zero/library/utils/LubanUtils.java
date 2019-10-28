package com.zero.library.utils;
////////////////////////////////////////////////////////////////////
//                          _ooOoo_                               //
//                         o8888888o                              //
//                         88" . "88                              //
//                         (| ^_^ |)                              //
//                         O\  =  /O                              //
//                      ____/`---'\____                           //
//                    .'  \\|     |//  `.                         //
//                   /  \\|||  :  |||//  \                        //
//                  /  _||||| -:- |||||-  \                       //
//                  |   | \\\  -  /// |   |                       //
//                  | \_|  ''\---/''  |   |                       //
//                  \  .-\__  `-`  ___/-. /                       //
//                ___`. .'  /--.--\  `. . ___                     //
//              ."" '<  `.___\_<|>_/___.'  >'"".                  //
//            | | :  `- \`.;`\ _ /`;.`/ - ` : | |                 //
//            \  \ `-.   \_ __\ /__ _/   .-` /  /                 //
//      ========`-.____`-.___\_____/___.-`____.-'========         //
//                           `=---='                              //
//      ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^        //
//                     佛祖保佑       永无BUG                       //
//                    此代码模块已经经过开光处理                      //
////////////////////////////////////////////////////////////////////

import android.content.Context;
import android.util.Log;

import com.ubzx.library.R;
import com.zero.library.bean.ImageItem;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

public class LubanUtils {

    private String comparessPath;

    private List<ImageItem> imageItemList;

    private OnCompressionListener compressionListener;

    public static Builder with(Context context){
        return new Builder(context);
    }

    private LubanUtils(Builder builder){
        this.comparessPath = SdCard.getExternalAppPath("thumbnail");
        this.imageItemList = builder.imageItems;
        this.compressionListener = builder.listener;
    }

    public static class Builder{

        private Context context;

        private List<ImageItem> imageItems;

        private OnCompressionListener listener;

        Builder(Context context) {
            this.context = context;
            this.imageItems = new ArrayList<>();
        }

        private LubanUtils buid(){
            return new LubanUtils(this);
        }

        public Builder load(List<ImageItem> list){
            this.imageItems.addAll(list);
            return this;
        }

        public Builder setOnCompresssionListener(OnCompressionListener listener){
            this.listener = listener;
            return this;
        }

        public void launch(){
            buid().launch(this.context);
        }

    }

    private void launch(Context context){
        if(!SdCard.getSDState()){
            compressionListener.onError(context.getResources().getString(R.string.sd_erroe));
            return;
        }
        List<String> comparessList = new ArrayList<>();
        for(ImageItem item : imageItemList){
            Log.e("Luban", item.getImagePath());
            comparessList.add(item.getImagePath());
            String[] fileName = item.getImagePath().split("/");
            File cameraOutFile = new File(comparessPath+"/"+fileName[fileName.length-1]);
            try {
                SdCard.deleteFile(cameraOutFile);
            } catch (Exception e) {
                e.printStackTrace();
            }finally {
                cameraOutFile.getParentFile().mkdirs();
            }
        }
        final List<ImageItem> outList = new ArrayList<>();
        Luban.with(context)
                .load(comparessList)
                .ignoreBy(100)
                .setTargetDir(comparessPath)
                .setCompressListener(new OnCompressListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(File file) {
                        ImageItem imageItem = new ImageItem();
                        imageItem.setImagePath(file.getPath());
                        int[] size = ImageUtils.getSdpicSize(file.getPath());
                        imageItem.setWidth(size[0]);
                        imageItem.setHeight(size[1]);
                        outList.add(imageItem);
                        if(outList.size() == imageItemList.size()){
                            compressionListener.onSuccess(outList);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Luban", e.getMessage());
                        compressionListener.onError(e.getMessage());
                    }
                }).launch();

    }

    public interface OnCompressionListener{
        void onSuccess(List<ImageItem> list);
        void onError(String messge);
    }

}
