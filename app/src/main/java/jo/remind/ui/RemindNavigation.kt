package jo.remind.ui

import androidx.navigation.NavHostController

sealed class RemindNavigation(val route: String) {
    object Splash : RemindNavigation("splash")
    object Login : RemindNavigation("login")
    object Main : RemindNavigation("main")
    object Search : RemindNavigation("search")
    object Home : RemindNavigation("home")
    object Record : RemindNavigation("Record")
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