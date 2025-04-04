package jo.opensource.ui

import androidx.navigation.NavHostController

sealed class RemindNavigation(val route: String) {
    object Splash : RemindNavigation("splash")
    object Home : RemindNavigation("home")
}

class RemindNavigationActions(private val navController: NavHostController) {
    fun navigateTo(destination: RemindNavigation, popUpTo: RemindNavigation? = null) {
        navController.navigate(destination.route) {
            popUpTo?.let {
                popUpTo(it.route) {
                    inclusive = true
                }
            }
            launchSingleTop = true
            restoreState = true
        }
    }
}