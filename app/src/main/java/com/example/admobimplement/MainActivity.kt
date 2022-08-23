package com.example.admobimplement

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.admobimplement.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.nativead.MediaView


class MainActivity : AppCompatActivity() {
    private val binding: ActivityMainBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityMainBinding.inflate(layoutInflater)
    }
    private val adRequest = AdRequest.Builder().build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        AdMobManger.loadBannerAd(binding.adViewBanner)
        AdMobManger.loadBannerAd(binding.adViewSmartBanner)

        setupInterstitialAd()
        setupRewardedAd()
        showDefaultNativeAd()
        showCustomNativeAd()
    }

    private fun setupInterstitialAd() {
        binding.btnInterstitialShow.setOnClickListener {
            AdMobManger.showInterstitialAd(this, object : AdMobManger.AdWatchListener {
                override fun onRewarded() {}

                override fun onAdLoadFailed(msg: String) {}
            })
        }

        binding.btnInterstitialLoad.setOnClickListener {
            AdMobManger.loadInterstitialAd(this, object : AdMobManger.AdListener {
                override fun onAdLoaded(totalLoadedAd: Int) {
                    super.onAdLoaded(totalLoadedAd)
                    showToast("Loaded Rewarded Ad $totalLoadedAd")
                }

                override fun onAdLoadFailed() {
                    super.onAdLoadFailed()
                    showToast("Ad Load failed")
                }
            })
        }
    }

    private fun setupRewardedAd() {
        binding.btnLoadRewarded.setOnClickListener {
            AdMobManger.loadRewardedAdReq(this, object : AdMobManger.AdListener {
                override fun onAdLoaded(totalLoadedAd: Int) {
                    super.onAdLoaded(totalLoadedAd)
                    showToast("Loaded Rewarded Ad $totalLoadedAd")
                }

                override fun onAdLoadFailed() {
                    super.onAdLoadFailed()
                    showToast("Ad Load failed")
                }
            })
        }
        binding.btnShowRewarded.setOnClickListener {
            AdMobManger.showRewardedAd(this, object : AdMobManger.AdWatchListener {
                override fun onRewarded() {}
                override fun onAdLoadFailed(msg: String) {}
            })
        }
    }

    private fun showDefaultNativeAd() {
        AdLoader.Builder(this, getString(R.string.admob_native_ad_id))
            .forNativeAd { nativeAd ->
                logD("NativeAd loaded")
                binding.avDefaultNative.setNativeAd(nativeAd)
            }.build().loadAd(adRequest)
    }

    private fun showCustomNativeAd() {
        logD("NativeAd loading...")
        val adLoader = AdLoader.Builder(this, getString(R.string.admob_native_ad_id))
            .forNativeAd { nativeAd ->
                logD("NativeAd loaded")
                val myAdView = binding.layoutNative.root
                val adBinder = binding.layoutNative

                myAdView.iconView = adBinder.adAppIcon
                myAdView.headlineView = adBinder.txtAppName
                myAdView.bodyView = adBinder.txtDescription
                myAdView.mediaView = adBinder.imgAdImage
                myAdView.callToActionView = adBinder.btnInstall
                myAdView.starRatingView = adBinder.ratingBar

                //Icon
                (myAdView.iconView as ImageView).setImageDrawable(nativeAd.icon?.drawable)
                (myAdView.headlineView as TextView).text = nativeAd.headline
                (myAdView.starRatingView as RatingBar).rating = nativeAd.starRating!!.toFloat()
                (myAdView.bodyView as TextView).text = nativeAd.body

                myAdView.setNativeAd(nativeAd)

            }
            .withAdListener(object : AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    logD("NativeAd loaded")
                    showToast("native Loaded")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    logD("NativeAd load failed ${p0.responseInfo}")
                    showToast("native Load failed")
                }
            }).build()
        adLoader.loadAd(adRequest)
    }
}
