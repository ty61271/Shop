package com.west.pratice.shop

import android.app.Activity
import android.content.Context
import android.content.Intent

fun <T> Activity.setActivityIntentForResult(context: Context, cls: Class<T>, requestCode: Int) {
    this.startActivityForResult(Intent(context, cls), requestCode)
}