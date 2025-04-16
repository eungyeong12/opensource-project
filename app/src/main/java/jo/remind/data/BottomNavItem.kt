package jo.remind.data

import androidx.annotation.DrawableRes

data class BottomNavItem(
    val route: String,
    @DrawableRes val iconRes: Int,
    val label: String
)