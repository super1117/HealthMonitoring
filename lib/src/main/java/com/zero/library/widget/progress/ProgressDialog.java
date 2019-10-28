package com.zero.library.widget.progress;
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
//         佛祖保佑       永无BUG     永不修改                       //
////////////////////////////////////////////////////////////////////

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.ubzx.library.R;
import com.zero.library.utils.DensityUtils;

/**
 * create by szl on 2017/8/24
 */

public class ProgressDialog {

    private Context context;

    private static final int CIRCLE_BG_LIGHT = 0xFFFAFAFA;

    private static final int MAX_ALPHA = 255;

    private CircleImageView mCircleView;

    private MaterialProgressDrawable mProgress;

    private RelativeLayout parent;

    private Dialog dialog;

    public ProgressDialog(Context context){
        this.context = context;
    }

    public ProgressDialog builder(){
        View view = LayoutInflater.from(this.context).inflate(R.layout.view_progress_dialog, null);
        view.setMinimumWidth(DensityUtils.dp2px(this.context, 100f));
        view.setMinimumHeight(DensityUtils.dp2px(this.context, 100f));
        this.parent = view.findViewById(R.id.pro_lay);
        dialog = new Dialog(context, R.style.ActionSheetDialogStyle);
        dialog.setContentView(view);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.CENTER);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        lp.x = 0;
        lp.y = 0;
        dialogWindow.setAttributes(lp);
        return this;
    }

    public ProgressDialog setCancelable(boolean cancelable){
        this.dialog.setCancelable(cancelable);
        return this;
    }

    public ProgressDialog setCanceledOnTouchOutside(boolean cancel){
        this.dialog.setCanceledOnTouchOutside(cancel);
        return this;
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener listener){
//        this.stopProgress();
        this.dialog.setOnCancelListener(listener);
    }

    public ProgressDialog show(){
        this.createProgressView(this.parent);
        this.dialog.show();
        return this;
    }

    public ProgressDialog dismiss(){
        this.dialog.dismiss();
        this.stopProgress();
        return this;
    }

    private void createProgressView(ViewGroup parent) {
        mCircleView = new CircleImageView(this.context);//, CIRCLE_BG_LIGHT);//, CIRCLE_DIAMETER / 2
        mProgress = new MaterialProgressDrawable(this.context, parent);
        mProgress.setBackgroundColor(CIRCLE_BG_LIGHT);
        mCircleView.setImageDrawable(mProgress);
        mCircleView.setVisibility(View.VISIBLE);
        parent.addView(mCircleView);
        mProgress.setAlpha(MAX_ALPHA);
        mProgress.start();
    }

    private void setColorViewAlpha(int targetAlpha) {
        if(mCircleView != null){
            mCircleView.getBackground().setAlpha(targetAlpha);
        }
        if(mProgress != null){
            mProgress.setAlpha(targetAlpha);
        }
    }

    private void stopProgress(){
        if(mProgress != null){
            mProgress.stop();
        }
        if(mCircleView != null){
            mCircleView.setVisibility(View.GONE);
            setColorViewAlpha(MAX_ALPHA);
            mCircleView.setImageDrawable(null);
        }
        mCircleView = null;
        mProgress = null;
    }

    public boolean isShowing(){
        return dialog.isShowing();
    }

}
