package jo.remind.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jo.remind.ui.home.HomeScreen
import jo.remind.ui.login.LoginScreen
import jo.remind.ui.splash.SplashScreen

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

        composable(RemindNavigation.Login.route) {
            LoginScreen(
                onLoginSuccess = {
                    navActions.navigateTo(RemindNavigation.Main, RemindNavigation.Login)
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
