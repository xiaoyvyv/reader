package com.xiaoyv.comic.reader.ui.screen.main.local

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaoyv.comic.reader.ui.component.ComicAppBar
import com.xiaoyv.comic.reader.ui.component.EmptyNavigationIcon
import com.xiaoyv.comic.reader.ui.component.ScaffoldWrap

/**
 * [LocalTabRoute]
 *
 * @author why
 * @since 5/4/24
 */

@Composable
fun LocalTabRoute(navigationType: Int) {
    val viewModel = viewModel<LocalTabViewModel>()

}

@Composable
fun LocalTabScreen() {
    ScaffoldWrap(
        topBar = {
            ComicAppBar(
                title = "本地文件库",
                hideNavigationIcon = true
            )
        }
    ) {


    }
}


@Preview(widthDp = 411, heightDp = 700)
@Composable
fun PreviewLocalTabScreen() {
    LocalTabScreen()
}