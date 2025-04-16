package jo.remind.ui.splash

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth
import jo.remind.R
import jo.remind.ui.RemindNavigation
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onFinish: (RemindNavigation) -> Unit
) {
    val context = LocalContext.current
    val isLoggedIn = remember {
        FirebaseAuth.getInstance().currentUser != null
    }

    LaunchedEffect(Unit) {
        delay(1000)
        if (isLoggedIn) {
            onFinish(RemindNavigation.Main)
        } else {
            onFinish(RemindNavigation.Login)
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "logo",
            modifier = Modifier.size(196.dp)
        )
    }
}