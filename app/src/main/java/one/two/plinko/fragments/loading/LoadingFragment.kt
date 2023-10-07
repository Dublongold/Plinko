package one.two.plinko.fragments.loading

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import one.two.plinko.R
import one.two.plinko.WebViewActivity
import one.two.plinko.fragments.FragmentCompat
import one.two.plinko.navigation.NavigationController

class LoadingFragment: FragmentCompat() {
    override val currentDestination = NavigationController.DESTINATION_LOADING
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return (inflater to container).inflate(R.layout.fragment_loading)
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            // TODO Here must be data fetching!
            delay(1500)
            val dataReceived = true
            if(dataReceived) {
                startActivity(
                    Intent(requireContext(), WebViewActivity::class.java)
                )
            }
            else {
                getNavigationController().navigate(
                    NavigationController.DESTINATION_MENU,
                    NavigationController.TYPE_REPLACE,
                    true
                )
            }
        }
    }
}