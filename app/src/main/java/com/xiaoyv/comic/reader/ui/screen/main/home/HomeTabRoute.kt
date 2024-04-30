package com.xiaoyv.comic.reader.ui.screen.main.home

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaoyv.comic.reader.config.types.NavigationType


@Composable
fun HomeTabRoute(
    @NavigationType navigationType: Int,
    onNavUp: () -> Unit,
    onNavTo: (String) -> Unit
) {
    val viewModel = viewModel<HomeTabViewModel>()

    HomeTabScreen(viewModel, onNavUp, onNavTo)
}