package one.two.plinko

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.webkit.WebView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import one.two.plinko.entities.FilePathCallbackContainer
import one.two.plinko.entities.NetworkChromeClient
import one.two.plinko.entities.NetworkInstaller
import one.two.plinko.entities.NetworkManagerClient
import java.io.File
import one.two.plinko.extentions.*
import one.two.plinko.interfaces.INetworkInstaller

class WebViewActivity : AppCompatActivity() {
    private var networkManager: WebView? = null
    private val filePathContainer = FilePathCallbackContainer()
    private var privateUri: Uri? = null
    private var lerfi: String? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web)
        networkManager = findViewById(R.id.webView)
        lerfi = "http://tsapptest.xyz" // TODO Here must be data fetching.
        installation()
        networkManager!!.loadUrl(lerfi!!)
    }

    override fun onStart() {
        super.onStart()
        addOnBackPressedCallback {
            networkManager?.let {
                if(it.canGoBack()) {
                    it.goBack()
                }
            }
        }
    }

    private fun installation() {
        val networkManager = networkManager
        if(networkManager != null) {
            val networkInstaller: INetworkInstaller = NetworkInstaller(networkManager)
            networkInstaller.setJavaSettings()
            networkInstaller.setAllEnablesAndAllows()
            networkInstaller.setNotTrueValues()
            networkInstaller.setUseWideViewPortAndLoadWithOverviewMode()
            networkInstaller.setNetworkManagerClient(NetworkManagerClient())
            finishInstallation(networkInstaller)
        }
    }

    private fun finishInstallation(networkInstaller: INetworkInstaller) {
        networkInstaller.setNetworkChromeClient(
            NetworkChromeClient(stringActivityResultLauncher, filePathContainer)
        )
        networkInstaller.setCookieManagerProperties()
    }

    private val stringActivityResultLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {
        lifecycleScope.launch(Dispatchers.IO) {
            var pictFile: File? = null
            try {
                val file = File.createTempFile(
                    "temp_picture_file",
                    ".jpg",
                    getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                )
                pictFile = file
                privateUri = Uri.fromFile(pictFile)
            } catch (fileException: Exception) {
                if (privateUri != null) {
                    privateUri = null
                }
                Log.e("PhotoFile", "Something happened while creating picture file.",
                    fileException)
            }
            activityResultLauncherForChoosing(
                Intent(Intent.ACTION_CHOOSER).makeItCType(
                    Intent(Intent.ACTION_GET_CONTENT).makeItOType(),
                    Intent().makeItTType(Uri.fromFile(pictFile))
                )
            )
        }
    }
    private val activityResultLauncherForChoosing = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        val filePathCallback = filePathContainer.filePathCallback ?:
            return@registerForActivityResult
        val necessaryString = it.data?.dataString
        if(necessaryString != null && it.resultCode == -1) {
            filePathCallback.onReceiveValue(arrayOf(Uri.parse(it.data?.dataString)))
        }
        else {
            filePathCallback.onReceiveValue(when {
                it.resultCode != -1 -> null
                it.data == null || it.data?.dataString == null -> {
                    privateUri?.let { privateUri ->
                        arrayOf(privateUri)
                    }
                }
                else -> null
            })
        }
        filePathContainer.filePathCallback = null
    }

    public override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        networkManager!!.saveState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        networkManager?.restoreState(savedInstanceState)
    }
}