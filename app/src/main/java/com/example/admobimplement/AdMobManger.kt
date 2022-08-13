package com.example.admobimplement

import android.app.Activity
import android.content.Context
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.util.*

object AdMobManger {

    val rewardedAds = Stack<RewardedAd>()

    fun initAdMob(context: Context) = MobileAds.initialize(context)

    fun loadBannerAd(adView: AdView) = adView.loadAd(AdRequest.Builder().build())

    fun showInterstitialAd(activity: Activity, adRequest: AdRequest? = null) {

        InterstitialAd.load(activity, activity.getString(R.string.admob_interstitial_id),
            adRequest ?: AdRequest.Builder().build(), object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    activity.showToast("AdLoad failed")
                }

                override fun onAdLoaded(p0: InterstitialAd) {
                    p0.show(activity)
                }
            })
    }

    fun showRewardedAd(activity: Activity) {
        if (!rewardedAds.empty()) rewardedAds.pop().apply {
            show(activity) {
                activity.showToast("Ad watched, Remain ${rewardedAds.size} Ad")
            }
        } else {
            requestForRewardedAd(activity)
            activity.showToast("No Ad Loaded. Requested for load.")
        }
    }

    fun requestForRewardedAd(activity: Activity, adRequest: AdRequest? = null) {
        if (rewardedAds.size < 5) RewardedAd.load(
            activity, activity.getString(R.string.admob_rewarded_ad_id),
            adRequest ?: AdRequest.Builder().build(), object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    activity.showToast("AdLoad failed")
                }

                override fun onAdLoaded(p0: RewardedAd) {
                    super.onAdLoaded(p0)
                    rewardedAds.add(p0)
                    activity.showToast("Total rewarded Ad loaded: ${rewardedAds.size}")
                }
            })
        else activity.showToast("Maximum Ad loaded. Please watch first.")
    }
}