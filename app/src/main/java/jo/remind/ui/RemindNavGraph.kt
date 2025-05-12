package jo.remind.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import jo.remind.ui.home.HomeScreen
import jo.remind.ui.login.LoginScreen
import jo.remind.ui.registration.MovieRegistrationScreen
import jo.remind.ui.search.BookSearchScreen
import jo.remind.ui.search.MovieSearchScreen
import jo.remind.ui.splash.SplashScreen

@Composable
fun RemindNavGraph(
    navController: NavHostController,
    startDestination: RemindNavigation,
    modifier: Modifier = Modifier
) {
    val navActions = remember(navController) { RemindNavigationActions(navController) }

    NavHost(
        navController = navController,
        startDestination = startDestination.route,
        modifier = modifier
    ) {
        composable(RemindNavigation.Splash.route) {
            SplashScreen(onFinish = { destination ->
                navActions.navigateTo(destination, RemindNavigation.Splash)
            })
        }

        composable(RemindNavigation.Login.route) {
            LoginScreen(onLoginSuccess = {
                navActions.navigateTo(RemindNavigation.Main, RemindNavigation.Login)
            })
        }

        composable(RemindNavigation.Main.route) {
            MainScreen()
        }

        composable(RemindNavigation.Search.route) {
            SearchScreen()
        }

        composable(RemindNavigation.Home.route) {
            HomeScreen(navController)
        }

        composable(RemindNavigation.Record.route) {
            RecordScreen()
        }

        composable(RemindNavigation.MovieSearch.route) {
            MovieSearchScreen(navController)
        }

        composable(RemindNavigation.BookSearch.route) {
            BookSearchScreen(navController)
        }

        composable(RemindNavigation.MovieRegistration.route) {
            MovieRegistrationScreen(navController)
        }
    }
}

