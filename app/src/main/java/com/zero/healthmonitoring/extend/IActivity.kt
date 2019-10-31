package com.zero.healthmonitoring.extend

import android.app.Activity
import android.content.Context
import android.widget.Toast

fun Activity.toast(context: Context, msg: String){
    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
}