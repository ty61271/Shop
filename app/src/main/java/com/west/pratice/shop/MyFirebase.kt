package com.west.pratice.shop

import android.app.Activity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class MyFirebase : Activity() {

    val auth = FirebaseAuth.getInstance()
    val database = FirebaseDatabase.getInstance().getReference(MyFirebaseUtil.REF_USERS)
    fun setNcikname(nickname: String) {
        database
            .child(auth.currentUser!!.uid)
            .child(MyFirebaseUtil.CHILD_NICKNAME)
            .setValue(nickname)

    }
}
