package one.two.plinko

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.badlogic.gdx.Gdx
import com.facebook.applinks.AppLinkData
import one.two.plinko.extentions.addOnBackPressedCallback
import one.two.plinko.fragments.FragmentCompat
import one.two.plinko.fragments.loading.LoadingFragment
import one.two.plinko.navigation.NavigationController

class MainActivity : AppCompatActivity() {

    val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(application)
            .create(MainActivityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS)
        setContentView(R.layout.activity_main)
        LoadingFragment().doStartNavigation(supportFragmentManager) {
            finish()
        }
        AppLinkData.fetchDeferredAppLinkData(this) {
            if(it != null) {
                val tree = it.argumentBundle?.getString("target_url")
                Log.i("Deep", "Tree: $tree")
                Log.i("Deep", "appLinkData: $it")
                Log.i("Deep", "targetUri: ${it.targetUri}")
                val url = it.targetUri?.toString()?.replace("/", "%2F")
                    ?.replace(":", "%3A")
                Log.i("Deep", "Url: $url")
            }
            else {
                Log.i("App link data", "null")
            }
        }
        addOnBackPressedCallback {
            val fragment = supportFragmentManager.fragments.lastOrNull()
            if(fragment is FragmentCompat) {
                fragment.getNavigationController().navigate(
                    type = NavigationController.TYPE_POP_BACK_STACK
                )
            }
            else {
                finish()
            }
        }
    }
}