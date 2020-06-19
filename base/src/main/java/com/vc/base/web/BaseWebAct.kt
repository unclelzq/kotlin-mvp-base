package com.vc.base.web

import android.view.KeyEvent
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.LinearLayout
import com.just.agentweb.AgentWeb
import com.just.agentweb.DefaultWebClient
import com.vc.base.BaseAct
import com.vc.base.R


abstract class BaseWebAct : BaseAct() {

    private val mAgentWeb: AgentWeb by lazy {
        val mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(initWebViewGroup(), LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .setWebChromeClient(initWebChromeClient())
                .setWebViewClient(initWebViewClient())
                .setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .createAgentWeb()
                .ready()
                .go(initWebUrl())

        mAgentWeb.agentWebSettings.webSettings.useWideViewPort = true
        return@lazy mAgentWeb
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (mAgentWeb.handleKeyEvent(keyCode, event)) {
            return true
        }
        return super.onKeyDown(keyCode, event)
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
        super.onDestroy()
        mAgentWeb.webLifeCycle.onDestroy()
    }

    fun getAgentWeb() = mAgentWeb

    abstract fun initWebViewGroup(): ViewGroup

    abstract fun initWebUrl(): String

    abstract fun initWebViewClient(): WebViewClient

    abstract fun initWebChromeClient(): WebChromeClient
}
