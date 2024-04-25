package com.xiaoyv.comic.reader.ui.page.bookshelf

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaoyv.comic.reader.config.ann.NavigationType


@Composable
fun BookShelfRoute(
    @NavigationType navigationType: Int,
    onNavUp: () -> Unit
) {
    val viewModel = viewModel<BookShelfViewModel>()

    BackHandler {
        onNavUp()
    }

    BookShelfScreen(viewModel)
}