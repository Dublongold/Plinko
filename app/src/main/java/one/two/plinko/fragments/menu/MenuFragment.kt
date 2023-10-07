package one.two.plinko.fragments.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import androidx.core.view.allViews
import one.two.plinko.R
import one.two.plinko.fragments.FragmentCompat
import one.two.plinko.navigation.NavigationController

class MenuFragment: FragmentCompat() {
    override val currentDestination = NavigationController.DESTINATION_MENU

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return (inflater to container).inflate(R.layout.fragment_menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonsOnClickListeners<ImageButton>(
            listOf(R.id.openGameButton, R.id.readPrivacyPolicyButton),
            listOf({
                setButtonsDisabled()
                getNavigationController().navigate(NavigationController.DESTINATION_GAME,
                    NavigationController.TYPE_ADD)
            }, {
                setButtonsDisabled()
                getNavigationController().navigate(NavigationController.DESTINATION_PRIVACY_POLICY,
                    NavigationController.TYPE_ADD)
            })
        )
    }

    private fun setButtonsDisabled() {
        view?.allViews?.filter {
            it is ImageButton
        }?.forEach {
            it.isEnabled = false
        }
    }

    fun restoreButtonsEnabled() {
        view?.allViews?.filter {
            it is ImageButton
        }?.forEach {
            it.isEnabled = true
        }
    }
}