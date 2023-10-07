package one.two.plinko.entities

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebSettings
import android.webkit.WebView
import one.two.plinko.interfaces.INetworkInstaller

class NetworkInstaller(private val networkManager: WebView): INetworkInstaller {
    private val netSettings
        get() = networkManager.settings

    /**
     * Sets [WebSettings.setJavaScriptEnabled] and
     * [WebSettings.setJavaScriptCanOpenWindowsAutomatically] to true.
     */
    @SuppressLint("SetJavaScriptEnabled")
    override fun setJavaSettings() {
        netSettings.javaScriptCanOpenWindowsAutomatically = true
        if(netSettings.javaScriptCanOpenWindowsAutomatically) {
            netSettings.javaScriptEnabled = netSettings.javaScriptCanOpenWindowsAutomatically
        }
        else {
            throw IllegalStateException("javaScriptCanOpenWindowsAutomatically inside netSettings" +
                    "is false, but was set to true earlier.")
        }
    }

    /**
     * Sets to true all properties in [WebSettings] that contain "allow" or "enabled" in their name.
     */
    override fun setAllEnablesAndAllows() {
        netSettings.databaseEnabled = true
        netSettings.allowFileAccess = netSettings.databaseEnabled
        val isOk = if(netSettings.allowFileAccess) {
            netSettings.allowContentAccess = netSettings.allowFileAccess
            netSettings.domStorageEnabled = netSettings.allowFileAccess
            netSettings.domStorageEnabled
        }
        else {
            throw IllegalStateException("allowFileAccess inside netSettings is false, but was set to true earlier.")
        }
        if(isOk) {
            setDeprecated()
        }
    }

    /**
     * Sets to true all deprecated properties in [WebSettings].
     */
    @Suppress("Deprecation")
    private fun setDeprecated() {
        netSettings.allowFileAccessFromFileURLs = true
        val allowed = netSettings.allowFileAccessFromFileURLs
        netSettings.allowUniversalAccessFromFileURLs = allowed
    }

    override fun setUseWideViewPortAndLoadWithOverviewMode() {
        var isTrue = true
        if(!netSettings.useWideViewPort) {
            netSettings.useWideViewPort = true
            isTrue = netSettings.useWideViewPort
        }
        netSettings.loadWithOverviewMode = isTrue
    }

    @SuppressLint("WrongConstant")
    override fun setNotTrueValues() {
        netSettings.mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
        netSettings.cacheMode = LOAD_DEFAULT
        netSettings.userAgentString = netSettings.userAgentString.let {
            val index = it.indexOf("; wv")
            netSettings.userAgentString.removeRange(index, index + 4)
        }
    }

    override fun setCookieManagerProperties() {
        val cookManagerObj = CookieManager.getInstance()
        val needReset = !cookManagerObj.acceptCookie() ||
                !cookManagerObj.acceptThirdPartyCookies(networkManager)
        cookManagerObj.setAcceptCookie(needReset)
        cookManagerObj.setAcceptThirdPartyCookies(networkManager, needReset)
    }

    override fun setNetworkChromeClient(networkChromeClient: NetworkChromeClient) {
        networkManager.webChromeClient = networkChromeClient
    }

    override fun setNetworkManagerClient(networkManagerClient: NetworkManagerClient) {
        networkManager.webViewClient = networkManagerClient
    }

    @Suppress("unused")
    companion object {
        // This is all for mixedContentMode inside netSettings
        /** Same as [WebSettings.MIXED_CONTENT_ALWAYS_ALLOW] */
        const val MIXED_CONTENT_ALWAYS_ALLOW = 0
        /** Same as [WebSettings.MIXED_CONTENT_NEVER_ALLOW] */
        const val MIXED_CONTENT_NEVER_ALLOW = 1
        /** Same as [WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE] */
        const val MIXED_CONTENT_COMPATIBILITY_MODE = 2
        // This too.
        /** Same as [WebSettings.LOAD_DEFAULT] */
        const val LOAD_DEFAULT = -1
        /** Same as [WebSettings.LOAD_NORMAL] */
        const val LOAD_NORMAL = 0
        /** Same as [WebSettings.LOAD_CACHE_ELSE_NETWORK] */
        const val LOAD_CACHE_ELSE_NETWORK = 1
        /** Same as [WebSettings.LOAD_NO_CACHE] */
        const val LOAD_NO_CACHE = 2
        /** Same as [WebSettings.LOAD_CACHE_ONLY] */
        const val LOAD_CACHE_ONLY = 3
    }
}