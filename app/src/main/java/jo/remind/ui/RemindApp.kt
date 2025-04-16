package jo.remind.ui

import androidx.compose.runtime.Composable

@Composable
fun RemindApp(
    startDestination: RemindNavigation
) {
    RemindNavGraph(startDestination = startDestination)
}