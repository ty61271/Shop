package com.west.pratice.shop

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
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
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        AlertDialog.Builder(this)
                            .setTitle("Sign up")
                            .setMessage("Account created")
                            .setPositiveButton("OK") { _, _ ->
                                setResult(Activity.RESULT_OK)
                                finish()
                            }.show()
                    } else {
                        AlertDialog.Builder(this)
                            .setTitle("Sign up")
                            .setMessage(it.exception?.message)
                            .setPositiveButton("OK", null)
                            .show()
                    }
                }
        }
    }
}
