package com.west.pratice.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        fragmentTransaction
            .add(R.id.container, NewsFragment.getInstant)
            .commit()
    }
}
