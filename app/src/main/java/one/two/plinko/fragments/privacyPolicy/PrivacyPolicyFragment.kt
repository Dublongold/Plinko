package one.two.plinko.fragments.privacyPolicy

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import one.two.plinko.R
import one.two.plinko.fragments.FragmentCompat
import one.two.plinko.navigation.NavigationController

class PrivacyPolicyFragment: FragmentCompat() {
    override val currentDestination = NavigationController.DESTINATION_PRIVACY_POLICY

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return (inflater to container).inflate(R.layout.fragment_privacy_policy)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setButtonsOnClickListeners<ImageButton>(
            listOf(R.id.backToHomeButton),
            listOf {
                getNavigationController().navigate(
                    type = NavigationController.TYPE_POP_BACK_STACK
                )
            },
        )
    }
}