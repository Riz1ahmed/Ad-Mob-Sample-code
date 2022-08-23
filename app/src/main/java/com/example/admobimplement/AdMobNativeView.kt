package com.example.admobimplement

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import com.google.android.gms.ads.nativead.NativeAdView

class AdMobNativeView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attributeSet, defStyleAttr) {
    init {
        val viewRes = R.layout.native_ad_view
        setView(viewRes)
    }

    fun setView(@LayoutRes layoutId: Int) {
        val view = LayoutInflater.from(context).inflate(layoutId, null) as NativeAdView
        removeAllViews()
        addView(view)
    }
}