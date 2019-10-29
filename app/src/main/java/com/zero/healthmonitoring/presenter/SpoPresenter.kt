package com.zero.healthmonitoring.presenter

import android.bluetooth.BluetoothAdapter
import com.zero.healthmonitoring.delegate.SpoDelegate
import com.zero.library.utils.PermissionManager
import android.os.Handler
import android.os.Message
import com.creative.base.BaseDate.Wave
import com.creative.FingerOximeter.IFingerOximeterCallBack
import com.bde.parentcyTransport.ACSUtility.blePort
import com.creative.base.BLESender
import com.creative.base.BLEReader
import com.creative.FingerOximeter.FingerOximeter
import com.creative.bluetooth.ble.IBLECallBack
import com.creative.bluetooth.ble.BLEOpertion
import java.lang.Exception


class SpoPresenter : BasePresenter<SpoDelegate>() {

    private val PERMISSION_RESULT_CODE = 0x100

    private lateinit var bluetoothAdapter: BluetoothAdapter

    private lateinit var permissionManager: PermissionManager

    private lateinit var ble: BLEOpertion

    private var pod: FingerOximeter? = null

    override fun doMain() {
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        this.permissionManager = PermissionManager(this, this)
        if (!this.permissionManager.isRequestPermissions(
                PERMISSION_RESULT_CODE,
                "android.permission.BLUETOOTH",
                "android.permission.BLUETOOTH_ADMIN"
            )
        ) {
            openBluetooth()
        }
    }

    private fun openBluetooth() {
        if (this.bluetoothAdapter != null && !this.bluetoothAdapter.isEnabled) {
            this.bluetoothAdapter.enable()
        }
        this.viewDelegate.rootView.postDelayed({
            if (this.bluetoothAdapter.isEnabled) {
                try {
                    ble = BLEOpertion(this, BleCallBack())
                    viewDelegate.para.text = "正在搜索设备..."
                    viewDelegate.wave.text = ".................."
                    myHandler.sendEmptyMessageDelayed(2, 2000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }, 1000)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSION_RESULT_CODE) {
            if (!this.permissionManager.verifyPermissions(grantResults)) {
                this.permissionManager.showMissingPermissionDialog(null)
            } else {
                openBluetooth()
            }
        }
    }

    private val myHandler = object : Handler() {

        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                0 -> {
                    viewDelegate?.para?.text = msg.obj.toString()
                    viewDelegate?.log?.append("${msg.obj} \n")
                }
                1 -> {
                    val wave = msg.obj as List<Wave>
                    var showText = "wave="
                    val iterator = wave.iterator()
                    while (iterator.hasNext()) {
                        val w = iterator.next()
                        showText += w.data.toString() + "---"
                    }
                    viewDelegate?.wave?.text = showText
                    viewDelegate?.log?.append("$showText \n")
                }
                2 -> {
                    ble?.startDiscover()
                    // ble.connect("D0:39:72:BC:58:D1");
                    obtainMessage(0, "开始连接").sendToTarget()
                }
            }
        }
    }

    internal inner class BleCallBack : IBLECallBack {

        override fun onFindDevice(port: blePort) {
            viewDelegate?.log?.append("onFindDevice ${port._device.address} ${port._device.name} ${port.devInfo} \n")
            if (port._device.name.trim { it <= ' ' } == "POD") {// 将POD修改为对应的设备名即可
                ble.stopDiscover()
                object : Thread() {

                    override fun run() {
                        super.run()
                        ble.connect(port)
                    }
                }.start()
            }
        }

        override fun onConnected(port: blePort) {
            viewDelegate?.log?.append("onConnected \n")
            pod = FingerOximeter(
                BLEReader(ble), BLESender(ble),
                FingerOximeterCallBack()
            )
            pod?.Start()
            pod?.SetWaveAction(true)
        }

        override fun onConnectFail() {
            myHandler?.obtainMessage(0, "连接断开").sendToTarget()
            if (pod != null)
                pod!!.Stop()
            pod = null
        }

        override fun onSended(isSend: Boolean) {

        }

        override fun onDiscoveryCompleted(device: List<blePort>) {
            viewDelegate?.log?.append("onDiscoveryCompleted: \n")
            viewDelegate?.log?.append("***************************************************\n")
            device?.forEach{ it ->
                it?.apply {
                    viewDelegate?.log?.append("* ${it.devInfo} \n")
                }
            }
            viewDelegate?.log?.append("***************************************************\n")
        }

        override fun onDisConnect(prot: blePort) {
            myHandler?.obtainMessage(0, "连接断开").sendToTarget()
            pod?.Stop()
            pod = null
        }

        override fun onReadyForUse() {
            viewDelegate?.log?.append("onReadyForUse \n")
        }

    }

    internal inner class FingerOximeterCallBack : IFingerOximeterCallBack {

        override fun OnGetSpO2Param(
            nSpO2: Int, nPR: Int, nPI: Float,
            nStatus: Boolean, nMode: Int, nPower: Float
        ) {
            myHandler?.obtainMessage(0, "接收到参数--$nSpO2 $nPR $nPI").sendToTarget()
        }

        override fun OnGetSpO2Wave(wave: List<Wave>) {
            myHandler?.obtainMessage(1, wave).sendToTarget()
        }

        override fun OnGetDeviceVer(
            nHWMajor: Int, nHWMinor: Int, nSWMajor: Int,
            nSWMinor: Int
        ) {

        }

        override fun OnConnectLose() {

        }
    }

    override fun onDestroy() {
        super.onDestroy()
//        if(this.bluetoothAdapter != null){
//            this.bluetoothAdapter.disable()
//        }
        this.myHandler?.removeCallbacksAndMessages(null)
    }
}