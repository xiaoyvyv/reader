package com.xiaoyv.comic.reader.ui.screen.feature.reader

import android.app.Application
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaoyv.comic.reader.ui.component.ComicAppBar
import com.xiaoyv.comic.reader.ui.component.ScaffoldWrap

/**
 * [ReaderScreen]
 *
 * @author why
 * @since 4/29/24
 */
@Composable
fun ReaderScreen(
    onNavUp: () -> Unit,
    onNavTo: (String) -> Unit
) {
    val viewModel = viewModel<ReaderViewModel>()

    ReaderScreen(
        viewModel = viewModel,
        onNavUp = onNavUp
    )
}

@Composable
fun ReaderScreen(
    viewModel: ReaderViewModel,
    onNavUp: () -> Unit,
) {
    ScaffoldWrap(
        topBar = {
            ComicAppBar(
                title = "阅读器",
                onNavigationIconClick = onNavUp
            )
        }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {

        }
    }
}

@Composable
@Preview(widthDp = 411, heightDp = 700)
fun PreviewReaderScreen() {
    ReaderScreen(viewModel = ReaderViewModel(Application()), onNavUp = {})
}