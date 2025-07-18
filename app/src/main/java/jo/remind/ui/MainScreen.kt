package jo.remind.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import jo.remind.ui.home.HomeScreen
import jo.remind.ui.navigation.CustomBottomBar
import jo.remind.ui.record.BookRecordScreen
import jo.remind.ui.record.DailyRecordScreen
import jo.remind.ui.record.MovieRecordScreen
import jo.remind.ui.registration.BookRegistrationScreen
import jo.remind.ui.registration.MovieRegistrationScreen
import jo.remind.ui.search.BookSearchScreen
import jo.remind.ui.search.MovieSearchScreen
import java.time.LocalDate

@Composable
fun MainScreen() {
    val navController = rememberNavController()

    val currentBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackStackEntry.value?.destination?.route

    Scaffold(
        bottomBar = {
            CustomBottomBar(
                currentRoute = currentRoute,
                onItemClick = { item ->
                    if (currentRoute != item.route) {
                        navController.navigate(item.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = RemindNavigation.Home.route
        ) {
            composable(RemindNavigation.Home.route) {
                HomeScreen(navController, modifier = Modifier.padding(innerPadding))
            }
            composable(RemindNavigation.Search.route) {
                SearchScreen()
            }
            composable(RemindNavigation.Record.route) {
                RecordScreen()
            }
            composable(
                route = RemindNavigation.MovieSearch.route + "?date={date}",
                arguments = listOf(navArgument("date") {
                    type = NavType.StringType
                    defaultValue = LocalDate.now().toString()
                })
            ) {
                MovieSearchScreen(navController)
            }
            composable(
                route = RemindNavigation.BookSearch.route + "?date={date}",
                arguments = listOf(navArgument("date") {
                    type = NavType.StringType
                    defaultValue = LocalDate.now().toString()
                })
            ) {
                BookSearchScreen(navController)
            }
            composable(RemindNavigation.MovieRegistration.route) {
                MovieRegistrationScreen(navController)
            }
            composable(RemindNavigation.BookRegistration.route) {
                BookRegistrationScreen(navController)
            }
            composable(RemindNavigation.MovieRecord.route) {
                MovieRecordScreen(navController)
            }
            composable(RemindNavigation.BookRecord.route) {
                BookRecordScreen(navController)
            }
            composable(
                route = RemindNavigation.DailyRecord.route + "?date={date}",
                arguments = listOf(navArgument("date") {
                    type = NavType.StringType
                    defaultValue = LocalDate.now().toString()
                })
            ) {
                DailyRecordScreen(navController)
            }
        }
    }
}

@Composable
fun SearchScreen() {
    Text("🔍 검색 화면")
}

@Composable
fun RecordScreen() {
    Text("📝 기록 화면")
}
