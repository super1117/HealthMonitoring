package com.zero.healthmonitoring.presenter;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.bde.parentcyTransport.ACSUtility.*;
import com.creative.FingerOximeter.FingerOximeter;
import com.creative.FingerOximeter.IFingerOximeterCallBack;
import com.creative.base.BLEReader;
import com.creative.base.BLESender;
import com.creative.base.BaseDate.*;
import com.creative.bluetooth.ble.BLEOpertion;
import com.creative.bluetooth.ble.IBLECallBack;
import com.zero.healthmonitoring.R;
import com.zero.library.utils.GsonUtil;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class DemoMainActivity extends AppCompatActivity {

    // private WifiAdmin wifiadmin;
    private BLEOpertion ble;
    private FingerOximeter pod;
    private TextView tvMsg;
    private TextView tvWave;
    private TextView tvLog;

    private BluetoothAdapter mBluetoothAdapter;
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub

            String action = intent.getAction();
            // 获得已经搜索到的蓝牙设备
            if (action.equals(BluetoothDevice.ACTION_FOUND)) {
                Log.e("aiya", "找到设备");
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.e("aiya", device.getName() + "   :   " + device.getAddress());
                // 搜索到的不是已经绑定的蓝牙设备
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    // 显示在TextView上
                    tvLog.append(device.getName() + "   :   " + device.getAddress()+"\n");
                }
                // 搜索完成
            } else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)) {
                setProgressBarIndeterminateVisibility(false);
                setTitle("搜索蓝牙设备");
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
        setContentView(R.layout.activity_demo);

        try {
//            ble = new BLEOpertion(this, new BleCallBack());
            tvMsg = (TextView) findViewById(R.id.para);
            tvWave = (TextView) findViewById(R.id.wave);
            tvLog = findViewById(R.id.tv_log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                v.setEnabled(false);
//				ble.startDiscover();
//                ble.connect("84:EB:18:78:43:3E");
//                myHandler.obtainMessage(0, "开始连接").sendToTarget();

                setProgressBarIndeterminateVisibility(true);
                setTitle("正在扫描....");
                // 如果正在搜索，就先取消搜索
                if (mBluetoothAdapter.isDiscovering()) {
                    mBluetoothAdapter.cancelDiscovery();
                }
                // 开始搜索蓝牙设备,搜索到的蓝牙设备通过广播返回
                mBluetoothAdapter.startDiscovery();
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                findViewById(R.id.button2).setEnabled(true);
//                ble.disConnect();
//                myHandler.obtainMessage(0, "连接断开").sendToTarget();
                mBluetoothAdapter.cancelDiscovery();
            }
        });
////////////////////////////////////////////////////////////////////////////////////////////////////
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        // 获取所有已经绑定的蓝牙设备
        Set<BluetoothDevice> devices = mBluetoothAdapter.getBondedDevices();
        if (devices.size() > 0) {
            for (BluetoothDevice bluetoothDevice : devices) {
                tvLog.append(bluetoothDevice.getName() + "   :   "
                        + bluetoothDevice.getAddress() + "\n\n");
            }
        }
        // 注册用以接收到已搜索到的蓝牙设备的receiver
        IntentFilter mFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        registerReceiver(mReceiver, mFilter);
        // 注册搜索完时的receiver
        mFilter = new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(mReceiver, mFilter);
        requestPermission();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        //解除注册
        unregisterReceiver(mReceiver);
    }

    private void requestPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            int checkAccessFinePermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
            if (checkAccessFinePermission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0x110);
                Log.e(getPackageName(), "没有权限，请求权限");
                return;
            }
            Log.e(getPackageName(), "已有定位权限");
            //这里可以开始搜索操作
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0x110: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.e(getPackageName(), "开启权限permission granted!");
                    //这里可以开始搜索操作
                } else {
                    Log.e(getPackageName(), "没有定位权限，请先开启!");
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private Handler myHandler = new Handler() {

        @SuppressWarnings("unchecked")
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0: {
                    tvMsg.setText((String) msg.obj);
                    tvLog.append(msg.obj.toString() + " \n");
                }
                break;
                case 1: {
                    List<Wave> wave = (List<Wave>) msg.obj;
                    String showText = "wave=";
                    for (Iterator<Wave> iterator = wave.iterator(); iterator
                            .hasNext();) {
                        Wave w = iterator.next();
                        showText += w.data + "---";
                    }
                    tvWave.setText(showText);
                    tvLog.append(showText + " \n");
                }
                break;
            }
        }
    };

    class BleCallBack implements IBLECallBack {

        @Override
        public void onFindDevice(final blePort port) {
            tvLog.append("onFindDevice " + port._device.getAddress()
                    + "  " + port._device.getName() + " " + port.devInfo +" \n");
            if (port._device.getName().trim().equals("POD")) {// 将POD修改为对应的设备名即可
                ble.stopDiscover();
                new Thread() {

                    @Override
                    public void run() {
                        super.run();
                        ble.connect(port);
                    }
                }.start();
            }
        }

        @Override
        public void onConnected(blePort port) {
            tvLog.append("onConnected \n");
            pod = new FingerOximeter(new BLEReader(ble), new BLESender(ble),
                    new FingerOximeterCallBack());
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
        public void onSended(boolean isSend) {

        }

        @Override
        public void onDiscoveryCompleted(List<blePort> device) {
            tvLog.append("onDiscoveryCompleted \n");
            if(device != null && device.size() > 0){
                tvLog.append(GsonUtil.setBeanToJson(device));
            }
        }

        @Override
        public void onDisConnect(blePort prot) {
            myHandler.obtainMessage(0, "连接断开").sendToTarget();
            pod.Stop();
            pod = null;
        }

        @Override
        public void onReadyForUse() {
            tvLog.append("onReadyForUse \n");
        }

    }

    class FingerOximeterCallBack implements IFingerOximeterCallBack {

        @Override
        public void OnGetSpO2Param(int nSpO2, int nPR, float nPI,
                                   boolean nStatus, int nMode, float nPower) {
            myHandler.obtainMessage(0,
                    "接收到参数--" + nSpO2 + " " + nPR + " " + nPI).sendToTarget();
        }

        @Override
        public void OnGetSpO2Wave(List<Wave> wave) {
            myHandler.obtainMessage(1, wave).sendToTarget();
        }

        @Override
        public void OnGetDeviceVer(int nHWMajor, int nHWMinor, int nSWMajor,
                                   int nSWMinor) {

        }

        @Override
        public void OnConnectLose() {

        }
    }

}
