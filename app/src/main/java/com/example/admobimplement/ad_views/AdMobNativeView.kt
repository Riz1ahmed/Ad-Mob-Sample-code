package com.example.admobimplement.ad_views

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.*
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.admobimplement.R
import com.example.admobimplement.hide
import com.example.admobimplement.show
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdLoader
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.nativead.MediaView
import com.google.android.gms.ads.nativead.NativeAd
import com.google.android.gms.ads.nativead.NativeAdView

class AdMobNativeView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var nativeAdView: NativeAdView
    private var nativeAd: NativeAd? = null

    private val appIconView: ImageView
    private val titleView: TextView
    private val descriptionView: TextView
    private val ratingBar: RatingBar
    private val tertiaryView: TextView
    private val mediaView: MediaView
    private val actionBtnView: Button
    private val background: ConstraintLayout

    init {
        context.theme.obtainStyledAttributes(attrs, R.styleable.AdMobNativeView, 0, 0).apply {
            getResourceId(R.styleable.AdMobNativeView_adLayout, R.layout.native_ad_sample)
                .let { layoutId ->
                    nativeAdView = LayoutInflater
                        .from(context).inflate(layoutId, null) as NativeAdView

                    removeAllViews()
                    addView(nativeAdView)

                    //init Views
                    appIconView = findViewById(R.id.ad_app_icon)
                    titleView = findViewById(R.id.txt_app_name)
                    descriptionView = findViewById(R.id.txt_description)
                    tertiaryView = findViewById(R.id.txt_body)
                    mediaView = findViewById(R.id.img_ad_Image)
                    actionBtnView = findViewById(R.id.btn_install)
                    ratingBar = findViewById(R.id.rating_bar)
                    background = findViewById(R.id.layout_background)

                    nativeAdView.iconView = appIconView
                    nativeAdView.headlineView = titleView
                    nativeAdView.bodyView = descriptionView
                    nativeAdView.mediaView = mediaView
                    nativeAdView.callToActionView = actionBtnView
                    nativeAdView.starRatingView = ratingBar

                }
        }
    }

    fun changedLayout(@LayoutRes layoutRes: Int) {
        nativeAdView = LayoutInflater.from(context).inflate(layoutRes, null) as NativeAdView
        removeAllViews()
        addView(nativeAdView)
    }

    fun initAd(nativeAdId: String, listener: AdListener) {
        val adLoader = AdLoader.Builder(context, nativeAdId)
            .forNativeAd { setNativeAd(it) }
            .withAdListener(listener)
            .build()
        adLoader.loadAd(AdRequest.Builder().build())
    }


    private fun setNativeAd(nativeAd1: NativeAd) {
        this.nativeAd = nativeAd1

        val store = nativeAd!!.store
        val advertiser = nativeAd!!.advertiser
        val appIcon = nativeAd!!.icon
        val title: String? = nativeAd!!.headline
        val btnText: String? = nativeAd!!.callToAction
        val starRating = nativeAd!!.starRating
        val body = nativeAd!!.body
        val description = if (adHasOnlyStore(nativeAd!!)) {
            nativeAdView.storeView = descriptionView
            store
        } else if (!advertiser.isNullOrEmpty()) {
            nativeAdView.advertiserView = descriptionView
            advertiser
        } else ""

        //These must be check before set.
        /*(nativeAdView.iconView as ImageView).setImageDrawable(nativeAd?.icon?.drawable)
        (nativeAdView.headlineView as TextView).text = nativeAd?.headline
        (nativeAdView.starRatingView as RatingBar).rating = nativeAd?.starRating!!.toFloat()
        (nativeAdView.bodyView as TextView).text = nativeAd?.body*/

        nativeAdView.iconView = appIconView
        nativeAdView.headlineView = titleView
        nativeAdView.bodyView = descriptionView
        nativeAdView.mediaView = mediaView
        nativeAdView.callToActionView = actionBtnView
        nativeAdView.starRatingView = ratingBar
        nativeAdView.bodyView = background

        //Icon
        with(appIconView) {
            if (appIcon == null) hide()
            else setImageDrawable(appIcon.drawable).also { show() }
        }
        setText(title, titleView)
        setText(description, descriptionView)
        //setText(body,tertiaryView)
        with(actionBtnView) {
            if (btnText.isNullOrEmpty()) hide()
            else text = btnText.also { show() }
        }

        //  Set the secondary view to be the star rating if available.
        if (starRating != null && starRating > 0) {
            tertiaryView.hide()
            ratingBar.show()
            ratingBar.rating = starRating.toFloat()
        } else {
            tertiaryView.text = body
            tertiaryView.show()
            ratingBar.hide()
        }

        /*if (!body.isNullOrEmpty()) {
            tertiaryView.text = body
            nativeAdView.bodyView = tertiaryView
        } else tertiaryView.hide()*/

        nativeAdView.setNativeAd(this.nativeAd!!)
    }

    private fun adHasOnlyStore(nativeAd: NativeAd): Boolean {
        val store = nativeAd.store
        val advertiser = nativeAd.advertiser
        return !TextUtils.isEmpty(store) && TextUtils.isEmpty(advertiser)
    }

    /**
     * To prevent memory leaks, make sure to destroy your ad when you don't need it anymore. This
     * method does not destroy the template view.
     * https://developers.google.com/admob/android/native-unified#destroy_ad
     */
    fun destroyNativeAd() {
        nativeAd?.destroy()
    }

    private fun setText(text: String?, view: TextView) {
        if (text.isNullOrEmpty()) view.hide()
        else {
            view.text = text
            view.show()
        }
    }
}