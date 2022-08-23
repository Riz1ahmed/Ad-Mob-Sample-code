package com.example.admobimplement

import android.app.Application
import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class MyApplication : Application() {
     //lateinit var adLoader:AdLoader
    override fun onCreate() {
        super.onCreate()
        //AdMobManger.initAdMob(this)
        MobileAds.initialize(this)
        //adLoader.loadAd(AdRequest.Builder().build())
    }
}

fun logD(s: String) = Log.d("xyz", s)
fun Context.showToast(msg: String) = Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()