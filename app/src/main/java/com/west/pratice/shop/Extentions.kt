package com.west.pratice.shop

import android.app.Activity
import android.content.Context
import kotlinx.android.synthetic.main.activity_nickname.*

fun Activity.setNickname(nickname: String) {
    getSharedPreferences("shop", Context.MODE_PRIVATE)
        .edit()
        .putString("NICKNAME", ed_nickname.text.toString())
        .apply()
}

fun Activity.getNickname(): String? = getSharedPreferences("shop", Context.MODE_PRIVATE).getString("NICKNAME", "")