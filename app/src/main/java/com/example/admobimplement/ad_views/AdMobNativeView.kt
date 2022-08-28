package com.example.admobimplement.ad_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.LayoutRes
import com.example.admobimplement.R
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAdView

class AdMobNativeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private var myAdView: NativeAdView

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.AdMobNativeView, 0, 0).apply {
            getResourceId(R.styleable.AdMobNativeView_adLayout, R.layout.native_ad_sample)
                .let { layoutId ->
                    myAdView = LayoutInflater.from(context).inflate(layoutId, null) as NativeAdView
                    removeAllViews()
                    addView(myAdView)
                }
        }
    }

    fun changedLayout(@LayoutRes layoutRes: Int) {
        myAdView = LayoutInflater.from(context).inflate(layoutRes, null) as NativeAdView
        removeAllViews()
        addView(myAdView)
    }

    fun initAd(nativeAdId: String) {
        val adLoader = AdLoader.Builder(context, nativeAdId)
            .forNativeAd { nativeAd ->
                myAdView.iconView = findViewById<ImageView>(R.id.ad_app_icon)
                myAdView.headlineView = findViewById<TextView>(R.id.txt_app_name)
                myAdView.bodyView = findViewById<TextView>(R.id.txt_description)
                myAdView.mediaView = findViewById<MediaView>(R.id.img_ad_Image)
                myAdView.callToActionView = findViewById<Button>(R.id.btn_install)
                myAdView.starRatingView = findViewById<RatingBar>(R.id.rating_bar)

                (myAdView.iconView as ImageView).setImageDrawable(nativeAd.icon?.drawable)
                (myAdView.headlineView as TextView).text = nativeAd.headline
                (myAdView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
                (myAdView.bodyView as TextView).text = nativeAd.body

                myAdView.setNativeAd(nativeAd)
            }
            .withAdListener(object : AdListener() {

            }).build()

        adLoader.loadAd(AdRequest.Builder().build())
    }
}