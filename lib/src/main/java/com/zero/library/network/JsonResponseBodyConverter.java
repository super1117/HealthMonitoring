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
import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.ubzx.library.BuildConfig;
import okhttp3.ResponseBody;
import retrofit2.Converter;

import java.io.*;

/**
 * create by szl on 2017/8/23
 */

public class JsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {
    private final Gson gson;
    private final TypeAdapter<T> adapter;
    public final static String JSON_ERROR_STR = "数据结构异常!";


    JsonResponseBodyConverter(Gson gson, TypeAdapter<T> adapter) {
        this.gson = gson;
        this.adapter = adapter;
    }

    @Override
    public T convert(ResponseBody value) throws IOException {
        String response = value.string()
                .replace("\"\"", "null");
        if(response.startsWith("(")){
            response = response.substring(1);
        }
        if(response.endsWith(")")){
            response.substring(0, response.lastIndexOf("}"));
        }
        if (BuildConfig.DEBUG) {
            Log.e("aiya", response);
        }
        try {
            InputStream inputStream = new ByteArrayInputStream(response.getBytes());
            Reader reader = new InputStreamReader(inputStream);
            JsonReader jsonReader = gson.newJsonReader(reader);
            T t = adapter.read(jsonReader);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(JSON_ERROR_STR);
        } finally {
            value.close();
        }
    }
}