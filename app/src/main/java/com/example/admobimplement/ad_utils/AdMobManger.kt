package com.example.admobimplement.ad_utils

import android.app.Activity
import android.content.Context
import android.widget.Toast
import com.example.admobimplement.R
import com.google.android.ads.nativetemplates.TemplateView
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.util.*

object AdMobManger {
    private var maxAdLoad = 5

    private const val maxTryToLoad = 10//when Ad fail now many recursive call cal to reload
    private var maxTriedToLoad = 0

    private val rewardedAds = Stack<RewardedAd>()
    private val interstitialAds = Stack<InterstitialAd>()
    private var adRequest: AdRequest = AdRequest.Builder().build()

    fun setAdRequest(adRequest: AdRequest) {
        AdMobManger.adRequest = adRequest
    }

    fun setMaxAdLoadSize(size: Int) {
        maxAdLoad = size
    }

    fun isRewardedAdLoaded() = rewardedAds.size != 0
    fun isInterstitialAdLoaded() = interstitialAds.size != 0

    fun initAdMob(context: Context) = MobileAds.initialize(context)

    fun loadBannerAd(adView: AdView) = adView.loadAd(AdRequest.Builder().build())

    fun loadInterstitialAd(context: Context, listener: AdListener?) {
        if (interstitialAds.size < maxAdLoad) InterstitialAd.load(context,
            context.getString(R.string.admob_interstitial_id),
            adRequest, object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    listener?.onAdLoadFailed()
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    interstitialAds.add(p0)
                    listener?.onAdLoaded(interstitialAds.size)
                }
            }) else listener?.maximumAdLoaded(interstitialAds.size)
    }

    fun loadRewardedAdReq(context: Context, totalAdLoad: Int, listener: AdListener?) {
        if (rewardedAds.size < maxAdLoad && rewardedAds.size < totalAdLoad)
            loadRewardedAdReq(context, object : AdListener {
                override fun onAdLoaded(totalLoadedAd: Int) {
                    super.onAdLoaded(totalLoadedAd)
                    if (rewardedAds.size < maxAdLoad && rewardedAds.size < totalAdLoad)
                        loadRewardedAdReq(context, totalAdLoad - 1, listener)
                    else listener?.onAdLoaded(rewardedAds.size)
                }

                override fun onAdLoadFailed() {
                    super.onAdLoadFailed()
                    listener?.onAdLoadFailed()
                }
            })
        else listener?.maximumAdLoaded(rewardedAds.size)
    }

    fun loadRewardedAdReq(context: Context, listener: AdListener?) {
        if (rewardedAds.size < maxAdLoad) RewardedAd.load(
            context, context.getString(R.string.admob_rewarded_ad_id),
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    listener?.onAdLoadFailed()
                }

                override fun onAdLoaded(p0: RewardedAd) {
                    super.onAdLoaded(p0)
                    rewardedAds.add(p0)
                    listener?.onAdLoaded(rewardedAds.size)
                }
            })
        else listener?.maximumAdLoaded(interstitialAds.size)
    }

    fun showInterstitialAd(activity: Activity, listener: AdWatchListener) {
        if (!interstitialAds.empty()) interstitialAds.pop().apply {
            show(activity)
            listener.onRewarded()
        } else listener.onAdLoadFailed("Ad Loaded list empty. Please load ad first")
    }

    fun showRewardedAd(activity: Activity, listener: AdWatchListener?) {
        if (!rewardedAds.empty()) rewardedAds.pop().apply {
            show(activity) { listener?.onRewarded() }
        } else listener?.onAdLoadFailed("Ad Loaded list empty. Please load ad first")
    }

    fun loadNativeAdInTemplate(
        context: Context, templateView: TemplateView, listener: AdListener?
    ) {
        AdLoader.Builder(context, context.getString(R.string.admob_native_ad_id))
            .forNativeAd { templateView.setNativeAd(it) }
            .withAdListener(object : com.google.android.gms.ads.AdListener() {
                override fun onAdLoaded() {
                    super.onAdLoaded()
                    listener?.onAdLoaded(1)
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    listener?.onAdLoadFailed()
                }
            })
            .build().loadAd(AdRequest.Builder().build())
    }

    private fun Activity.showToast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    interface AdListener {
        fun onAdLoaded(totalLoadedAd: Int) {}
        fun onAdLoadFailed() {}
        fun maximumAdLoaded(size: Int) {}
        fun adLoadRequested() {}
    }

    interface AdWatchListener {
        fun onRewarded()
        fun onAdLoadFailed(msg: String)
    }
}