package com.vc.base

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.AbsoluteLayout
import android.widget.ProgressBar

/**
 * 带进度条的webView
 */
class ProgressWebView  : WebView {
    var progressbar: ProgressBar?=null
//    private var context: Context? = null

    constructor(context: Context) :super(context) {
//        this.context = context
        progressbar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        progressbar?.layoutParams = AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 8, 0, 0)
        addView(progressbar)
        // setWebViewClient(new WebViewClient(){});
        webChromeClient = MyWebChromeClient()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
//        this.context = context
        progressbar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
        progressbar?.layoutParams = AbsoluteLayout.LayoutParams(AbsoluteLayout.LayoutParams.MATCH_PARENT, 8, 0, 0)
        addView(progressbar)
        progressbar = progressbar

        // 删除浏览器弹出的是否记住密码功能
        val set = this.settings
        set.savePassword = false
        set.saveFormData = false
        set.javaScriptEnabled = true
        webChromeClient = MyWebChromeClient()
        webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return false

            }
        }
    }
    inner class MyWebChromeClient : WebChromeClient() {
        override fun onProgressChanged(view: WebView, newProgress: Int) {
            super.onProgressChanged(view, newProgress)
            if (newProgress == 100) {
                progressbar?.visibility = View.GONE
            } else {
                if (progressbar?.visibility == View.GONE)
                    progressbar?.visibility = View.VISIBLE
                progressbar?.progress = newProgress
            }
        }
    }

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        val lp = progressbar?.layoutParams as AbsoluteLayout.LayoutParams
        lp.x = l
        lp.y = t
        progressbar?.layoutParams = lp
        super.onScrollChanged(l, t, oldl, oldt)
    }
}