package com.vc.base.web

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.vc.base.BaseFgm
import com.vc.base.R

abstract class BaseWebFgm : BaseFgm() {
    private val mAgentWeb: AgentWeb by lazy {
        val mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(initWebViewGroup(), LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator(-1, 3)
                .setWebViewClient(initWebViewClient())
                .setWebChromeClient(initWebChromeClient())
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)
                .interceptUnkownUrl()
                .createAgentWeb()
                .ready()
                .go(initWebUrl())

        mAgentWeb.agentWebSettings.webSettings.useWideViewPort = true
        return@lazy mAgentWeb
    }

    override fun onPause() {
        mAgentWeb.webLifeCycle.onPause()
        super.onPause()
    }

    override fun onResume() {
        mAgentWeb.webLifeCycle.onResume()
        super.onResume()
    }

    override fun onDestroy() {
        mAgentWeb.webLifeCycle.onDestroy()
        mAgentWeb.clearWebCache()
        super.onDestroy()
    }

    fun getAgentWeb() = mAgentWeb

    abstract fun initWebViewGroup(): ViewGroup

    abstract fun initWebUrl(): String

    abstract fun initWebViewClient(): WebViewClient

    abstract fun initWebChromeClient(): WebChromeClient
}