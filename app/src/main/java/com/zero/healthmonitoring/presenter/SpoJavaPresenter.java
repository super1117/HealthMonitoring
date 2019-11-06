package com.zero.healthmonitoring.presenter;

import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;

import com.bde.parentcyTransport.ACSUtility;
import com.creative.FingerOximeter.FingerOximeter;
import com.creative.FingerOximeter.IFingerOximeterCallBack;
import com.creative.base.BLEReader;
import com.creative.base.BLESender;
import com.creative.base.BaseDate;
import com.creative.bluetooth.ble.BLEOpertion;
import com.creative.bluetooth.ble.IBLECallBack;
import com.zero.healthmonitoring.base.BaseFragmentPresenter;
import com.zero.healthmonitoring.delegate.SpoDelegate;
import com.zero.library.utils.GsonUtil;
import com.zero.library.utils.PermissionManager;

import java.util.Iterator;
import java.util.List;

public class SpoJavaPresenter extends BaseFragmentPresenter<SpoDelegate> {

    private final static int PERMISSION_RESULT_CODE = 0x100;

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
                   viewDelegate.getPara().setText("正在搜索设备...");
                   viewDelegate.getWave().setText(".................");
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
            switch (msg.what){
                case 0:
                    viewDelegate.getPara().setText(msg.obj.toString());
                    viewDelegate.getTvLog().append(msg.obj.toString() + "\n");
                    break;
                case 1:
                    List<BaseDate.Wave> waves = (List<BaseDate.Wave>) msg.obj;
                    String showText = "wave=";
                    for (Iterator<BaseDate.Wave> iterator = waves.iterator(); iterator.hasNext();) {
                        BaseDate.Wave w = iterator.next();
                        showText += w.data + "---";
                    }
                    viewDelegate.getWave().setText(showText);
                    break;
                case 2:
//                    ble.startDiscover();
                    ble.connect("84:EB:18:78:43:3E");
                    myHandler.obtainMessage(0, "开始连接").sendToTarget();
                    break;

            }
        }
    };

    private class BleCallBack implements IBLECallBack{

        @Override
        public void onFindDevice(ACSUtility.blePort blePort) {
            viewDelegate.getTvLog().setText("onFindDevice" + blePort._device.getAddress() + " " + blePort._device.getName() + " " + blePort.devInfo + " \n");
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
            viewDelegate.getTvLog().append("onDiscoveryCompleted: \n");
            viewDelegate.getTvLog().append("***************************************************\n");
            if(device != null && device.size() > 0){
                for (ACSUtility.blePort d : device){
                    viewDelegate.getTvLog().append("* "+ d.devInfo +"\n");
                    viewDelegate.getTvLog().append("* " + GsonUtil.setBeanToJson(d._device) + " \n");
                }
            }
            viewDelegate.getTvLog().append("***************************************************\n");
        }

        @Override
        public void onConnected(ACSUtility.blePort blePort) {
            viewDelegate.getTvLog().append("onConnected \n" + blePort.devInfo + " \n");
            viewDelegate.getTvLog().append("***************************************************\n");
            viewDelegate.getTvLog().append(GsonUtil.setBeanToJson(blePort._device) + " \n");
            viewDelegate.getTvLog().append("***************************************************\n");
            pod = new FingerOximeter(new BLEReader(ble), new BLESender(ble), new FingerOximeterCallBack());
            pod.Start();
            pod.SetWaveAction(true);
        }

        @Override
        public void onConnectFail() {
            myHandler.obtainMessage(0, "连接断开").sendToTarget();
            if (pod != null)
                pod.Stop();
            pod = null;
        }

        @Override
        public void onSended(boolean b) {

        }

        @Override
        public void onDisConnect(ACSUtility.blePort blePort) {
            myHandler.obtainMessage(0, "连接断开").sendToTarget();
            pod.Stop();
            pod = null;
        }

        @Override
        public void onReadyForUse() {
            viewDelegate.getTvLog().append("onReadyForUse \n");
        }
    }

    private class FingerOximeterCallBack implements IFingerOximeterCallBack{

        @Override
        public void OnGetSpO2Param(int nSpO2, int nPR, float nPI, boolean nStatus, int nMode, float nPower) {
            myHandler.obtainMessage(0, "接收到参数--" + nSpO2 + " " + nPR + " " + nPI).sendToTarget();
        }

        @Override
        public void OnGetSpO2Wave(List<BaseDate.Wave> wave) {
            myHandler.obtainMessage(1, wave).sendToTarget();
        }

        @Override
        public void OnGetDeviceVer(int i, int i1, int i2, int i3) {

        }

        @Override
        public void OnConnectLose() {

        }
    }
}
