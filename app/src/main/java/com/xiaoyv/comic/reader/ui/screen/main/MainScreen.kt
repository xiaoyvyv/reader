package com.xiaoyv.comic.reader.ui.screen.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.xiaoyv.comic.reader.config.types.NavigationType
import com.xiaoyv.comic.reader.navigation.ComicBottomNavigationBar
import com.xiaoyv.comic.reader.navigation.ComicRoute
import com.xiaoyv.comic.reader.navigation.MotionConstants
import com.xiaoyv.comic.reader.navigation.materialFadeThroughIn
import com.xiaoyv.comic.reader.navigation.materialFadeThroughOut
import com.xiaoyv.comic.reader.ui.screen.main.home.HomeTabRoute

/**
 * [MainScreen]
 *
 * @author why
 * @since 4/30/24
 */
@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    @NavigationType navigationType: Int,
    onNavUp: () -> Unit,
    onNavTo: (String) -> Unit
) {
    val tabNavController = rememberNavController()
    val navBackStackEntry by tabNavController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: ComicRoute.ROUTE_MAIN

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.surface)
    ) {
        NavHost(
            modifier = Modifier.weight(1f),
            navController = tabNavController,
            startDestination = ComicRoute.ROUTE_TAB_HOME,
            enterTransition = {
                materialFadeThroughIn(
                    initialScale = 1f,
                    durationMillis = MotionConstants.DefaultFadeInDuration
                )
            },
            exitTransition = {
                materialFadeThroughOut(durationMillis = MotionConstants.DefaultFadeOutDuration)
            }
        ) {
            // 首页
            composable(ComicRoute.ROUTE_TAB_HOME) {
                HomeTabRoute(
                    navigationType = navigationType,
                    onNavUp = onNavUp,
                    onNavTo = onNavTo
                )
            }
            // 个人中心
            composable(ComicRoute.ROUTE_TAB_PROFILE) {
                Text(text = "你好")
            }

            // 设置
            composable(ComicRoute.ROUTE_TAB_SETTING) {
                Text(text = "你好")
            }
        }

        AnimatedVisibility(visible = navigationType == NavigationType.TYPE_BOTTOM_NAVIGATION) {
            ComicBottomNavigationBar(
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = {
                    tabNavController.navigate(it.route) {
                        popUpTo(tabNavController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}