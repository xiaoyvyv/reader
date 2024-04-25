package com.xiaoyv.comic.reader.navigation

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource

/**
 * 平板模式下，左侧导航栏
 */
@Composable
fun ComicNavigationRail(
    selectedDestination: String,
    navigateToTopLevelDestination: (ComicTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {},
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.inverseOnSurface
    ) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationRailItem(
                selected = selectedDestination == destination.route,
                onClick = { navigateToTopLevelDestination(destination) },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(
                            id = destination.iconTextId
                        )
                    )
                }
            )
        }
    }
}

/**
 * 移动端模式下，底部导航栏
 */
@Composable
fun ComicBottomNavigationBar(
    selectedDestination: String,
    navigateToTopLevelDestination: (ComicTopLevelDestination) -> Unit
) {
    NavigationBar(modifier = Modifier.fillMaxWidth()) {
        TOP_LEVEL_DESTINATIONS.forEach { destination ->
            NavigationBarItem(
                selected = selectedDestination == destination.route,
                alwaysShowLabel = true,
                label = {
                    Text(
                        text = stringResource(id = destination.iconTextId)
                    )
                },
                icon = {
                    Icon(
                        imageVector = destination.selectedIcon,
                        contentDescription = stringResource(id = destination.iconTextId)
                    )
                },
                onClick = { navigateToTopLevelDestination(destination) }
            )
        }
    }
}
