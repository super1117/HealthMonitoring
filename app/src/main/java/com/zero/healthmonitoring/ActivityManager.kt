package com.zero.healthmonitoring

import android.app.Activity
import java.util.*

class ActivityManager private constructor() {

    /**
     * 获取当前activity
     * @return
     */
    val currentActivity: Activity?
        get() = if (mActivityStack!!.size > 0) mActivityStack!!.lastElement() else null

    /**
     * 添加activity到堆栈
     * @param activity
     */
    fun addActivity(activity: Activity) {
        if (mActivityStack == null) {
            mActivityStack = Stack()
        }
        mActivityStack!!.add(activity)
    }

    /**
     * 结束指定activity
     * @param activity
     */
    fun finishActivity(activity: Activity?) {
        var activity: Activity? = activity ?: return
        mActivityStack!!.remove(activity)
        activity!!.finish()
        activity = null
    }

    /**
     * 移除指定activity
     * @param activity
     */
    fun removeActivity(activity: Activity?) {
        var activity = activity
        if (activity != null) {
            mActivityStack!!.remove(activity)
            activity = null
        }
    }

    /**
     * 结束指定类名的activity
     * @param cls
     */
    fun finishActivity(cls: Class<*>) {
        for (activity in mActivityStack!!) {
            if (activity.javaClass == cls) {
                finishActivity(activity)
            }
        }
    }

    /**
     * 结束所有的activity
     */
    fun finishAllActivity() {
        try {
            var i = 0
            val j = mActivityStack!!.size
            while (i < j) {
                if (mActivityStack!![i] != null) {
                    mActivityStack!![i].finish()
                }
                i++
            }
            mActivityStack!!.clear()
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    /**
     * 退出应用程序
     * @param hasService
     */
    fun exit(hasService: Boolean?) {
        finishAllActivity()
        if (!(hasService)!!) {//如果没有后台运行程序就完全退出
            System.exit(0)
        }
    }

    companion object {

        private var mActivityStack: Stack<Activity>? = null

        private var mActivityManager: ActivityManager? = null

        val instance: ActivityManager
            get() {
                if (mActivityManager == null) {
                    mActivityManager = ActivityManager()
                }
                return mActivityManager!!
            }
    }

}