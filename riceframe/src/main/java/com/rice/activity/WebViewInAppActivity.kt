package com.rice.activity

import android.annotation.SuppressLint
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rice.base.RiceBaseActivity
import com.rice.riceframe.R
import com.rice.tool.TextUtils
import kotlinx.android.synthetic.main.activity_webview.*


/**
 * Created by wry on 2018-07-11 18:02
 */
@SuppressLint("Registered")
class WebViewInAppActivity : RiceBaseActivity() {

    override fun clear() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_webview
    }

    private var title = ""
    private var url = ""

    override fun getIntentData() {

        if (intent != null && intent.extras != null) {
            url = intent.extras!!.getString("url", "")
            title = intent.extras!!.getString("title", "")
        }

        //        if (!textXModel && !url.startsWith("http")) {
        //            url = "https://$url"
        //        }
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
        imgClose.setOnClickListener { finish() }
        textTitle.text = title
        val settings = webview.settings
        settings.domStorageEnabled = true
        settings.useWideViewPort = true
        settings.javaScriptEnabled = true
        settings.blockNetworkImage = false
        settings.javaScriptCanOpenWindowsAutomatically = true
        settings.loadsImagesAutomatically = true
        settings.savePassword = false
        settings.userAgentString = settings.userAgentString
        settings.defaultTextEncodingName = "UTF-8"
        settings.loadWithOverviewMode = true
        settings.allowContentAccess = true
        if (TextUtils.isNotEmpty(url)) {
            webview!!.loadUrl(url)
        }

        webview.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView, title: String?) {
                super.onReceivedTitle(view, title)
                if (title != null && title != "about:blank") {
                    textTitle.text = title
                }
            }
        }

        webview.webViewClient = object : WebViewClient() {

            override fun shouldOverrideUrlLoading(view: WebView, url: String?): Boolean {
                view.loadUrl(url)
                return super.shouldOverrideUrlLoading(view, url)
            }
        }
    }

    //    private fun setStatusBarVisibility(visible: Boolean) {
    //        val flag = if (visible) 0 else WindowManager.LayoutParams.FLAG_FULLSCREEN
    //        window.setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    //    }
    //
    //    override fun onPause() {
    //        if (webview != null) {
    //            webview!!.reload()
    //        }
    //        super.onPause()
    //    }
    //
    //
    //    override fun onBackPressed() {
    //        if (webview != null) {
    //            if (webview!!.canGoBack()) {
    //                webview!!.goBack()
    //            } else {
    //                super.onBackPressed()
    //            }
    //        }
    //    }
    //
    //    public override fun onDestroy() {
    //        if (webview != null) {
    //            (webview!!.parent as ViewGroup).removeView(webview)
    //            webview!!.destroy()
    //        }
    //        super.onDestroy()
    //    }
    //
    //    public override fun onStart() {
    //        super.onStart()
    //
    //    }
    //
    //    public override fun onStop() {
    //        super.onStop()
    //
    //    }
    //
    //    companion object {
    //
    //        /**
    //         * 视频全屏参数
    //         */
    //        protected val COVER_SCREEN_PARAMS = FrameLayout.LayoutParams(
    //                ViewGroup.LayoutParams.MATCH_PARENT,
    //                ViewGroup.LayoutParams.MATCH_PARENT
    //        )
    //    }
}
