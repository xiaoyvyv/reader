package com.xiaoyv.comic.reader.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.xiaoyv.comic.i18n.RS

object ComicRoute {
    const val ROUTE_TAB_HOME = "Home"
    const val ROUTE_TAB_PROFILE = "Profile"
    const val ROUTE_TAB_SETTING = "Setting"

    const val ROUTE_MAIN = "Main"
    const val ROUTE_READER = "Reader"

    val homeTabs = listOf(ROUTE_TAB_HOME, ROUTE_TAB_PROFILE, ROUTE_TAB_SETTING)
}

data class ComicTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

enum class ComicNavAction {
    PUSH,
    POP,
    REPLACE,
}

class ComicNavigationActions(private val navController: NavHostController) {

    fun navigateTo(destination: ComicTopLevelDestination) {
        navController.navigate(destination.route) {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }
    }
}

val TOP_LEVEL_DESTINATIONS = listOf(
    ComicTopLevelDestination(
        route = ComicRoute.ROUTE_TAB_HOME,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Default.Home,
        iconTextId = RS.tab_bookshelf
    ),
    ComicTopLevelDestination(
        route = ComicRoute.ROUTE_TAB_SETTING,
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Default.Settings,
        iconTextId = RS.tab_setting
    ),
    ComicTopLevelDestination(
        route = ComicRoute.ROUTE_TAB_PROFILE,
        selectedIcon = Icons.Default.AccountCircle,
        unselectedIcon = Icons.Default.AccountCircle,
        iconTextId = RS.tab_profile
    )
)
