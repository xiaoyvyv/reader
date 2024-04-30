package com.xiaoyv.comic.reader

import android.annotation.SuppressLint
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.window.layout.DisplayFeature
import androidx.window.layout.FoldingFeature
import com.xiaoyv.comic.reader.config.types.ContentType
import com.xiaoyv.comic.reader.config.types.NavigationType
import com.xiaoyv.comic.reader.navigation.ComicNavAction
import com.xiaoyv.comic.reader.navigation.ComicNavigationActions
import com.xiaoyv.comic.reader.navigation.ComicNavigationRail
import com.xiaoyv.comic.reader.navigation.ComicRoute
import com.xiaoyv.comic.reader.navigation.ComicTopLevelDestination
import com.xiaoyv.comic.reader.navigation.MotionConstants
import com.xiaoyv.comic.reader.navigation.TOP_LEVEL_DESTINATIONS
import com.xiaoyv.comic.reader.navigation.materialSharedAxisXIn
import com.xiaoyv.comic.reader.navigation.materialSharedAxisXOut
import com.xiaoyv.comic.reader.navigation.rememberSlideDistance
import com.xiaoyv.comic.reader.ui.screen.feature.reader.ReaderScreen
import com.xiaoyv.comic.reader.ui.screen.main.MainScreen
import com.xiaoyv.comic.reader.ui.utils.DevicePosture
import com.xiaoyv.comic.reader.ui.utils.debugLog
import com.xiaoyv.comic.reader.ui.utils.isBookPosture
import com.xiaoyv.comic.reader.ui.utils.isSeparating
import kotlinx.coroutines.launch

/**
 * [MainPage]
 *
 * @author why
 * @since 3/4/24
 */
