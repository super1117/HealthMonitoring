package com.zero.library.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ubzx.library.R;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class PermissionManager {

    private Activity activity;

    private Object object;

    public PermissionManager(Activity activity, Object object){
        this.activity = activity;
        this.object = object;
    }

    public boolean isRequestPermissions(int requestCode, @NonNull String... needPermissions) {
        try {
            if (Build.VERSION.SDK_INT >= 23 && this.activity.getApplicationInfo().targetSdkVersion >= 23) {
                List<String> needRequestPermissonList = findDeniedPermissions(needPermissions);
                if (null != needRequestPermissonList
                        && needRequestPermissonList.size() > 0) {
                    String[] array = needRequestPermissonList.toArray(new String[needRequestPermissonList.size()]);
                    Method method = this.object.getClass().getMethod("requestPermissions", new Class[]{String[].class, int.class});

                    method.invoke(this.object, array, requestCode);
                    return true;
                }else{
                    return false;
                }
            }else{
                return false;
            }
        } catch (Throwable e) {
            Toast.makeText(this.activity, "权限获取失败", Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    /**
     * 获取权限集中需要申请权限的列表
     *
     * @param permissions
     * @return
     * @since 2.5.0
     */
    private List<String> findDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissonList = new ArrayList<>();
        try {
            for (String perm : permissions) {
                Method checkSelfMethod = this.activity.getClass().getMethod("checkSelfPermission", String.class);
                Method shouldShowRequestPermissionRationaleMethod = this.object.getClass().getMethod("shouldShowRequestPermissionRationale",
                        String.class);
                if ((Integer) checkSelfMethod.invoke(this.activity, perm) != PackageManager.PERMISSION_GRANTED
                        || (Boolean) shouldShowRequestPermissionRationaleMethod.invoke(this.object, perm)) {
                    needRequestPermissonList.add(perm);
                }
            }
        } catch (Exception e) {
            Toast.makeText(this.activity, "权限获取失败!", Toast.LENGTH_SHORT).show();;
        }
        return needRequestPermissonList;
    }

    /**
     * 检测是否说有的权限都已经授权
     *
     * @param grantResults
     * @return
     * @since 2.5.0
     */
    public boolean verifyPermissions(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * 显示提示信息
     *
     * @since 2.5.0
     */
    public void showMissingPermissionDialog(final OnPDialogClickListener listener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this.activity);
        builder.setTitle("提示");
        builder.setMessage(this.activity.getResources().getString(R.string.app_name)+"未获取到权限");
        builder.setNegativeButton("取消",(dialog, which) -> {
            dialog.dismiss();
            if(listener != null){
                listener.cancel();
            }
        });
        builder.setPositiveButton(R.string.setting,(dialog, which) -> JumpPermissionManagement.GoToSetting(activity));
        builder.setCancelable(false);
        builder.show();
    }

    /**
     * 检查手机是否是miui系统
     *
     * @return
     */
    public boolean isMIUI() {
        String device = Build.MANUFACTURER;
        System.out.println("Build.MANUFACTURER = " + device);
        if (device.equals("Xiaomi")) {
            System.out.println("this is a xiaomi device");
            Properties prop = new Properties();
            try {
                prop.load(new FileInputStream(new File(Environment.getRootDirectory(), "build.prop")));
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
            return prop.getProperty("ro.miui.ui.version.code", null) != null
                    || prop.getProperty("ro.miui.ui.version.name", null) != null
                    || prop.getProperty("ro.miui.internal.storage", null) != null;
        } else {
            return false;
        }
    }

    public interface OnPDialogClickListener{
        void cancel();
    }
}
