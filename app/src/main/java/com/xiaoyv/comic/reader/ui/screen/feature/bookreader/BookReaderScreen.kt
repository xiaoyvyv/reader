package com.xiaoyv.comic.reader.ui.screen.feature.bookreader

import android.graphics.drawable.Drawable
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.integration.compose.rememberGlidePreloadingData
import com.github.panpf.zoomimage.GlideZoomAsyncImage
import com.xiaoyv.comic.datasource.book.BookDataSource
import com.xiaoyv.comic.datasource.book.BookModel
import com.xiaoyv.comic.datasource.book.BookPage
import com.xiaoyv.comic.datasource.book.remote.RemotePage
import com.xiaoyv.comic.reader.ui.component.ComicAppBar
import com.xiaoyv.comic.reader.ui.component.LazyList
import com.xiaoyv.comic.reader.ui.component.Loading
import com.xiaoyv.comic.reader.ui.component.PageStateScreen
import com.xiaoyv.comic.reader.ui.component.ScaffoldWrap
import com.xiaoyv.comic.reader.ui.component.WebViewWrap
import com.xiaoyv.comic.reader.ui.utils.addStateListener
import com.xiaoyv.comic.reader.ui.utils.debugLog
import kotlinx.coroutines.delay

/**
 * [BookReaderScreen]
 *
 * @author why
 * @since 4/29/24
 */
@Composable
fun BookReaderRoute(
    onNavUp: () -> Unit
) {
    val viewModel = viewModel<BookReaderViewModel>()

    val readerState by viewModel.uiState.collectAsStateWithLifecycle()

    BookReaderScreen(
        state = readerState,
        onNavUp = onNavUp
    )
}

@Composable
fun BookReaderScreen(
    state: BookReaderState,
    onNavUp: () -> Unit
) {
    val rememberAppBarVisibleState = remember { mutableStateOf(false) }

    ScaffoldWrap(
        topBar = {
            AnimatedVisibility(
                visible = rememberAppBarVisibleState.value,
                enter = slideInVertically(initialOffsetY = { -it }),
                exit = slideOutVertically(targetOffsetY = { -it })
            ) {
                ComicAppBar(
                    title = "阅读器",
                    onNavigationIconClick = onNavUp
                )
            }
        },
        bottomBar = {
            AnimatedVisibility(
                visible = rememberAppBarVisibleState.value,
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it })
            ) {
                BookReaderBottomBar()
            }
        }
    ) { padding ->
        debugLog { "Compose Scaffold Content" }

        // 状态图
        PageStateScreen(
            loadState = state.loadState,
            itemCount = { state.pages.size },
            onRetryClick = {}
        ) {
            BookReaderPager(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White),
                state = state,
                horizontal = false,
                onImageTap = {
                    rememberAppBarVisibleState.value = !rememberAppBarVisibleState.value
                }
            )
        }
    }
}

@Composable
fun BookReaderBottomBar() {
    BottomAppBar(
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(Icons.Filled.Check, contentDescription = "Localized description")
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Edit,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Mic,
                    contentDescription = "Localized description",
                )
            }
            IconButton(onClick = { /* do something */ }) {
                Icon(
                    Icons.Filled.Image,
                    contentDescription = "Localized description",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Add, "Localized description")
            }
        }

    )
}

/**
 * 上下|左右 切换
 */
@Composable
fun BookReaderPager(
    modifier: Modifier,
    state: BookReaderState,
    horizontal: Boolean,
    onImageTap: (Offset) -> Unit = {}
) {
    val pagerState = rememberPagerState { state.pages.size }
    if (horizontal) {
        HorizontalPager(
            modifier = modifier,
            state = pagerState,
            beyondBoundsPageCount = 2,
        ) {
            BookReaderItemContent(
                page = state.pages[it],
                index = it,
                onImageTap = onImageTap
            )
        }
    } else {
        VerticalPager(
            modifier = modifier,
            state = pagerState,
            beyondBoundsPageCount = 2,
        ) {
            BookReaderItemContent(
                page = state.pages[it],
                index = it,
                onImageTap = onImageTap
            )
        }
    }
}


/**
 * 条漫
 */
@Composable
fun BookReaderLong(
    modifier: Modifier,
    state: BookReaderState,
    onImageTap: (Offset) -> Unit = {}
) {
    val glidePreloadingData = rememberGlidePreloadingData(
        data = state.pages,
        preloadImageSize = Size(210f, 279f)
    ) { dataItem, requestBuilder ->
        requestBuilder.load(dataItem)
    }

    LazyList(modifier = modifier) {
        items(count = glidePreloadingData.size) {
            val (data, request) = glidePreloadingData[it]

            BookReaderItemContent(
                modifier = Modifier
                    .aspectRatio(if (data.pageRatio == 0f) 210 / 279f else data.pageRatio),
                page = data,
                index = it,
                preloadRequest = request,
                onImageTap = onImageTap
            )
        }
    }
}

/**
 * 每一页具体渲染实现
 */
@Composable
fun BookReaderItemContent(
    modifier: Modifier = Modifier,
    page: BookPage<BookModel, out BookDataSource<BookModel>>,
    index: Int,
    preloadRequest: RequestBuilder<Drawable>? = null,
    onImageTap: (Offset) -> Unit
) {
    Box(modifier = modifier) {
        val loading = remember { mutableStateOf(true) }
//
//        WebViewWrap(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(Color.Transparent),
//            url = page.glideModel().toString(),
//            onLoadStart = {
//                loading.value = true
//            },
//            onLoadFinish = {
//                loading.value = false
//            }
//        )

        GlideZoomAsyncImage(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Transparent),
            model = page.glideModel(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            transition = com.github.panpf.zoomimage.compose.glide.internal.CrossFade,
            onTap = onImageTap,
            requestBuilderTransform = {
                if (preloadRequest != null) {
                    it.thumbnail(preloadRequest)
                }

                it.addStateListener(loading)
            }
        )

        if (loading.value) Loading()
    }
}


/**
 * 远程数据的页面直接加载图片的 Url
 */
private fun Any.glideModel(): Any {
    if (this is RemotePage) {
        return renderPageUrl()
    }
    return this
}

@Composable
@Preview(widthDp = 411, heightDp = 700)
fun PreviewBookReaderScreen() {
    BookReaderScreen(
        state = BookReaderState(),
        onNavUp = {}
    )
}