package com.example.admobimplement

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.MobileAds

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        AdMobManger.initAdMob(this)
    }
}

fun logD(s: String) = Log.d("xyz", s)
fun Context.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()