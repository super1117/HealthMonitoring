package com.zero.library.network;
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

import android.util.Log;
import com.ubzx.library.R;
import com.zero.library.mvp.view.AppDelegate;
import com.zero.library.utils.SystemUtils;
import com.zero.library.widget.progress.ProgressDialog;
import com.zero.library.widget.snakebar.Prompt;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

/**
 * create by szl on 2017/8/23
 */
//实现Observer类，把里面用不到的方法这里全处理掉 我们只处理结果 成功/失败
public abstract class RxSubscribe<T> implements Observer<T> {
    private AppDelegate delegate;
    private ProgressDialog dialog;
    private boolean isShow;

    public RxSubscribe(AppDelegate delegate, boolean showLoading) {
        this.delegate = delegate;
        this.isShow = showLoading;
    }

    @Override
    public void onComplete() {
        if (isShow) {
            if (dialog != null)
                dialog.dismiss();
        }
    }

    @Override
    public void onSubscribe(@NonNull final Disposable d) {
        if (isShow && this.delegate != null && !this.delegate.isDestroyed()) {
            dialog = new ProgressDialog(delegate.getActivity()).builder();
            dialog.setCanceledOnTouchOutside(false);
            dialog.setOnCancelListener(dialog1 -> {
                if (!d.isDisposed()) {
                    d.dispose();
                }
            });
            dialog.show();

        }
    }

    @Override
    public void onNext(T result) {
        if(this.delegate != null && !this.delegate.isDestroyed()){
            if(result == null){
                this.delegate.snakebar("请求失败", Prompt.ERROR);
                _onError("result DATA is null");
            }else{
                _onNext(result);
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        Log.i("onError",e.getMessage()+"" );
        //把底层的一些错误翻译一下
        String errMsg;
        if (e == null || e.getMessage() == null || e.getMessage().isEmpty()) {//未知错误
            errMsg = this.delegate.getActivity().getString(R.string.error_unknown);
        } else if (e.getMessage().contains("Failed to connect to") ||
                e.getMessage().contains("failed to connect to") ||
                e.getMessage().contains("Unable to resolve host")) {//连接服务器失败
            if (!SystemUtils.isNetworkAvailable(this.delegate.getActivity())) {//没网络
                errMsg = this.delegate.getActivity().getString(R.string.error_bad_network);
            } else {//真的没连接上服务器
                errMsg = this.delegate.getActivity().getString(R.string.error_connect_server);
            }
        }
        else if(e.getMessage().contains("onNext called with null")){
            errMsg = "data is null!"; //mContext.getString(R.string.error_no_more);
        }
        else if (e.getMessage().contains("timed out") || e.getMessage().contains("timeout")) {
            //超时了
            errMsg = this.delegate.getActivity().getString(R.string.error_connect_server_timeout);
        }else if (e.getMessage().contains("500")) {
            //服务器内部错误
            errMsg = this.delegate.getActivity().getString(R.string.error_connect_server_500);
        } else {
            errMsg = e.getMessage();
        }
        if (isShow) {
            if (dialog != null)
                dialog.dismiss();
        }
        if(this.delegate != null && !this.delegate.isDestroyed()){
            this.delegate.snakebar(errMsg, Prompt.ERROR);
            _onError(errMsg);
        }
    }

    //成功
    protected abstract void _onNext(T t);
    //失败
    protected abstract void _onError(String message);
}