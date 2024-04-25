/*
 * Copyright 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.xiaoyv.comic.reader.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.xiaoyv.comic.i18n.RS
import com.xiaoyv.comic.reader.R

object ComicRoute {
    const val ROUTE_BOOKSHELF = "Bookshelf"
    const val ROUTE_PROFILE = "Profile"
    const val ROUTE_SETTING = "Setting"
}

data class ComicTopLevelDestination(
    val route: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val iconTextId: Int
)

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
        route = ComicRoute.ROUTE_BOOKSHELF,
        selectedIcon = Icons.Default.Home,
        unselectedIcon = Icons.Default.Home,
        iconTextId = RS.tab_bookshelf
    ),
    ComicTopLevelDestination(
        route = ComicRoute.ROUTE_SETTING,
        selectedIcon = Icons.Default.Settings,
        unselectedIcon = Icons.Default.Settings,
        iconTextId = RS.tab_setting
    ),
    ComicTopLevelDestination(
        route = ComicRoute.ROUTE_PROFILE,
        selectedIcon = Icons.Default.AccountCircle,
        unselectedIcon = Icons.Default.AccountCircle,
        iconTextId = RS.tab_profile
    )
)
