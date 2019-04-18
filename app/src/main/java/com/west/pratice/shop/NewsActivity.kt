package com.west.pratice.shop

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class NewsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        val fragmentTransition = supportFragmentManager.beginTransaction()
        fragmentTransition.add(R.id.container, NewsFragment.getInstant)
        fragmentTransition.commit()
    }
}
