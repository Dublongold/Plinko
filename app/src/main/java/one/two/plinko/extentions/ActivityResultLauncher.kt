package one.two.plinko.extentions

import androidx.activity.result.ActivityResultLauncher
import androidx.core.app.ActivityOptionsCompat

operator fun<T> ActivityResultLauncher<T>.invoke(value: T) {
    launch(value)
}

operator fun<T> ActivityResultLauncher<T>.invoke(value: T, options: ActivityOptionsCompat) {
    launch(value, options)
}