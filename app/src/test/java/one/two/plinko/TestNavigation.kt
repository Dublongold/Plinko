package one.two.plinko

import one.two.plinko.fragments.loading.LoadingFragment
import one.two.plinko.fragments.menu.MenuFragment
import one.two.plinko.navigation.NavigationController
import org.junit.Test

import org.junit.Assert.*

class TestNavigation {
    @Test
    fun `Test back stack size changing by add, replace, pop back stack way`() {
        val navigationController = NavigationController(null)

        navigationController.navigate(NavigationController.DESTINATION_LOADING, NavigationController.TYPE_ADD)
        navigationController.navigate(NavigationController.DESTINATION_MENU, NavigationController.TYPE_REPLACE)
        navigationController.navigate(NavigationController.TYPE_POP_BACK_STACK, NavigationController.TYPE_POP_BACK_STACK)

        assert(navigationController.getBackStackSize() == 0)
    }


    @Test
    fun `Test back stack size changing by add, replace, add in back stack too, pop back stack way`() {
        val navigationController = NavigationController(null)

        navigationController.navigate(NavigationController.DESTINATION_LOADING, NavigationController.TYPE_ADD)
        navigationController.navigate(NavigationController.DESTINATION_MENU, NavigationController.TYPE_REPLACE)
        navigationController.navigate(NavigationController.DESTINATION_GAME, NavigationController.TYPE_ADD)
        navigationController.navigate(NavigationController.TYPE_POP_BACK_STACK, NavigationController.TYPE_POP_BACK_STACK)

        assert(navigationController.getBackStackSize() == 1)
    }

    @Test
    fun `Test getting navigation controller from fragments`() {
        val tempFragment = LoadingFragment()
        val navigationController = tempFragment.getNavigationController()

        val secondTempFragment = MenuFragment()
        val secondNavigationController = secondTempFragment.getNavigationController()

        assertSame(navigationController, secondNavigationController)
    }
}