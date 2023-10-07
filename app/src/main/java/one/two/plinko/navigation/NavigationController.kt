package one.two.plinko.navigation

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import one.two.plinko.R
import one.two.plinko.fragments.FragmentCompat
import one.two.plinko.fragments.game.GameFragment
import one.two.plinko.fragments.loading.LoadingFragment
import one.two.plinko.fragments.menu.MenuFragment
import one.two.plinko.fragments.privacyPolicy.PrivacyPolicyFragment

class NavigationController(
    private var navigationManager: FragmentManager?
) {
    private val backStack = mutableListOf<NavigationControllerEntry>()
    var doIfBackStackIsEmpty: (() -> Unit)? = null

    fun setNavigationManagerIfItNull(navigationManager: FragmentManager) {
        this.navigationManager = this.navigationManager ?: navigationManager
    }

    /**
     * It need for set start destination, when it is first created fragment.
     * @param destination integer code of necessary fragment, for example [DESTINATION_LOADING].
     * You must set according to the fragment.
     * @param type type of navigation. Can be [TYPE_ADD], [TYPE_REPLACE], [TYPE_POP_BACK_STACK],
     * but you must set it to [TYPE_ADD].
     */
    fun FragmentCompat.setStartDestination(
        destination: Int = DESTINATION_LOADING,
        type: Int = TYPE_REPLACE
    ) {
        backStack.add(NavigationControllerEntry(destination, type, this))
    }

    /**
     * Navigate between fragments. If type is [TYPE_POP_BACK_STACK],
     * you can pass anything as [destination].
     * @param destination integer code of necessary fragment, for example [DESTINATION_LOADING].
     * @param type type of navigation. Can be [TYPE_ADD], [TYPE_REPLACE], [TYPE_POP_BACK_STACK].
     * @param allowSateLoss if you do navigation after [AppCompatActivity.onSaveInstanceState], set
     * to true, otherwise - false.
     */
    fun navigate(
        destination: Int = DESTINATION_LOADING,
        type: Int = TYPE_REPLACE,
        allowSateLoss: Boolean = false
    ) {
        lateinit var removedFragment: FragmentCompat
        if(type == TYPE_POP_BACK_STACK) {
            removedFragment = backStack.removeLast().fragment
        }
        if(type == TYPE_POP_BACK_STACK && backStack.size > 0 || type == TYPE_ADD ||
            type == TYPE_REPLACE && backStack.size > 0
            ) {
            if(navigationManager == null) {
                Log.w("Navigation controller", "Navigation manager is null!!!")
            }
            val navigationControllerEntry = if(type == TYPE_POP_BACK_STACK) {
                backStack.last()
            }
            else {
                NavigationControllerEntry(
                    destination,
                    type,
                    getFragmentByDestination(destination)
                )
            }
            val transaction = navigationManager?.beginTransaction()

            when (type) {
                TYPE_ADD -> {
                    backStack.add(navigationControllerEntry)
                    transaction?.add(
                        DEFAULT_CONTAINER, navigationControllerEntry.fragment
                    )
                }
                TYPE_REPLACE -> {
                    backStack.removeLast()
                    backStack.add(navigationControllerEntry)
                    transaction?.replace(
                        DEFAULT_CONTAINER, navigationControllerEntry.fragment
                    )
                }
                TYPE_POP_BACK_STACK -> {
                    transaction?.remove(removedFragment)
                    backStack.last().fragment.let {
                        if(it is MenuFragment) {
                            it.restoreButtonsEnabled()
                        }
                    }
                }
            }

            if(allowSateLoss) {
                transaction?.commitAllowingStateLoss()
            }
            else {
                transaction?.commit()
            }
        }
        else if (type == TYPE_POP_BACK_STACK) {
            doIfBackStackIsEmpty?.invoke()
        }
        else {
            Log.wtf("Navigation controller", "Something wrong.\n" +
                    "Type: ${type}." +
                    "backStack.size: ${backStack.size}")
            /*
            type == TYPE_POP_BACK_STACK && backStack.size > 0 || type == TYPE_ADD ||
            type == TYPE_REPLACE && backStack.size > 0
             */
        }
    }

    /**
     * Returns count of [NavigationControllerEntry] inside back stack..
     */
    fun getBackStackSize() = backStack.size

    /**
     * Returns a fragment depending on [destination]. If destination is invalid, throws
     * [IllegalArgumentException].
     * @param destination integer code of necessary fragment, for example [DESTINATION_LOADING].
     * @return Child fragment of [FragmentCompat], for example [LoadingFragment].
     * @throws IllegalArgumentException
     */
    private fun getFragmentByDestination(destination: Int) = when(destination) {
        DESTINATION_LOADING -> LoadingFragment()
        DESTINATION_MENU -> MenuFragment()
        DESTINATION_GAME -> GameFragment()
        DESTINATION_PRIVACY_POLICY -> PrivacyPolicyFragment()
        else -> throw IllegalArgumentException("Invalid destination $destination.")
    }

    /**
     * A class for storing fragments with their purpose and type inside back stack.
     */
    data class NavigationControllerEntry(
        val destination: Int,
        val type: Int,
        val fragment: FragmentCompat
    )
    companion object {
        val DEFAULT_CONTAINER = R.id.individualFragments

        const val DESTINATION_LOADING = 0
        const val DESTINATION_MENU = 1
        const val DESTINATION_GAME = 2
        const val DESTINATION_PRIVACY_POLICY = 3

        const val TYPE_POP_BACK_STACK = -1
        const val TYPE_ADD = 0
        const val TYPE_REPLACE = 1
    }
}