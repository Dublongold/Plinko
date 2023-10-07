package one.two.plinko.entities

import android.net.Uri
import android.webkit.ValueCallback

class FilePathCallbackContainer {
    var filePathCallback: ValueCallback<Array<Uri>>? = null
}