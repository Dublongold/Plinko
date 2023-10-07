package one.two.plinko.entities

import android.net.Uri
import android.os.Message
import android.util.Log
import android.webkit.ConsoleMessage
import android.webkit.GeolocationPermissions
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.result.ActivityResultLauncher

class NetworkChromeClient(
    private val activityLauncher: ActivityResultLauncher<String>,
    private val filePathCallbackContainer: FilePathCallbackContainer
): WebChromeClient() {
    private var windowLastState = WindowLastState.NONE
        set(value) {
            field = value
            Log.i(TAG, "Window state changed to $value.")
        }
    private var geolocationPermissions = false
        set(value) {
            field = value
            Log.i(TAG, "Geolocation permissions ${if(value) "show" else "hide"} prompt.")
        }
    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?
    ): Boolean {
        activityLauncher.launch(CAMERA)
        filePathCallbackContainer.filePathCallback = filePathCallback
        return true
    }

    override fun onCloseWindow(window: WebView?) {
        windowLastState = WindowLastState.CLOSED
        super.onCloseWindow(window)
    }

    override fun onCreateWindow(
        view: WebView?,
        isDialog: Boolean,
        isUserGesture: Boolean,
        resultMsg: Message?
    ): Boolean {
        windowLastState = WindowLastState.CREATED
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg)
    }

    override fun onGeolocationPermissionsHidePrompt() {
        geolocationPermissions = false
        super.onGeolocationPermissionsHidePrompt()
    }

    override fun onGeolocationPermissionsShowPrompt(
        origin: String?,
        callback: GeolocationPermissions.Callback?
    ) {
        geolocationPermissions = true
        Log.i(TAG, "Geolocation origin: $origin. Callback is " +
                "${if(callback == null) "" else "not"} null.")
        super.onGeolocationPermissionsShowPrompt(origin, callback)
    }

    override fun onConsoleMessage(consoleMessage: ConsoleMessage?): Boolean {
        Log.i(TAG, "Console: ${consoleMessage?.message()}.")
        return super.onConsoleMessage(consoleMessage)
    }

    enum class WindowLastState() {
        NONE,
        CLOSED,
        CREATED
    }

    companion object {
        const val TAG = "Network chrome client"
        const val CAMERA = "android.permission.CAMERA"
    }

}