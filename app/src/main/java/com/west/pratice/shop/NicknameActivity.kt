package com.west.pratice.shop

import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_nickname.*

class NicknameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)
        done.setOnClickListener {
            setNickname(nick.text.toString())
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