@Composable
fun MainPage(
    windowSize: WindowSizeClass,
    displayFeatures: List<DisplayFeature>,
) {
    // 判断折叠屏状态
    val foldingFeature = displayFeatures.filterIsInstance<FoldingFeature>().firstOrNull()
    val foldingDevicePosture = when {
        isBookPosture(foldingFeature) ->
            DevicePosture.BookPosture(foldingFeature.bounds)

        isSeparating(foldingFeature) ->
            DevicePosture.Separating(foldingFeature.bounds, foldingFeature.orientation)

        else -> DevicePosture.NormalPosture
    }

    // 不同尺寸适配使用不同的导航栏
    @NavigationType val navigationType: Int
    @ContentType val contentType: Int

    when (windowSize.widthSizeClass) {
        // 小屏底部导航栏
        WindowWidthSizeClass.Compact -> {
            navigationType = NavigationType.TYPE_BOTTOM_NAVIGATION
            contentType = ContentType.TYPE_SINGLE_PANE
        }

        // 中屏幕左侧窄导航栏
        WindowWidthSizeClass.Medium -> {
            navigationType = NavigationType.TYPE_NAVIGATION_RAIL
            contentType = if (foldingDevicePosture != DevicePosture.NormalPosture) {
                ContentType.TYPE_DUAL_PANE
            } else {
                ContentType.TYPE_SINGLE_PANE
            }
        }

        // 大屏幕左侧宽导航栏
        WindowWidthSizeClass.Expanded -> {
            navigationType = if (foldingDevicePosture is DevicePosture.BookPosture) {
                NavigationType.TYPE_NAVIGATION_RAIL
            } else {
                NavigationType.TYPE_PERMANENT_NAVIGATION_DRAWER
            }
            contentType = ContentType.TYPE_DUAL_PANE
        }

        else -> {
            navigationType = NavigationType.TYPE_BOTTOM_NAVIGATION
            contentType = ContentType.TYPE_SINGLE_PANE
        }
    }

    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val navController = rememberNavController()
    val navigationActions = remember(navController) {
        ComicNavigationActions(navController)
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val selectedDestination = navBackStackEntry?.destination?.route ?: ComicRoute.ROUTE_MAIN

    // 抽屉适配
    if (navigationType == NavigationType.TYPE_PERMANENT_NAVIGATION_DRAWER) {
        // 大屏左侧固定宽导航栏
        PermanentNavigationDrawer(
            drawerContent = {
                // 平板固定式抽屉菜单
                PermanentDrawerSheet {
                    MainPageDrawerItems(
                        selectedDestination = selectedDestination,
                        navigateToTopLevelDestination = navigationActions::navigateTo
                    )
                }
            }
        ) {
            // 平板抽屉布局主内容
            MainActivityPageContent(
                navigationType = navigationType,
                navController = navController,
                displayFeatures = displayFeatures,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo
            )
        }
    } else {
        ModalNavigationDrawer(
            drawerState = drawerState,
            gesturesEnabled = false,
            drawerContent = {
                // 移动端抽屉菜单
                ModalDrawerSheet {
                    MainPageDrawerItems(
                        selectedDestination = selectedDestination,
                        navigateToTopLevelDestination = navigationActions::navigateTo,
                        onDrawerClicked = {
                            scope.launch {
                                drawerState.close()
                            }
                        }
                    )
                }
            }
        ) {
            // 移动端抽屉布局主内容
            MainActivityPageContent(
                navigationType = navigationType,
                navController = navController,
                displayFeatures = displayFeatures,
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigationActions::navigateTo,
                onDrawerClicked = {
                    scope.launch {
                        drawerState.open()
                    }
                }
            )
        }
    }
}

@Composable
fun MainPageDrawerItems(
    selectedDestination: String,
    navigateToTopLevelDestination: (ComicTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    TOP_LEVEL_DESTINATIONS.forEach { destination ->
        NavigationDrawerItem(
            selected = selectedDestination == destination.route,
            label = {
                Text(
                    text = stringResource(id = destination.iconTextId),
                    modifier = Modifier.padding(horizontal = 16.dp)
                )
            },
            icon = {
                Icon(
                    imageVector = destination.selectedIcon,
                    contentDescription = stringResource(id = destination.iconTextId)
                )
            },
            colors = NavigationDrawerItemDefaults.colors(
                unselectedContainerColor = Color.Transparent
            ),
            onClick = { navigateToTopLevelDestination(destination) },
        )
    }
}

@Composable
fun MainActivityPageContent(
    modifier: Modifier = Modifier,
    @NavigationType navigationType: Int,
    navController: NavHostController,
    displayFeatures: List<DisplayFeature>,
    selectedDestination: String,
    navigateToTopLevelDestination: (ComicTopLevelDestination) -> Unit,
    onDrawerClicked: () -> Unit = {}
) {
    Row(modifier = modifier.fillMaxSize()) {
        // 针对中屏幕配置左侧窄导航栏
        AnimatedVisibility(visible = navigationType == NavigationType.TYPE_NAVIGATION_RAIL) {
            ComicNavigationRail(
                selectedDestination = selectedDestination,
                navigateToTopLevelDestination = navigateToTopLevelDestination,
                onDrawerClicked = onDrawerClicked,
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            MainActivityPageNavHost(
                navController = navController,
                displayFeatures = displayFeatures,
                navigationType = navigationType,
                modifier = Modifier.weight(1f),
            )
        }
    }
}


@SuppressLint("RestrictedApi")
@Composable
fun MainActivityPageNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    displayFeatures: List<DisplayFeature>,
    @NavigationType navigationType: Int,
//    closeDetailScreen: () -> Unit,
//    navigateToDetail: (Long, ReplyContentType) -> Unit,
) {

    // 记录导航的事件行为
    var backStackCount by remember { mutableIntStateOf(0) }
    var lastAction by remember { mutableStateOf(ComicNavAction.PUSH) }
    val currentBackStack by navController.currentBackStack.collectAsState()

    // 根据回退堆栈数目判断
    LaunchedEffect(navController) {
        snapshotFlow { currentBackStack.size }.collect {
            lastAction = when {
                it > backStackCount -> ComicNavAction.PUSH
                it < backStackCount -> ComicNavAction.POP
                else -> ComicNavAction.REPLACE
            }
            backStackCount = it
        }
    }

    val slideDistance = rememberSlideDistance()

    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = ComicRoute.ROUTE_MAIN,
        enterTransition = {
            materialSharedAxisXIn(
                forward = lastAction != ComicNavAction.POP,
                slideDistance = slideDistance,
                durationMillis = MotionConstants.DefaultMotionDuration,
            )
        },
        exitTransition = {
            materialSharedAxisXOut(
                forward = lastAction != ComicNavAction.POP,
                slideDistance = slideDistance,
                durationMillis = MotionConstants.DefaultMotionDuration,
            )
        }
    ) {
        // 主页
        composable(ComicRoute.ROUTE_MAIN) {
            MainScreen(
                navigationType = navigationType,
                onNavUp = navController::navigateUp,
                onNavTo = navController::navigate,
            )
        }

        // 阅读页
        composable(ComicRoute.ROUTE_READER) {
            ReaderScreen(
                onNavUp = navController::navigateUp,
                onNavTo = navController::navigate,
            )
        }
    }
}