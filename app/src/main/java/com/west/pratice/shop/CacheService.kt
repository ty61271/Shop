package com.west.pratice.shop

import android.app.IntentService
import android.content.Intent
import android.os.IBinder
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info

class CacheService : IntentService("CacheService"), AnkoLogger {
    companion object {
        val ACTION_CACHE_DONE = "action_cache_done"
    }

    override fun onHandleIntent(intent: Intent?) {
        info("onHandleIntent")
        val title = intent?.getStringExtra("TITLE")
        val image = intent?.getStringExtra("IMAGE")
        info("downloading... $title $image")
        sendBroadcast(Intent(ACTION_CACHE_DONE))
    }

    override fun onCreate() {
        super.onCreate()
        info("onCreate")
    }

    override fun onDestroy() {
        super.onDestroy()
        info("onDestroy")
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

}