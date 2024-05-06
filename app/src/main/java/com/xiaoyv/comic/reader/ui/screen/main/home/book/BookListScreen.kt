package com.xiaoyv.comic.reader.ui.screen.main.home.book

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.bumptech.glide.integration.compose.GlideImage
import com.xiaoyv.comic.reader.config.types.FeedType
import com.xiaoyv.comic.reader.data.entity.BookSeriesEntity
import com.xiaoyv.comic.reader.ui.component.Loading
import com.xiaoyv.comic.reader.ui.component.PageStateScreen
import com.xiaoyv.comic.reader.ui.component.ScaffoldWrap
import com.xiaoyv.comic.reader.ui.utils.isStoped

/**
 * [BookListRoute]
 *
 * @author why
 * @since 4/26/24
 */
@Composable
fun BookListRoute(
) {
    val viewModel = viewModel<BookListViewModel>()
    val pagingItems = viewModel.bookList.collectAsLazyPagingItems()

    BookListScreen(
        viewModel = viewModel,
        pagingItems = pagingItems
    )
}

@Composable
private fun BookListScreen(
    viewModel: BookListViewModel,
    pagingItems: LazyPagingItems<BookSeriesEntity>
) {
    val listType by viewModel.listType.collectAsStateWithLifecycle()
    val refreshState = rememberPullToRefreshState()
    if (refreshState.isRefreshing) {
        LaunchedEffect(Unit) {
            pagingItems.refresh()
        }
    }

    LaunchedEffect(pagingItems.loadState.refresh) {
        if (pagingItems.loadState.refresh.isStoped) {
            refreshState.endRefresh()
        }
    }

    val count = when (listType) {
        FeedType.List -> 1
        FeedType.LargeGrid -> 2
        FeedType.SmallGrid -> 3
    }

    ScaffoldWrap(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(refreshState.nestedScrollConnection)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(count),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(8.dp)
            ) {
                items(count = pagingItems.itemCount) {
                    val model = pagingItems[it]
                    BookListScreenItem(model, it)
                }

                val append = pagingItems.loadState.append
                when (append) {
                    // 加载更多中...
                    is LoadState.Loading -> {
                        item {
                            Loading(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp)
                            )
                        }
                    }
                    // 加载更多失败
                    is LoadState.Error -> {
                        item {
                            TextButton(
                                modifier = Modifier.align(Alignment.Center),
                                onClick = { pagingItems.retry() }
                            ) {
                                Text(text = "Click Retry")
                            }
                        }
                    }

                    else -> {}
                }
            }

            // 状态视图绑定
            PageStateScreen(
                pagingItems = pagingItems,
                refreshState = refreshState
            )

            // 下拉刷新
            PullToRefreshContainer(
                modifier = Modifier.align(Alignment.TopCenter),
                state = refreshState,
            )
        }
    }
}

@Composable
fun BookListScreenItem(
    entity: BookSeriesEntity?,
//    request: RequestBuilder<Drawable>,
    index: Int
) {

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        val maxWidth = maxWidth

        ConstraintLayout(
            constraintSet = ConstraintSet {
                val ivCover = createRefFor("ivCover")
                val tvTitle = createRefFor("tvTitle")

                constrain(ivCover) {
                    top.linkTo(parent.top)
                }
                constrain(tvTitle) {
                    top.linkTo(ivCover.bottom, 8.dp)
                }
            }
        ) {
            ElevatedCard(
                modifier = Modifier
                    .layoutId("ivCover")
                    .fillMaxWidth()
                    .height(150.dp),
                onClick = {

                }
            ) {
                GlideImage(
                    modifier = Modifier.fillMaxSize(),
                    model = entity?.model,
                    contentDescription = "",
                    contentScale = ContentScale.Crop
                )
            }

            Text(
                text = entity?.name.orEmpty(),
                modifier = Modifier.layoutId("tvTitle"),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
