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
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * create by szl on 2018/1/15
 */

public class MapUtils {

    /**
     * * 检查手机上是否安装了指定的软件
     * @param context
     * @return
             */
    public static List<String> getMapApp(Context context){
        //获取packagemanager
        final PackageManager packageManager = context.getPackageManager();
        //获取所有已安装程序的包信息
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);
        //用于存储所有已安装程序的包名
        List<String> packageNames = new ArrayList<>();
        //从pinfo中将包名字逐一取出，压入pName list中
        if(packageInfos != null){
            for(int i = 0; i < packageInfos.size(); i++){
                String packName = packageInfos.get(i).packageName;
                if(packName.equals("com.baidu.BaiduMap")){
                    packageNames.add("百度");
                }else if(packName.equals("com.autonavi.minimap")){
                    packageNames.add("高德");
                }
            }
        }
        return packageNames;
    }

    /**
     * 打开百度地图
     * @param context
     * @param to 目的地经纬度
     * @param city 区域
     * @param destinationName 目的地地址
     */
    public static void openBaiduMap(Context context, double[] to, String city, String destinationName){
        double[] latlng = gaoDeToBaidu(to[1], to[0]);
        Intent i1 = new Intent();
        Uri uri = Uri.parse("baidumap://map/direction?region=" + city + "&destination=latlng:" + latlng[1] + "," + latlng[0] + "|name:" + destinationName + "&mode=driving");
        i1.setData(uri);
        context.startActivity(i1);

    }

    /**
     * 打开高德地图
     * @param context
     * @param to
     */
    public static void openGdMap(Context context, double[] to){
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        Uri uri = Uri.parse("androidamap://navi?sourceApplication=LordLove&poiname=lord&lat="+ to[0] +"&lon=" + to[1] +  "&dev=1&style=2");
        intent.setData(uri);
        context.startActivity(intent);
    }

    /**
     * 打开网页版导航
     * @param context
     * @param mapAppName
     * @param to
     * @param from
     */
    public static void openWebMap(Context context, String mapAppName, double[] to, double[] from, String city, String destinationName){
        String url = "";
        Intent intent = new Intent();
        intent.setAction("android.intent.action.VIEW");
        if(TextUtils.equals(mapAppName, "高德网页")){
            url = "http://m.amap.com/?from=" + from[0] + "," + from[1] + "(from)&to=" + to[0] + "," + to[1] + "(to)&type=0&opt=1&dev=0";
        }else if(TextUtils.equals(mapAppName, "百度网页")){
            double[] latlng = gaoDeToBaidu(from[1], from[0]);
            double[] latlneto = gaoDeToBaidu(to[1], to[0]);
            url = "http://api.map.baidu.com/direction?origin=latlng:" + latlng[1] +"," + latlng[0] + "|name:我的位置" +
                    "&destination=latlng:" + latlneto[1] + "," + latlneto[0] + "|name:"+ destinationName +"&mode=driving&region=" + city + "&output=html" +
                    "&src=主爱之家";
        }
        if(TextUtils.isEmpty(url)){
            return;
        }
        Uri content_url = Uri.parse(url);
        intent.setData(content_url);
        context.startActivity(intent);
    }

    /**
     * 高德地图定位经纬度转百度经纬度
     * @param gd_lon
     * @param gd_lat
     * @return
     */
    public static double[] gaoDeToBaidu(double gd_lon, double gd_lat) {
        double[] bd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = gd_lon, y = gd_lat;
        double z = Math.sqrt(x * x + y * y) + 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) + 0.000003 * Math.cos(x * PI);
        bd_lat_lon[0] = z * Math.cos(theta) + 0.0065;
        bd_lat_lon[1] = z * Math.sin(theta) + 0.006;
        return bd_lat_lon;
    }

    /**
     * 百度地图定位经纬度转高德经纬度
     * @param bd_lat
     * @param bd_lon
     * @return
     */
    public static double[] bdToGaoDe(double bd_lat, double bd_lon) {
        double[] gd_lat_lon = new double[2];
        double PI = 3.14159265358979324 * 3000.0 / 180.0;
        double x = bd_lon - 0.0065, y = bd_lat - 0.006;
        double z = Math.sqrt(x * x + y * y) - 0.00002 * Math.sin(y * PI);
        double theta = Math.atan2(y, x) - 0.000003 * Math.cos(x * PI);
        gd_lat_lon[0] = z * Math.cos(theta);
        gd_lat_lon[1] = z * Math.sin(theta);
        return gd_lat_lon;
    }

    /**
     * 打开地图导航
     * @param context
     * @param to 目的地
     * @param from 我的位置
     * @param city 当前城市
     * @param destinationName 目的地
     * @param mapAppName
     */
    public static void openMap(Context context, double[] to, double[] from, String city, String destinationName, String mapAppName){
        if(TextUtils.equals(mapAppName, "高德")){
            openGdMap(context, to);
        }else if(TextUtils.equals(mapAppName, "百度")){
            openBaiduMap(context, to, city, destinationName);
        }else {
            openWebMap(context, mapAppName, to, from, city, destinationName);
        }
    }
}
