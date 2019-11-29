package com.zero.healthmonitoring.presenter;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.bde.parentcyTransport.ACSUtility;
import com.bde.parentcyTransport.ACSUtility.blePort;
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
import com.zero.healthmonitoring.delegate.SpoDelegate;
import com.zero.library.network.RxSubscribe;
import com.zero.library.utils.PermissionManager;
import com.zero.library.widget.DrawableView;
import com.zero.library.widget.snakebar.Prompt;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpoJavaPresenter extends BaseFragmentPresenter<SpoDelegate> {

    private final static int PERMISSION_RESULT_CODE = 0x100;

    private final static long TEST_TOTAL_TIME = 11 * 1000;

    private long currentTime = 0;

    private BluetoothAdapter bluetoothAdapter;

    private PermissionManager permissionManager;

    private BLEOpertion ble;

    private FingerOximeter pod;

    private boolean isFindDevice;

    private boolean isConnected = true;

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (TextUtils.equals(device.getName(), "POD")) {
                    isFindDevice = true;
                    if (myHandler != null) {
                        myHandler.obtainMessage(0, "开始连接").sendToTarget();
                    }
                    if (bluetoothAdapter != null) {
                        bluetoothAdapter.cancelDiscovery();
                    }
                    if (ble != null) {
                        new Thread() {
                            @Override
                            public void run() {
                                super.run();
                                ble.connect(device.getAddress());
                            }
                        }.start();
                    }
                }
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                if (!isFindDevice) {
                    myHandler.obtainMessage(0, "未找到可连接设备").sendToTarget();
                }
            }
        }
    };

    @Override
    public void doMain() {
        this.viewDelegate.getToolbar().setTitle("血氧测试");
        this.viewDelegate.getToolbar().inflateMenu(this.viewDelegate.getOptionMenuId());
        this.viewDelegate.getToolbar().getMenu().getItem(0).setTitle(this.getUser().getName());
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.permissionManager = new PermissionManager(this.getActivity(), this);
        if (!this.permissionManager.isRequestPermissions(
                PERMISSION_RESULT_CODE,
                Manifest.permission.BLUETOOTH,
                Manifest.permission.BLUETOOTH_ADMIN,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            this.openBluetooth();
        }
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        getActivity().registerReceiver(mReceiver, mFilter);
        mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        getActivity().registerReceiver(mReceiver, mFilter);
    }

    @Override
    protected void bindEvenListener() {
        super.bindEvenListener();
        viewDelegate.setOnClickListener(this.onClickListener, R.id.tv_status);
    }

    private View.OnClickListener onClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_status:
                    myHandler.obtainMessage(0, "正在搜索设备...").sendToTarget();
                    myHandler.sendEmptyMessageDelayed(2, 3000);
                    break;
                default:
                    break;
            }
        }
    };

    private void openBluetooth() {
        if (this.bluetoothAdapter != null && !this.bluetoothAdapter.isEnabled()) {
            this.bluetoothAdapter.enable();
        }
        this.viewDelegate.getRootView().postDelayed(() -> {
            if (this.bluetoothAdapter != null && bluetoothAdapter.isEnabled()) {
                try {
                    ble = new BLEOpertion(getActivity(), new BleCallBack());
                    myHandler.obtainMessage(0, "正在搜索设备...").sendToTarget();
                    myHandler.sendEmptyMessageDelayed(2, 3000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, 1000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_RESULT_CODE) {
            if (!this.permissionManager.verifyPermissions(grantResults)) {
                this.permissionManager.showMissingPermissionDialog(null);
            } else {
                openBluetooth();
            }
        }
    }

    private Handler myHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (viewDelegate == null) {
                return;
            }
            viewDelegate.getTvStatus().setClickable(false);
            switch (msg.what) {
                case 0:
                    if (TextUtils.equals(msg.obj.toString(), "connected")) {
                        currentTime = System.currentTimeMillis();
                        viewDelegate.getTvStatus().setText("设备已连接");
                        viewDelegate.getTvStatus().setDrawableResource(R.drawable.ic_check_circle_24dp, DrawableView.DrawablePosition.RIGHT);
                        myHandler.sendEmptyMessage(4);
                    } else if (TextUtils.equals(msg.obj.toString(), "disconnect")) {
                        viewDelegate.getTvStatus().setText("设备已断开");
                        viewDelegate.getTvStatus().clearDrawable();
                    } else if (TextUtils.equals(msg.obj.toString(), "success")) {
                        viewDelegate.getTvStatus().setClickable(true);
                        viewDelegate.getTvStatus().setText("测试完成，点击重新测试");
                        viewDelegate.getTvStatus().clearDrawable();
                    } else {
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
//                    ble.connect("84:EB:18:78:43:3E");
                    if (bluetoothAdapter != null) {
                        bluetoothAdapter.startDiscovery();
                    }
                    break;
                case 3:
                    String[] data = msg.obj.toString().split(" ");
                    viewDelegate.getTvSpo2().setText(data[0]);
                    viewDelegate.getTvBpm().setText(data[1]);
                    break;
                case 4:
                    int curProgress = msg.obj == null ? 0 : Integer.parseInt(msg.obj.toString());
                    if (Build.VERSION.SDK_INT >= 24) {
                        viewDelegate.getSpoSeek().setProgress(curProgress > 100 ? 100 : curProgress, true);
                    } else {
                        viewDelegate.getSpoSeek().setProgress(curProgress > 100 ? 100 : curProgress);
                    }
                    viewDelegate.getTvSeek().setText(viewDelegate.getSpoSeek().getProgress() + "%");
                    long cur = System.currentTimeMillis();
                    if (curProgress < 100) {
                        Message message = new Message();
                        message.what = 4;
                        message.obj = (cur - currentTime) * 100 / TEST_TOTAL_TIME;
                        myHandler.sendMessageDelayed(message, isConnected ? 50 : 10);
                    } else if (curProgress == 100) {
                        try{
                            if(Integer.parseInt(viewDelegate.getTvSpo2().getText().toString()) == 0 ||
                                    Integer.parseInt(viewDelegate.getTvBpm().getText().toString()) == 0){
                                return;
                            }
                            addInfo(viewDelegate.getTvSpo2().getText().toString(), viewDelegate.getTvBpm().getText().toString());
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.isConnected = false;
        if (ble != null) {
            ble.disConnect();
        }
        if (mReceiver != null) {
            getActivity().unregisterReceiver(mReceiver);
        }
    }

    private class BleCallBack implements IBLECallBack {

        @Override
        public void onFindDevice(ACSUtility.blePort blePort) {
            myHandler.obtainMessage(0, "开始连接").sendToTarget();
            if (TextUtils.equals(blePort._device.getName(), "POD")) {
                ble.stopDiscover();
                new Thread() {
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

        }

        @Override
        public void onConnected(ACSUtility.blePort blePort) {
            if (myHandler != null) {
                myHandler.obtainMessage(0, "connected").sendToTarget();
                pod = new FingerOximeter(new BLEReader(ble), new BLESender(ble), new FingerOximeterCallBack());
                pod.Start();
                pod.SetWaveAction(true);
            }
            isConnected = true;
        }

        @Override
        public void onConnectFail() {
            isConnected = false;
            if (myHandler != null) {
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
            isConnected = false;
            if (myHandler != null) {
                myHandler.obtainMessage(0, "disconnect").sendToTarget();
                pod.Stop();
                pod = null;
            }
        }

        @Override
        public void onReadyForUse() {

        }
    }

    private class FingerOximeterCallBack implements IFingerOximeterCallBack {

        @Override
        public void OnGetSpO2Param(int nSpO2, int nPR, float nPI, boolean nStatus, int nMode, float nPower) {
            if (myHandler != null) {
                myHandler.obtainMessage(3, nSpO2 + " " + nPR).sendToTarget();
            }
        }

        @Override
        public void OnGetSpO2Wave(List<BaseDate.Wave> wave) {
            if (myHandler != null) {
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

    private void addInfo(String spo, String bpm) {
        if (this.getUser() == null) {
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
                        if(myHandler != null){
                            myHandler.obtainMessage(0, "success").sendToTarget();
                        }
                        if (ble != null) {
                            ble.disConnect();
                        }
                        viewDelegate.snakebar("已提交", Prompt.SUCCESS);
                    }

                    @Override
                    protected void _onError(String message) {
                        if(myHandler != null){
                            myHandler.obtainMessage(0, "success").sendToTarget();
                        }
                        if (ble != null) {
                            ble.disConnect();
                        }
                        viewDelegate.snakebar("提价失败，请重新测试", Prompt.ERROR);
                    }
                });
    }
}
