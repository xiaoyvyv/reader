package com.xiaoyv.comic.reader.ui.screen.main.profile

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaoyv.comic.reader.ui.component.ComicAppBar
import com.xiaoyv.comic.reader.ui.component.ScaffoldWrap

/**
 * [ProfileTabRoute]
 *
 * @author why
 * @since 5/4/24
 */

@Composable
fun ProfileTabRoute(navigationType: Int) {
    val viewModel = viewModel<ProfileTabViewModel>()
    ProfileTabScreen()
}

@Composable
fun ProfileTabScreen() {
    ScaffoldWrap(
        topBar = {
            ComicAppBar(
                title = "个人中心",
                hideNavigationIcon = true
            )
        }
    ) {

    }
}


@Preview(widthDp = 411, heightDp = 700)
@Composable
fun PreviewLocalTabScreen() {
    ProfileTabScreen()
}