package jo.remind.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import jo.remind.R
import jo.remind.ui.RemindNavigation
import jo.remind.ui.component.CalendarScreen
import jo.remind.ui.component.RecordTypeDialog

@Composable
fun HomeScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    var showDialog by remember { mutableStateOf(false) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        CalendarScreen()

        FloatingActionButton(
            onClick = { showDialog = true },
            containerColor = MaterialTheme.colorScheme.onPrimary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            shape = RoundedCornerShape(50),
            elevation = FloatingActionButtonDefaults.elevation(0.dp),
            modifier = Modifier
                .size(60.dp)
                .align(Alignment.BottomEnd)
        ) {
            Image(
                painter = painterResource(id = R.drawable.pen),
                contentDescription = "FAB Image",
                modifier = Modifier.size(24.dp)
            )
        }

        if (showDialog) {
            RecordTypeDialog(
                onDismiss = { showDialog = false },
                onSelect = { selected ->
                    showDialog = false
                    if (selected == "영화 기록하기") {
                        navController.navigate(RemindNavigation.MovieSearch.route)
                    }
                    if (selected == "책 기록하기") {
                        navController.navigate(RemindNavigation.BookSearch.route)
                    }
                }
            )
        }
    }
}
