package com.xiaoyv.comic.reader.ui.screen.main.home.bookshelf

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel

/**
 * [BookShelfScreen]
 *
 * @author why
 * @since 4/26/24
 */
@Composable
fun BookShelfScreen(
    onNavTo: (String) -> Unit
) {
    BookShelfScreen(viewModel = viewModel())
}

@Composable
private fun BookShelfScreen(viewModel: BookShelfViewModel) {
    LazyColumn(modifier = Modifier.fillMaxWidth()) {

    }
}