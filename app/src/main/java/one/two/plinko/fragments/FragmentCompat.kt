package one.two.plinko.fragments

import android.app.Activity
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import one.two.plinko.MainActivity
import one.two.plinko.MainActivityViewModel
import one.two.plinko.navigation.NavigationController

abstract class FragmentCompat: Fragment() {
    abstract val currentDestination: Int

    protected val viewModel by lazy {
        getViewModelFromActivity()
    }
    /**
     * A simpler way to inflate the fragment container.
     * @param layoutId fragment layout id.
     * @param attachToRoot Whether the inflated hierarchy should be attached to the root parameter?
     * If false, root is only used to create the correct subclass of LayoutParams for the root view
     * in the XML.
     * By default is false.
     */
    protected fun Pair<LayoutInflater, ViewGroup?>.inflate(
        layoutId: Int,
        attachToRoot: Boolean = false
    ): View? {
        return first.inflate(layoutId, second, attachToRoot)
    }

    /**
     * Get [NavigationController] to navigate between fragments. It is singleton object.
     *
     * If this fragment has been attached to fragment container, but this method has not been
     * called before, returns [NavigationController] and sets navigationManager to result of
     * [getParentFragmentManager] function.
     *
     * If this fragment has not been attached to fragment container, returns [NavigationController]
     * and sets navigationManager to null. Therefore, you must set navigationManager in the future
     * for correct navigation.
     * @return [NavigationController].
     */
    fun getNavigationController(): NavigationController {
        return (navigationController ?:
        NavigationController(if(activity != null)
                parentFragmentManager
            else
                null)).also {
            if(navigationController == null) {
                navigationController = it
            }
        }
    }

    /**
     * Designed for first navigation from [AppCompatActivity] or [Activity].
     * @param fragmentManager [FragmentManager], by default received from
     * [AppCompatActivity.getSupportFragmentManager] function.
     * @param doIfBackStackIsEmpty the action, that will be called when back stack is empty and the
     * last navigation type is [NavigationController.TYPE_POP_BACK_STACK].
     */
    fun doStartNavigation(fragmentManager: FragmentManager, doIfBackStackIsEmpty: () -> Unit) {
        navigationController = getNavigationController().apply {
            setNavigationManagerIfItNull(fragmentManager)
            this.doIfBackStackIsEmpty = doIfBackStackIsEmpty
        }
        navigationController?.navigate(currentDestination, NavigationController.TYPE_ADD, true)
    }

    /**
     * Designed for collecting state flows by one function.
     * @param action what should be done with the state flow value?
     * @return [Job]
     */
    fun<T> StateFlow<T>.observe(action: (T) -> Unit): Job {
        return viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycleStarted {
                collect(action)
            }
        }
    }

    /**
     * Do repeat on lifecycle state is started.
     * @param action what should be repeated?
     */
    private suspend fun repeatOnLifecycleStarted(action: suspend CoroutineScope.() -> Unit) {
        repeatOnLifecycle(Lifecycle.State.STARTED, action)
    }

    /**
     * Returns view model from activity.
     * @return if all OK, [MainActivityViewModel], else null.
     */
    private fun getViewModelFromActivity(): MainActivityViewModel? {
        val activity = activity
        if(activity != null) {
            if(activity is MainActivity) {
                return activity.viewModel
            }
            else {
                Log.e("Get view model", "Error code: 2")
            }
        }
        else {
            Log.e("Get view model", "Error code: 1")
        }
        return null
    }

    /**
     * Designed to perform assigning listeners to buttons with less code.
     * @param ids list of button ids.
     * @param listeners list of button listeners.
     */
    fun<T: View> setButtonsOnClickListeners(ids: List<Int>, listeners: List<(View) -> Unit>) {
        if(ids.size == listeners.size) {
            for(index in ids.indices) {
                view?.findViewById<T>(ids[index])?.setOnClickListener(listeners[index])
            }
        }
        else {
            throw IllegalArgumentException("Size of ids and listeners must be equals!")
        }
    }

    /**
     *
     */
    fun<T> setObservingOnTextViews(ids: List<Int>, stateFlows: List<StateFlow<T>>) {
        if(ids.size == stateFlows.size) {
            view?.run {
                for (index in ids.indices) {
                    stateFlows[index].observe {
                        findViewById<TextView>(ids[index]).text = it.toString()
                    }
                }
            }
        }
        else {
            throw IllegalArgumentException("Size of ids and stateFlows must be equals!")
        }
    }

    companion object {
        /**
         * Navigation controller. It is necessary for navigation between fragments.
         * By default is null, but will receive the value after execution [onCreate] method.
         */
        private var navigationController: NavigationController? = null

    }
}