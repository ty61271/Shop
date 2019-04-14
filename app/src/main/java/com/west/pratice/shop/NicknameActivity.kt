package com.west.pratice.shop

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_nickname.*

class NicknameActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_nickname)
        done.setOnClickListener {
            setNickname(nick.text.toString())
//            Firebase realtime database
            FirebaseDatabase.getInstance()
                .getReference("users")
                .child(FirebaseAuth.getInstance().currentUser!!.uid)
                .child("nickname")
                .setValue(nick.text.toString())
            setResult(Activity.RESULT_OK)
            finish()
        }
    }
}
