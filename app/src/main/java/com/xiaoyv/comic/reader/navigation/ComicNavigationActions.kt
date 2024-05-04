package com.xiaoyv.comic.reader.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.WifiTethering
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.xiaoyv.comic.i18n.RS

object ComicRoute {
    const val ROUTE_TAB_HOME = "Home"
    const val ROUTE_TAB_LOCAL = "Local"
    const val ROUTE_TAB_REMOTE = "Remote"
    const val ROUTE_TAB_PROFILE = "Profile"

    const val ROUTE_MAIN = "Main"
    const val ROUTE_READER = "Reader"
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
        selectedIcon = Icons.Default.Bookmarks,
        unselectedIcon = Icons.Default.Bookmarks,
        iconTextId = RS.tab_bookshelf
    ),
    ComicTopLevelDestination(
        route = ComicRoute.ROUTE_TAB_LOCAL,
        selectedIcon = Icons.Default.LocalFireDepartment,
        unselectedIcon = Icons.Default.LocalFireDepartment,
        iconTextId = RS.tab_local
    ),
    ComicTopLevelDestination(
        route = ComicRoute.ROUTE_TAB_REMOTE,
        selectedIcon = Icons.Default.WifiTethering,
        unselectedIcon = Icons.Default.WifiTethering,
        iconTextId = RS.tab_remote
    ),
    ComicTopLevelDestination(
        route = ComicRoute.ROUTE_TAB_PROFILE,
        selectedIcon = Icons.Default.AccountCircle,
        unselectedIcon = Icons.Default.AccountCircle,
        iconTextId = RS.tab_profile
    )
)
