package jo.remind.ui

import androidx.navigation.NavHostController

sealed class RemindNavigation(val route: String) {
    object Splash : RemindNavigation("splash")
    object Login : RemindNavigation("login")
    object Main : RemindNavigation("main")
    object Search : RemindNavigation("search")
    object Home : RemindNavigation("home")
    object Record : RemindNavigation("record")
    object MovieSearch : RemindNavigation("movie_search") {
        fun withDate(date: String) = "$route?date=$date"
    }

    object BookSearch : RemindNavigation("book_search") {
        fun withDate(date: String) = "$route?date=$date"
    }
    object MovieRegistration : RemindNavigation("movie_registration")
    object BookRegistration : RemindNavigation("book_registration")
    object MovieRecord : RemindNavigation("movie_record")
    object BookRecord : RemindNavigation("book_record")
    object DailyRecord : RemindNavigation("daily_record") {
        fun withDate(date: String) = "$route?date=$date"
    }
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