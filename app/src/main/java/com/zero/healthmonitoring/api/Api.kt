package com.zero.healthmonitoring.api

import com.zero.healthmonitoring.data.BaseModel
import com.zero.healthmonitoring.data.UserBean
import io.reactivex.Observable
import retrofit2.http.*

/**
 * 登录接口:http://101.201.53.157/blood/login/index 参数：uid、pwd 成功返回uid失败返回文字信息
注册接口:http://101.201.53.157/blood/login/register 参数：uid、pwd、docid 成功返回uid失败返回文字信息
增加记录接口:http://101.201.53.157/blood/user/add_info 参数：uid、spo、bpm 成功返回1失败返回文字信息
修改会员信息接口:http://101.201.53.157/blood/user/edit_info 参数：uid、tid(将会员信息从上至下按顺序123456789类似array(0=>'name',1=>'mobile',2=>'sex',3=>'age',4=>'height',5=>'weight',6=>'birth',7=>'address');)、eval(修改的某一项值) 成功返回1失败返回文字信息
测量信息列表接口:http://101.201.53.157/blood/user/blo_list 参数：uid、pagenum 成功返回所有相关信息失败返回文字信息
 */

interface Api {

    @FormUrlEncoded
    @POST("blood/login/register")
    fun register(@FieldMap map: Map<String, String>): Observable<BaseModel<UserBean>>

    @FormUrlEncoded
    @POST("blood/login/index")
    fun login(@FieldMap map: Map<String, String>): Observable<BaseModel<UserBean>>

}