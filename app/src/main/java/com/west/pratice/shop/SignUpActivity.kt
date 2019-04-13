package com.west.pratice.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        bt_signup.setOnClickListener {
            val mEmail = ed_email.text.toString()
            val mPassword = ed_password.text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(mEmail, mPassword)
        }
    }
}
