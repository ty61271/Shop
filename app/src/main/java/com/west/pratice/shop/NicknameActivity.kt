package com.west.pratice.shop

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_nickname.*

class NicknameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)

        bt_sava.setOnClickListener {
            val nickname = edi_nickname.text.toString()
            val myFirebase = MyFirebase()
            myFirebase.setNcikname(nickname)
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
