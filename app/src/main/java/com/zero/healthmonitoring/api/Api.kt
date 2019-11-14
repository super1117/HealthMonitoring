package com.zero.healthmonitoring.api

import com.zero.healthmonitoring.data.BaseModel
import com.zero.healthmonitoring.data.UserBean
import com.zero.healthmonitoring.data.UserTestBean
import io.reactivex.Observable
import retrofit2.http.*

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
     * 应用内修改密码接口
     */
    @FormUrlEncoded
    @POST("blood/user/edit_pwd")
    fun editPw(@FieldMap map: Map<String, String?>): Observable<BaseModel<String>>

    /**
     * 获取验证码
     * 参数：type(注册register/忘记密码forget)、mobile 成功返回1失败返回文字信息
     */
    @FormUrlEncoded
    @POST("blood/login/sendcode")
    fun getVerifyCode(@FieldMap map: Map<String, String?>): Observable<BaseModel<String>>

    /**
     * 登录也注册、忘记密码
     * 参数：type(注册register/忘记密码forget)、mobile、code（用户填写的验证码） 、pwd、docid成功返回1失败返回文字信息
     */
    @FormUrlEncoded
    @POST("blood/login/verify_code")
    fun registerOrForget(@FieldMap map: Map<String, String?>): Observable<BaseModel<String>>

    /**
     * 医生患者列表
     */
    @FormUrlEncoded
    @POST("blood/user/patient_list")
    fun getPatientList(@FieldMap map: Map<String, String?>): Observable<BaseModel<List<UserBean>>>



}