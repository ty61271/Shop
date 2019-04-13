package com.west.pratice.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)
        signup.setOnClickListener {
            val sEmail = email.text.toString()
            val sPassword = password.text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(sEmail,sPassword)
        }
    }
}
