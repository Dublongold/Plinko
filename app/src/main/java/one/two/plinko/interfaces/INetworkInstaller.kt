package one.two.plinko.interfaces

import android.annotation.SuppressLint
import android.webkit.CookieManager
import android.webkit.WebSettings
import one.two.plinko.entities.NetworkChromeClient
import one.two.plinko.entities.NetworkInstaller
import one.two.plinko.entities.NetworkManagerClient

interface INetworkInstaller {
    fun setJavaSettings()

    fun setAllEnablesAndAllows()

    fun setUseWideViewPortAndLoadWithOverviewMode()

    fun setNotTrueValues()

    fun setCookieManagerProperties()

    fun setNetworkChromeClient(networkChromeClient: NetworkChromeClient)

    fun setNetworkManagerClient(networkManagerClient: NetworkManagerClient)
}