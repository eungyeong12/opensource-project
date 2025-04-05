package jo.opensource.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jo.opensource.ui.home.HomeScreen
import jo.opensource.ui.home.MainScreen
import jo.opensource.ui.home.RecordScreen
import jo.opensource.ui.home.SearchScreen
import jo.opensource.ui.splash.SplashScreen

@Composable
fun RemindNavGraph(
    startDestination: RemindNavigation = RemindNavigation.Splash,
    navController: NavHostController = rememberNavController(),
    modifier: Modifier = Modifier
) {
    val navActions = remember(navController) { RemindNavigationActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(RemindNavigation.Splash.route) {
            SplashScreen(
                onFinish = { destination ->
                    navActions.navigateTo(destination, RemindNavigation.Splash)
                }
            )
        }

        composable(RemindNavigation.Main.route) {
            MainScreen()
        }

        composable(RemindNavigation.Search.route) {
            SearchScreen()
        }

        composable(RemindNavigation.Home.route) {
            HomeScreen()
        }

        composable(RemindNavigation.Record.route) {
            RecordScreen()
        }
    }
}
