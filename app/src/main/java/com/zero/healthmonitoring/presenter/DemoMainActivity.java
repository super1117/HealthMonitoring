package com.zero.healthmonitoring.presenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.bde.parentcyTransport.ACSUtility.*;
import com.creative.FingerOximeter.FingerOximeter;
import com.creative.FingerOximeter.IFingerOximeterCallBack;
import com.creative.base.BLEReader;
import com.creative.base.BLESender;
import com.creative.base.BaseDate.*;
import com.creative.bluetooth.ble.BLEOpertion;
import com.creative.bluetooth.ble.IBLECallBack;
import com.zero.healthmonitoring.R;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

/**
 * {@hide}
 */
public class DemoMainActivity extends AppCompatActivity {

    // private WifiAdmin wifiadmin;
    private BLEOpertion ble;
    private FingerOximeter pod;
    private TextView tvMsg;
    private TextView tvWave;
    private TextView tvLog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_demo);

        try {
            ble = new BLEOpertion(this, new BleCallBack());
            tvMsg = (TextView) findViewById(R.id.para);
            tvWave = (TextView) findViewById(R.id.wave);
            tvLog = findViewById(R.id.tv_log);
        } catch (Exception e) {
            e.printStackTrace();
        }

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
//				ble.startDiscover();
                ble.connect("84:EB:18:78:43:3E");
                myHandler.obtainMessage(0, "开始连接").sendToTarget();
            }
        });
        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ble.disConnect();
                myHandler.obtainMessage(0, "连接断开").sendToTarget();
            }
        });

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
