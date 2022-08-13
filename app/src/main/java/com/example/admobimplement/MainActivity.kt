package com.example.admobimplement

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.admobimplement.databinding.ActivityMainBinding
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import java.util.*


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
        binding.btnInterstitial.setOnClickListener {
            AdMobManger.showInterstitialAd(this, adRequest)
        }
        binding.btnLoadRewarded.setOnClickListener {
            AdMobManger.requestForRewardedAd(this, adRequest)
        }
        binding.btnShowRewarded.setOnClickListener {
            AdMobManger.showRewardedAd(this)
        }

        binding.btnNativeAd.setOnClickListener { showNativeAd() }
        /*setupBannerAd()
        binding.btnInterstitial.setOnClickListener { setupInterstitialAd() }
        binding.btnLoadRewarded.setOnClickListener { loadRewardedAd() }
        binding.btnShowRewarded.setOnClickListener { showRewardedAd() }*/
    }

    private fun showNativeAd() {
        AdLoader.Builder(this, getString(R.string.admob_native_ad_id))
            .forNativeAd {
                val nativeView = binding.layoutNative.root
                nativeView.setNativeAd(it)
            }
            .withAdListener(object : AdListener() {

                override fun onAdLoaded() {
                    super.onAdLoaded()
                    showToast("native Loaded")
                }

                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    showToast("native Load failed")
                }

                override fun onAdClosed() {
                    super.onAdClosed()
                }

            }).build()
    }


    /*val rewardedAds = Stack<RewardedAd>()
    private fun showRewardedAd() {
        if (!rewardedAds.empty()) {
            rewardedAds.pop()?.let {
                it.show(this) {
                    showToast("Ad watched, Remain ${rewardedAds.size} Ad")
                }
            }
        } else showToast("No Ad Loaded")
    }

    private fun loadRewardedAd() {
        showToast("Ad Loading...")
        RewardedAd.load(
            this, getString(R.string.admob_rewarded_ad_id),
            adRequest, object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(p0: LoadAdError) {
                    super.onAdFailedToLoad(p0)
                    showToast("AdLoad failed")
                }

                override fun onAdLoaded(p0: RewardedAd) {
                    super.onAdLoaded(p0)
                    rewardedAds.add(p0)
                    showToast("Total rewarded Ad loaded: ${rewardedAds.size}")
                }
            })
    }

    private fun setupInterstitialAd() {
        showToast("Ad Loading...")
        AdMobManger.showInterstitialAd(this)
    }

    private fun setupBannerAd() {
        binding.adViewBanner.loadAd(adRequest)
    }*/
}
