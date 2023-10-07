package one.two.plinko.extentions

import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity

fun AppCompatActivity.addOnBackPressedCallback(callback: () -> Unit) {
    onBackPressedDispatcher.addCallback(object: OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            callback()
        }
    })
}