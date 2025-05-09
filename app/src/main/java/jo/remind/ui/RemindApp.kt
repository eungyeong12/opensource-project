package jo.remind.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController

@Composable
fun RemindApp(
    startDestination: RemindNavigation
) {
    val navController = rememberNavController()

    RemindNavGraph(
        navController = navController,
        startDestination = RemindNavigation.Splash
    )
}