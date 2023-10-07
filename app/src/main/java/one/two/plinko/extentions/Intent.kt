package one.two.plinko.extentions

import android.content.Intent
import android.net.Uri
import android.provider.MediaStore

fun Intent.makeItOType(): Intent {
    if(action != Intent.ACTION_GET_CONTENT) {
        action = Intent.ACTION_GET_CONTENT
    }
    if(categories?.contains(CATEGORY_OPENABLE) == false) {
        addCategory(CATEGORY_OPENABLE)
    }
    type = ALL_TYPES
    return this
}

fun Intent.makeItTType(pictFileUri: Uri?): Intent {
    if(action != MediaStore.ACTION_IMAGE_CAPTURE) {
        action = MediaStore.ACTION_IMAGE_CAPTURE
    }
    putExtra(EXTRA_OUTPUT, pictFileUri)
    return this
}

fun Intent.makeItCType(oType: Intent, tType: Intent): Intent {
    if(action != Intent.ACTION_CHOOSER) {
        action = Intent.ACTION_CHOOSER
    }
    putExtra(EXTRA_INTENT, oType)
    putExtra(EXTRA_INITIAL_INTENTS, arrayOf(tType))
    return this
}
private const val CATEGORY_OPENABLE = Intent.CATEGORY_OPENABLE
private const val ALL_TYPES = "*/*"
private const val EXTRA_OUTPUT = MediaStore.EXTRA_OUTPUT
private const val EXTRA_INTENT = Intent.EXTRA_INTENT
private const val EXTRA_INITIAL_INTENTS = Intent.EXTRA_INITIAL_INTENTS