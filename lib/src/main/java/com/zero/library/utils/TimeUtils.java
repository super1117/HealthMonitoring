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
//         佛祖保佑       永无BUG     永不修改                       //
////////////////////////////////////////////////////////////////////

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * create by szl on 2017/8/26
 */

public class TimeUtils {
    /**
     * yyyy-MM-dd HH:mm:ss
     * @param time
     */
    public static String getSendTime(long time){
        SimpleDateFormat dfs = new SimpleDateFormat("MM-dd HH:mm");
        try {
            Date date = dfs.parse(dfs.format(time));
            return dfs.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "1970-01-01";
    }

    /**
     * 获取当前时间
     * @return
     */
    public static String getTimeNow(){
       return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    /**
     * 获取当前时间
     * @param pattern
     * @return
     */
    public static String getTimeNow(String pattern){
        return new SimpleDateFormat(pattern).format(new Date());
    }

    /**\
     * 获取月的天数
     * @param year
     * @param month
     * @return
     */
    public static int getDaysOfMonth(String year, String month) {
        try{
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(sdf.parse(year + "-" + month));
            return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        }catch (Exception e){
            return 30;
        }
    }
}
