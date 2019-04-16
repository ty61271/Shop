package com.west.pratice.shop

import android.app.Activity
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.alert
import org.jetbrains.anko.okButton

class SignUpActivity : AppCompatActivity(), AnkoLogger {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_sign_up)
        bt_signup.setOnClickListener {
            val mEmail = edi_signup_email.text.toString()
            val mPassword = edi_sign_password.text.toString()
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(mEmail, mPassword).addOnCompleteListener {
                when (it.isSuccessful) {
                    true -> setAlerDailog("Account Created", it.isSuccessful)
                    else -> setAlerDailog(it.exception?.message, it.isSuccessful)
                }

            }
//            setResult(Activity.RESULT_OK)
//            finish()
        }
    }

    private fun setAlerDailog(message: String?, success: Boolean) {
        alert(message!!, "Sign Up") {
            when (success) {
                true -> okButton {
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                false -> okButton { }
            }
        }.show()
    }
}
