package com.zero.healthmonitoring.presenter;

import android.bluetooth.BluetoothAdapter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.widget.SeekBar;

import com.bde.parentcyTransport.ACSUtility;
import com.creative.FingerOximeter.FingerOximeter;
import com.creative.FingerOximeter.IFingerOximeterCallBack;
import com.creative.base.BLEReader;
import com.creative.base.BLESender;
import com.creative.base.BaseDate;
import com.creative.bluetooth.ble.BLEOpertion;
import com.creative.bluetooth.ble.IBLECallBack;
import com.zero.healthmonitoring.R;
import com.zero.healthmonitoring.api.RxHelper;
import com.zero.healthmonitoring.api.SystemApi;
import com.zero.healthmonitoring.base.BaseFragmentPresenter;
import com.zero.healthmonitoring.data.UserBean;
import com.zero.healthmonitoring.delegate.SpoDelegate;
import com.zero.library.network.RxSubscribe;
import com.zero.library.utils.GsonUtil;
import com.zero.library.utils.PermissionManager;
import com.zero.library.widget.DrawableView;
import com.zero.library.widget.snakebar.Prompt;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class SpoJavaPresenter extends BaseFragmentPresenter<SpoDelegate> {

    private final static int PERMISSION_RESULT_CODE = 0x100;

    private final static long TEST_TOTAL_TIME = 5500;

    private long currentTime = 0;

    private BluetoothAdapter bluetoothAdapter;

    private PermissionManager permissionManager;

    private BLEOpertion ble;

    private FingerOximeter pod;

    @Override
    public void doMain() {
        this.viewDelegate.getToolbar().setTitle("血氧测试");
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.permissionManager = new PermissionManager(this.getActivity(), this);
        if(!this.permissionManager.isRequestPermissions(PERMISSION_RESULT_CODE,
                "android.permission.BLUETOOTH",
                "android.permission.BLUETOOTH_ADMIN")){
            this.openBluetooth();
        }
    }

    private void openBluetooth(){
        if(this.bluetoothAdapter != null && !this.bluetoothAdapter.isEnabled()){
            this.bluetoothAdapter.enable();
        }
        this.viewDelegate.getRootView().postDelayed(() -> {
           if(bluetoothAdapter.isEnabled()){
               try{
                   ble = new BLEOpertion(getActivity(), new BleCallBack());
                   myHandler.obtainMessage(0, "正在搜索设备...").sendToTarget();
                   myHandler.sendEmptyMessageDelayed(2, 3000);
               }catch (Exception e){
                   e.printStackTrace();
               }
           }
        }, 1000);
    }

    private Handler myHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            if(viewDelegate == null){
                return;
            }
            switch (msg.what){
                case 0:
                    if(TextUtils.equals(msg.obj.toString(), "connected")){
                        currentTime = System.currentTimeMillis();
                        viewDelegate.getTvStatus().setText("设备已连接");
                        viewDelegate.getTvStatus().setDrawableResource(R.drawable.ic_check_circle_24dp, DrawableView.DrawablePosition.RIGHT);
                        myHandler.sendEmptyMessage(4);
                    }else if(TextUtils.equals(msg.obj.toString(), "disconnect")){
                        viewDelegate.getTvStatus().setText("设备已断开");
                        viewDelegate.getTvStatus().clearDrawable();
                    }else{
                        viewDelegate.getTvStatus().setText(msg.obj.toString());
                        viewDelegate.getTvStatus().clearDrawable();
                    }
                    break;
                case 1:
//                    List<BaseDate.Wave> waves = (List<BaseDate.Wave>) msg.obj;
//                    String showText = "wave=";
//                    for (Iterator<BaseDate.Wave> iterator = waves.iterator(); iterator.hasNext();) {
//                        BaseDate.Wave w = iterator.next();
//                        showText += w.data + "---";
//                    }
//                    viewDelegate.getWave().setText(showText);
                    break;
                case 2:
//                    ble.startDiscover();
                    ble.connect("84:EB:18:78:43:3E");
                    myHandler.obtainMessage(0, "开始连接").sendToTarget();
                    break;
                case 3:
                    String[] data = msg.obj.toString().split(" ");
                    viewDelegate.getTvSpo2().setText(data[0]);
                    viewDelegate.getTvBpm().setText(data[1]);
                    break;
                case 4:
                    int curProgress = msg.obj == null ? 0 : Integer.parseInt(msg.obj.toString());
                    if(Build.VERSION.SDK_INT >= 24){
                        viewDelegate.getSpoSeek().setProgress(curProgress > 100 ? 100 : curProgress, true);
                    }else{
                        viewDelegate.getSpoSeek().setProgress(curProgress > 100 ? 100 : curProgress);
                    }
                    viewDelegate.getTvSeek().setText(viewDelegate.getSpoSeek().getProgress() + "%");
                    long cur = System.currentTimeMillis();
                    if(curProgress < 100){
                        Message message = new Message();
                        message.what = 4;
                        message.obj = (cur - currentTime) * 100 / TEST_TOTAL_TIME;
                        myHandler.sendMessageDelayed(message, 50);
                    }else if(curProgress == 100){
                        myHandler.obtainMessage(0, "测试完成").sendToTarget();
                        if(ble != null){
                            ble.disConnect();
                        }
                        addInfo(viewDelegate.getTvSpo2().getText().toString(), viewDelegate.getTvBpm().getText().toString());
                    }
                    break;
                default:break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(ble != null){
            ble.disConnect();
        }
    }

    private class BleCallBack implements IBLECallBack{

        @Override
        public void onFindDevice(ACSUtility.blePort blePort) {
//            viewDelegate.getTvLog().setText("onFindDevice" + blePort._device.getAddress() + " " + blePort._device.getName() + " " + blePort.devInfo + " \n");
            if(TextUtils.equals(blePort._device.getName(), "POD")){
                ble.stopDiscover();
                new Thread(){
                    @Override
                    public void run() {
                        super.run();
                        ble.connect(blePort);
                    }
                }.start();
            }
        }

        @Override
        public void onDiscoveryCompleted(List<ACSUtility.blePort> device) {
//            viewDelegate.getTvLog().append("onDiscoveryCompleted: \n");
//            viewDelegate.getTvLog().append("***************************************************\n");
//            if(device != null && device.size() > 0){
//                for (ACSUtility.blePort d : device){
//                    viewDelegate.getTvLog().append("* "+ d.devInfo +"\n");
//                    viewDelegate.getTvLog().append("* " + GsonUtil.setBeanToJson(d._device) + " \n");
//                }
//            }
//            viewDelegate.getTvLog().append("***************************************************\n");
        }

        @Override
        public void onConnected(ACSUtility.blePort blePort) {
//            viewDelegate.getTvLog().append("onConnected \n" + blePort.devInfo + " \n");
//            viewDelegate.getTvLog().append("***************************************************\n");
//            viewDelegate.getTvLog().append(GsonUtil.setBeanToJson(blePort._device) + " \n");
//            viewDelegate.getTvLog().append("***************************************************\n");
            if(myHandler != null){
                myHandler.obtainMessage(0, "connected").sendToTarget();
                pod = new FingerOximeter(new BLEReader(ble), new BLESender(ble), new FingerOximeterCallBack());
                pod.Start();
                pod.SetWaveAction(true);
            }
        }

        @Override
        public void onConnectFail() {
            if(myHandler != null){
                myHandler.obtainMessage(0, "连接失败").sendToTarget();
            }
            if (pod != null)
                pod.Stop();
            pod = null;
        }

        @Override
        public void onSended(boolean b) {

        }

        @Override
        public void onDisConnect(ACSUtility.blePort blePort) {
            if(myHandler != null){
                myHandler.obtainMessage(0, "disconnect").sendToTarget();
                pod.Stop();
                pod = null;
            }
        }

        @Override
        public void onReadyForUse() {
//            viewDelegate.getTvLog().append("onReadyForUse \n");
        }
    }

    private class FingerOximeterCallBack implements IFingerOximeterCallBack{

        @Override
        public void OnGetSpO2Param(int nSpO2, int nPR, float nPI, boolean nStatus, int nMode, float nPower) {
//            myHandler.obtainMessage(0, "接收到参数--" + nSpO2 + " " + nPR + " " + nPI).sendToTarget();
            if(myHandler != null){
                myHandler.obtainMessage(3, nSpO2 + " " + nPR).sendToTarget();
            }
        }

        @Override
        public void OnGetSpO2Wave(List<BaseDate.Wave> wave) {
            if(myHandler != null){
                myHandler.obtainMessage(1, wave).sendToTarget();
            }
        }

        @Override
        public void OnGetDeviceVer(int i, int i1, int i2, int i3) {

        }

        @Override
        public void OnConnectLose() {

        }
    }

    private void addInfo(String spo, String bpm){
        if(this.getUser() == null){
            readyGo(LoginPresenter.class);
            getActivity().finish();
            return;
        }
        Map<String, String> param = new HashMap<>();
        param.put("uid", this.getUser().getUid());
        param.put("spo", spo);
        param.put("bpm", bpm);
        SystemApi.provideService()
                .addInfo(param)
                .compose(RxHelper.applySchedulers())
                .subscribe(new RxSubscribe<String>(this.viewDelegate, true) {
                    @Override
                    protected void _onNext(String t) {
                        viewDelegate.snakebar("已提交", Prompt.SUCCESS);
                    }

                    @Override
                    protected void _onError(String message) {

                    }
                });
    }
}
