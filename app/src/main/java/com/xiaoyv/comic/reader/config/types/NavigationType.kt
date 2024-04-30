package com.xiaoyv.comic.reader.config.types

import androidx.annotation.IntDef
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer

@IntDef(
    NavigationType.TYPE_BOTTOM_NAVIGATION,
    NavigationType.TYPE_NAVIGATION_RAIL,
    NavigationType.TYPE_PERMANENT_NAVIGATION_DRAWER,
)
@Retention(AnnotationRetention.SOURCE)
annotation class NavigationType {
    companion object {
        /**
         * 小屏幕底部导航栏 [ModalNavigationDrawer] [NavigationBar] 实现
         */
        const val TYPE_BOTTOM_NAVIGATION = 1

        /**
         * 中屏幕左侧窄导航栏 [ModalNavigationDrawer] [NavigationRail] 实现
         */
        const val TYPE_NAVIGATION_RAIL = 2

        /**
         * 大屏幕左侧宽导航栏 [PermanentNavigationDrawer] [PermanentDrawerSheet] 实现
         */
        const val TYPE_PERMANENT_NAVIGATION_DRAWER = 3
    }
}