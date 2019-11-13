package com.zero.healthmonitoring.api

import com.zero.healthmonitoring.data.BaseModel
import com.zero.healthmonitoring.data.UserBean
import com.zero.healthmonitoring.data.UserTestBean
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
    /**
     * 注册
     */
    @FormUrlEncoded
    @POST("blood/login/register")
    fun register(@FieldMap map: Map<String, String>): Observable<BaseModel<UserBean>>

    /**
     * 登录
     */
    @FormUrlEncoded
    @POST("blood/login/index")
    fun login(@FieldMap map: Map<String, String>): Observable<BaseModel<UserBean>>

    /**
     * 添加记录
     */
    @FormUrlEncoded
    @POST("blood/user/add_info")
    fun addInfo(@FieldMap map: Map<String, String>): Observable<BaseModel<String>>

    /**
     * 测量信息列表 By year
     */
    @FormUrlEncoded
    @POST("blood/user/blo_list_year")
    fun getBloListByYear(@FieldMap map: Map<String, String?>): Observable<BaseModel<UserTestBean>>

    /**
     * 测量信息列表 By month
     */
    @FormUrlEncoded
    @POST("blood/user/blo_list_month")
    fun getBloListByMonth(@FieldMap map: Map<String, String?>): Observable<BaseModel<UserTestBean>>

    /**
     * 测量信息列表 By day
     */
    @FormUrlEncoded
    @POST("blood/user/blo_list_day")
    fun getBloListByDay(@FieldMap map: Map<String, String?>): Observable<BaseModel<UserTestBean>>

    /**
     * 获取所有的年份
     */
    @FormUrlEncoded
    @POST("blood/user/blo_all_year")
    fun getYears(@FieldMap map: Map<String, String?>): Observable<BaseModel<List<String>>>

    /**
     * 获取所有的年份
     */
    @FormUrlEncoded
    @POST("blood/user/edit_pwd")
    fun editPw(@FieldMap map: Map<String, String?>): Observable<BaseModel<String>>
}