package com.xiaoyv.comic.reader.ui.screen.main.local

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.xiaoyv.comic.reader.data.entity.FileEntity
import com.xiaoyv.comic.reader.ui.component.ComicAppBar
import com.xiaoyv.comic.reader.ui.component.LazyList
import com.xiaoyv.comic.reader.ui.component.PageStateScreen
import com.xiaoyv.comic.reader.ui.component.ScaffoldWrap
import com.xiaoyv.comic.reader.ui.screen.main.home.file.BookFileScreenBookItem
import com.xiaoyv.comic.reader.ui.utils.debugLog
import com.xiaoyv.comic.reader.ui.utils.isStoped
import com.xiaoyv.comic.reader.ui.utils.toJson
import java.io.File

/**
 * [LocalTabRoute]
 *
 * @author why
 * @since 5/4/24
 */

@Composable
fun LocalTabRoute(
    navigationType: Int,
    onBookFileClick: (FileEntity) -> Unit = {}
) {
    val viewModel = viewModel<LocalTabViewModel>()

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LocalTabScreen(
        state = state,
        onRefresh = { viewModel.refresh() },
        onBookFileClick = onBookFileClick
    )
}

@Composable
fun LocalTabScreen(
    state: LocalTabState,
    onRefresh: () -> Unit = {},
    onBookFileClick: (FileEntity) -> Unit = {}
) {
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            onRefresh()
        }
    }

    LaunchedEffect(state.loadState) {
        if (state.loadState.isStoped) {
            refreshState.endRefresh()
        }
    }

    ScaffoldWrap(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(refreshState.nestedScrollConnection),
        topBar = {
            ComicAppBar(
                title = "本地文件库",
                hideNavigationIcon = true
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = padding.calculateTopPadding())
        ) {
            LazyList(Modifier.fillMaxSize()) {
                items(state.books) {
                    BookFileScreenBookItem(
                        fileEntity = it,
                        index = 1,
                        onEnterDir = {},
                        onBookFileClick = onBookFileClick
                    )
                }
            }

            // 下拉刷新
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState,
            )
        }
    }
}


@Preview(widthDp = 411, heightDp = 700)
@Composable
fun PreviewLocalTabScreen() {
    LocalTabScreen(LocalTabState())
}