package one.two.plinko.entities

import android.graphics.Bitmap
import android.util.Log
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient

class NetworkManagerClient: WebViewClient() {
    private var lastUrlLoadingOverrides = false
    private var pageState = OnPageStates.NONE
        set(state) {
            Log.i(TAG, "Page state: $state")
            field = state
        }
    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        lastUrlLoadingOverrides = request?.url?.toString()?.let {
            !(it.contains("http") && it.contains("/") && it.contains(":"))
        } ?: true
        return lastUrlLoadingOverrides
    }

    override fun onPageCommitVisible(view: WebView?, url: String?) {
        pageState = OnPageStates.COMMIT_VISIBLE
        Log.i(TAG, "Page url: $url")
        super.onPageCommitVisible(view, url)
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        pageState = OnPageStates.FINISHED
        Log.i(TAG, "Page url: $url")
        super.onPageFinished(view, url)
    }

    override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
        pageState = OnPageStates.STARTED
        Log.i(TAG, "Page url: $url, favicon (null or not): $favicon")
        super.onPageStarted(view, url, favicon)
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        Log.e(TAG, "${request?.url}, ${errorResponse?.statusCode}")
        super.onReceivedHttpError(view, request, errorResponse)
    }

    enum class OnPageStates {
        NONE,
        STARTED,
        FINISHED,
        COMMIT_VISIBLE
    }

    companion object {
        const val TAG = "Network manager client"
    }
}