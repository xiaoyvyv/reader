package com.xiaoyv.comic.reader.ui.screen.main.remote

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import com.xiaoyv.comic.datasource.remote.RemoteBookSeriesEntity
import com.xiaoyv.comic.datasource.remote.RemoteLibraryEntity
import com.xiaoyv.comic.reader.ui.component.ComicAppBar
import com.xiaoyv.comic.reader.ui.component.PageStateScreen
import com.xiaoyv.comic.reader.ui.component.ScaffoldWrap
import com.xiaoyv.comic.reader.ui.component.StringLabelPage
import com.xiaoyv.comic.reader.ui.component.TopBarOverflowMenu
import com.xiaoyv.comic.reader.ui.screen.main.remote.page.RemoteTabPageRoute
import kotlinx.coroutines.launch

/**
 * [RemoteTabScreen]
 *
 * @author why
 * @since 5/4/24
 */

@Composable
fun RemoteTabRoute(
    navigationType: Int,
    onBookClick: (RemoteBookSeriesEntity) -> Unit
) {
    val viewModel = viewModel<RemoteTabViewModel>()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    RemoteTabScreen(
        uiState = uiState,
        onBookClick = onBookClick
    )
}

@Composable
fun RemoteTabScreen(
    uiState: RemoteTabViewState,
    onBookClick: (RemoteBookSeriesEntity) -> Unit
) {
    ScaffoldWrap(
        topBar = {
            ComicAppBar(
                title = "远程资源库",
                hideNavigationIcon = true,
                actions = {
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.AddCircleOutline,
                            contentDescription = "添加",
                        )
                    }

                    TopBarOverflowMenu(
                        items = remember { listOf("帮助", "关于") },
                        onMenuItemClick = {

                        }
                    )
                }
            )
        }
    ) {
        PageStateScreen(loadState = uiState.loadState) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = it.calculateTopPadding())
            ) {
                val scope = rememberCoroutineScope()
                val pagerState = rememberPagerState(pageCount = { uiState.tabs.size })
                val tabs = remember {
                    uiState.tabs.map {
                        StringLabelPage(
                            label = it.title,
                            content = {
                                RemoteTabPageRoute(
                                    entity = it,
                                    onBookClick = onBookClick
                                )
                            }
                        )
                    }
                }

                SecondaryTabRow(selectedTabIndex = pagerState.currentPage) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = index == pagerState.currentPage,
                            onClick = {
                                scope.launch {
                                    pagerState.animateScrollToPage(index)
                                }
                            },
                            text = {
                                Text(
                                    text = title.label,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        )
                    }
                }

                HorizontalPager(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    state = pagerState,
                    beyondBoundsPageCount = 0
                ) { page ->
                    tabs[page].content()
                }
            }
        }
    }
}


@Preview(widthDp = 411, heightDp = 700)
@Composable
fun PreviewLocalTabScreen() {
    RemoteTabScreen(
        uiState = RemoteTabViewState(
            loadState = LoadState.NotLoading(true),
            tabs = listOf(
                RemoteLibraryEntity(title = "Tab1", id = "id1"),
                RemoteLibraryEntity(title = "Tab2", id = "id2"),
                RemoteLibraryEntity(title = "Tab3", id = "id3")
            )
        ),
        onBookClick = {}
    )
}