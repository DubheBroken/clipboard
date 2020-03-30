package com.rice.activity

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.*
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.FrameLayout
import com.rice.base.RiceBaseActivity
import com.rice.riceframe.R
import com.rice.tool.TextUtils
import kotlinx.android.synthetic.main.activity_webview.*

/**
 * Created by wry on 2018-07-11 18:02
 */
@SuppressLint("Registered")
class WebViewActivity : RiceBaseActivity() {

    override fun clear() {

    }

    override fun getLayoutId(): Int {
        return R.layout.activity_webview
    }

    private var title = ""
    private var url = ""
    private var customView: View? = null
    private var fullscreenContainer: FrameLayout? = null
    private var customViewCallback: WebChromeClient.CustomViewCallback? = null
    private var textXModel = false //是否开启富文本模式

    override fun getIntentData() {
        window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        if (intent != null && intent.extras != null) {
            textXModel = intent.extras!!.getBoolean("textX", false)
            url = intent.extras!!.getString("url", "")
            title = intent.extras!!.getString("title", "")
        }

        if (!textXModel && !url.startsWith("http")) {
            url = "https://$url"
        }
        initView()
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun initView() {
        imgBack.setOnClickListener {
            onBackPressed()
        }
        imgClose.setOnClickListener { finish() }
        textTitle.text = title
        webview!!.settings.pluginState = WebSettings.PluginState.ON
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            webview!!.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        setStatusBarVisibility(true)
        //声明WebSettings子类
        val webSettings = webview!!.settings
        //如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.javaScriptEnabled = true
        webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN //自适应
        //支持插件
        // webSettings.setPluginsEnabled(true);
        //设置自适应屏幕，两者合用
        webSettings.useWideViewPort = true //将图片调整到适合webview的大小
        webSettings.loadWithOverviewMode = true // 缩放至屏幕的大小
        //缩放操作
        webSettings.setSupportZoom(true) //支持缩放，默认为true。是下面那个的前提。
        webSettings.builtInZoomControls = true //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.displayZoomControls = false //隐藏原生的缩放控件
        //其他细节操作
        webSettings.cacheMode = WebSettings.LOAD_CACHE_ELSE_NETWORK //关闭webview中缓存
        webSettings.allowFileAccess = true //设置可以访问文件
        webSettings.javaScriptCanOpenWindowsAutomatically = true //支持通过JS打开新窗口
        webSettings.loadsImagesAutomatically = true //支持自动加载图片
        webSettings.defaultTextEncodingName = "utf-8" //设置编码格式
        webview!!.webChromeClient = object : WebChromeClient() {

            override fun onReceivedTitle(view: WebView, title: String?) {
                super.onReceivedTitle(view, title)
                if (title != null && title != "about:blank") {
                    textTitle.text = title
                }
            }

            /*** 视频播放相关的方法  */

            override fun getVideoLoadingProgressView(): View? {
                val frameLayout = FrameLayout(this@WebViewActivity)
                frameLayout.layoutParams = WindowManager.LayoutParams(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.MATCH_PARENT
                )
                return frameLayout
            }

            override fun onShowCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
                showCustomView(view, callback)
            }

            override fun onHideCustomView() {
                hideCustomView()
            }

            override fun onProgressChanged(view: WebView, newProgress: Int) {
                if (progressbar != null) {
                    if (newProgress == 100) {
                        progressbar!!.visibility = View.GONE
                    } else {
                        progressbar!!.visibility = View.VISIBLE
                    }
                }
                super.onProgressChanged(view, newProgress)
            }
        }

        //
        //        if (textXModel) {
        //            getText()
        //        } else if (!Common.empty(url)) {
        if (textXModel) {
            //            textTitle.text = "关于我们"
            //            webview!!.loadData(url, "text/html", "UTF-8")
            webview!!.loadDataWithBaseURL(null, url, "text/html", "UTF-8", null)
        } else if (TextUtils.isNotEmpty(url)) {
            webview!!.loadUrl(url)
        }
        //        }
    }


    /**
     * 视频播放全屏
     */
    private fun showCustomView(view: View, callback: WebChromeClient.CustomViewCallback) {
        // if a view already exists then immediately terminate the new one
        if (customView != null) {
            callback.onCustomViewHidden()
            return
        }

        this@WebViewActivity.window.decorView

        val decor = window.decorView as FrameLayout
        fullscreenContainer =
                FullscreenHolder(this@WebViewActivity)
        fullscreenContainer!!.addView(
                view,
                COVER_SCREEN_PARAMS
        )
        decor.addView(
                fullscreenContainer,
                COVER_SCREEN_PARAMS
        )
        customView = view
        setStatusBarVisibility(false)
        customViewCallback = callback
    }

    /**
     * 全屏容器界面
     */
    internal class FullscreenHolder(ctx: Context) : FrameLayout(ctx) {

        init {
            setBackgroundColor(ctx.resources.getColor(android.R.color.black))
        }

        override fun onTouchEvent(evt: MotionEvent): Boolean {
            return true
        }
    }

    private fun setStatusBarVisibility(visible: Boolean) {
        val flag = if (visible) 0 else WindowManager.LayoutParams.FLAG_FULLSCREEN
        window.setFlags(flag, WindowManager.LayoutParams.FLAG_FULLSCREEN)
    }


    /**
     * 隐藏视频全屏
     */
    private fun hideCustomView() {
        if (customView == null) {
            return
        }
        setStatusBarVisibility(true)
        val decor = window.decorView as FrameLayout
        decor.removeView(fullscreenContainer)
        fullscreenContainer = null
        customView = null
        customViewCallback!!.onCustomViewHidden()
        webview!!.visibility = View.VISIBLE
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> {
                /** 回退键 事件处理 优先级:视频播放全屏-网页回退-关闭页面  */
                if (customView != null) {
                    hideCustomView()
                } else {
                    if (webview != null) {
                        if (webview!!.canGoBack()) {
                            webview!!.goBack()
                        } else {
                            finish()
                        }
                    } else {
                        finish()
                    }
                }
                return true
            }
            else -> return super.onKeyUp(keyCode, event)
        }
    }

    override fun onPause() {
        if (webview != null) {
            webview!!.reload()
        }
        super.onPause()
    }


    override fun onBackPressed() {
        if (webview != null) {
            if (webview!!.canGoBack()) {
                webview!!.goBack()
            } else {
                super.onBackPressed()
            }
        }
    }

    public override fun onDestroy() {
        if (webview != null) {
            (webview!!.parent as ViewGroup).removeView(webview)
            webview!!.destroy()
        }
        super.onDestroy()
    }

    public override fun onStart() {
        super.onStart()

    }

    public override fun onStop() {
        super.onStop()

    }

    companion object {

        /**
         * 视频全屏参数
         */
        protected val COVER_SCREEN_PARAMS = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}
