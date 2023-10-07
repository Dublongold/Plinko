package com.example.business_app_card

import android.webkit.WebSettings
import android.webkit.WebView
import androidx.test.platform.app.InstrumentationRegistry
import one.two.plinko.entities.NetworkInstaller
import one.two.plinko.entities.NetworkManagerClient
import org.junit.Test

class TestNetworkInstaller {
    @Test
    fun checkIfJavascriptSettingsIsOK() {
        val context = InstrumentationRegistry.getInstrumentation().context
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val networkManager = WebView(context)
            val networkInstaller = NetworkInstaller(networkManager)
            networkInstaller.setJavaSettings()

            val isOk = networkManager.settings.javaScriptEnabled ==
                    networkManager.settings.javaScriptCanOpenWindowsAutomatically
                    && networkManager.settings.javaScriptEnabled
            assert(isOk)
        }
    }

    @Test
    fun checkIfNotTrueValuesIsCorrect() {
        val context = InstrumentationRegistry.getInstrumentation().context
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val networkManager = WebView(context)
            val netSettings = networkManager.settings
            val networkInstaller = NetworkInstaller(networkManager)
            networkInstaller.setNotTrueValues()

            val check1 = netSettings.cacheMode == WebSettings.LOAD_DEFAULT
            val check2 = netSettings.mixedContentMode == WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            val check3 = !netSettings.userAgentString.contains("; wv")

            val isOk = check1 == check2 == check3
            assert(isOk) {
                "Check1: $check1, Check2: $check2, Check3: $check3."
            }
        }
    }

    @Test
    fun checkIfSettingTwoPropertiesIsOk() {
        val context = InstrumentationRegistry.getInstrumentation().context
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val networkManager = WebView(context)
            val netSettings = networkManager.settings
            val networkInstaller = NetworkInstaller(networkManager)
            networkInstaller.setUseWideViewPortAndLoadWithOverviewMode()

            val check1 = netSettings.useWideViewPort
            val check2 = netSettings.loadWithOverviewMode

            val isOk = check1 == check2
            assert(isOk) {
                "Check1: $check1, Check2: $check2."
            }
        }
    }

    @Test
    fun checkCorrectSettingOfClient() {
        val networkManagerClient = NetworkManagerClient()
        val context = InstrumentationRegistry.getInstrumentation().context
        InstrumentationRegistry.getInstrumentation().runOnMainSync {
            val networkManager = WebView(context)
            val networkInstaller = NetworkInstaller(networkManager)
            networkInstaller.setNetworkManagerClient(networkManagerClient)

            assert(networkManagerClient == networkManager.webViewClient)
        }
    }
}