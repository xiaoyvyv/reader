package com.xiaoyv.comic.reader.ui.screen.feature.bookseries

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.xiaoyv.comic.datasource.book.BookModel
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteSeriesInfo
import com.xiaoyv.comic.datasource.series.SeriesBook
import com.xiaoyv.comic.reader.R
import com.xiaoyv.comic.reader.ui.component.ComicAppBar
import com.xiaoyv.comic.reader.ui.component.ImageViewer
import com.xiaoyv.comic.reader.ui.component.LocalPopupHostState
import com.xiaoyv.comic.reader.ui.component.PageStateScreen
import com.xiaoyv.comic.reader.ui.component.ScaffoldWrap

/**
 * [BookSeriesScreen]
 *
 * @author why
 * @since 4/29/24
 */
@Composable
fun BookSeriesRoute(
    onNavUp: () -> Unit,
    onBookClick: (BookModel) -> Unit
) {
    val viewModel = viewModel<BookSeriesViewModel>()

    val readerState by viewModel.uiState.collectAsStateWithLifecycle()

    BookSeriesScreen(
        state = readerState,
        onNavUp = onNavUp,
        onBookClick = onBookClick
    )
}

@Composable
fun BookSeriesScreen(
    state: BookSeriesState,
    onNavUp: () -> Unit,
    onBookClick: (BookModel) -> Unit
) {

    ScaffoldWrap(
        topBar = {
            ComicAppBar(
                title = "系列详情",
                colors = TopAppBarDefaults
                    .topAppBarColors()
                    .copy(containerColor = Color.Transparent),
                onNavigationIconClick = onNavUp,
            )
        }
    ) { padding ->
        val popupHostState = LocalPopupHostState.current

        PageStateScreen(loadState = state.loadState) {
            requireNotNull(state.seriesInfo)

            Column {
                // Header
                BookSeriesHeader(
                    modifier = Modifier.padding(top = padding.calculateTopPadding()),
                    seriesinfo = state.seriesInfo,
                    onCoverClick = {
                        popupHostState.show {
                            ImageViewer(
                                item = state.seriesInfo.cover,
                                onImageTap = {
                                    popupHostState.hide()
                                }
                            )
                        }
                    }
                )

                Text(
                    text = "全部内容", Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.SemiBold
                )

                // Content
                BookSeriesContent(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .padding(
                            bottom = padding.calculateBottomPadding(),
                            top = 8.dp,
                            start = 8.dp,
                            end = 8.dp
                        ),
                    seriesinfo = state.seriesInfo,
                    onBookClick = onBookClick
                )
            }
        }
    }
}


/**
 * 顶部块
 */
@Composable
fun BookSeriesHeader(
    modifier: Modifier,
    seriesinfo: RemoteSeriesInfo,
    onCoverClick: () -> Unit
) {
    ConstraintLayout {
        val sufaceColor = MaterialTheme.colorScheme.surface
        val gradientColors = remember {
            listOf(
                sufaceColor.copy(alpha = 0.5f),
                sufaceColor.copy(alpha = 1f),
            )
        }
        val (background, space, cover, title, info) = createRefs()

        GlideImage(
            modifier = Modifier
                .fillMaxWidth()
                .constrainAs(background) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(parent.bottom)
                    height = Dimension.fillToConstraints
                }
                .drawWithContent {
                    drawContent()
                    drawRect(
                        brush = Brush.verticalGradient(
                            colors = gradientColors,
                            startY = 0f,
                            endY = size.height
                        )
                    )
                },
            model = seriesinfo.cover,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = placeholder(R.drawable.ic_placeholder),
            transition = CrossFade
        )

        Spacer(modifier = modifier.constrainAs(space) {
            top.linkTo(parent.top)
        })

        ElevatedCard(
            modifier = Modifier.constrainAs(cover) {
                width = Dimension.value(100.dp)
                height = Dimension.ratio("3:4")
                linkTo(
                    top = space.bottom,
                    bottom = parent.bottom,
                    bottomMargin = 16.dp,
                    topMargin = 16.dp,
                    bias = 0f
                )
                linkTo(
                    start = parent.start,
                    end = parent.end,
                    bias = 0f,
                    startMargin = 16.dp,
                )
            },
            onClick = onCoverClick
        ) {
            GlideImage(
                modifier = Modifier.fillMaxSize(),
                model = seriesinfo.cover,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                transition = CrossFade
            )
        }

        Text(
            modifier = Modifier.constrainAs(title) {
                top.linkTo(cover.top)
                start.linkTo(cover.end, 12.dp)
                end.linkTo(parent.end, 12.dp)
                width = Dimension.fillToConstraints
            },
            text = seriesinfo.title.orEmpty(),
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            minLines = 2,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.constrainAs(info) {
                start.linkTo(title.start)
                bottom.linkTo(cover.bottom)
                end.linkTo(parent.end, 12.dp)
                width = Dimension.fillToConstraints
            },
            text = String.format(
                "出版商：%s\n作\u3000者：%s\n标\u3000签：%s",
                seriesinfo.publisher.orEmpty().ifBlank { "暂无" },
                seriesinfo.authors.orEmpty().ifBlank { "暂无" },
                seriesinfo.tags.orEmpty().ifBlank { "暂无" },
            ),
            style = MaterialTheme.typography.bodySmall,
            maxLines = 3,
            lineHeight = 20.sp,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/**
 * 系列下的全部章节
 */
@Composable
fun BookSeriesContent(
    modifier: Modifier,
    seriesinfo: RemoteSeriesInfo,
    onBookClick: (BookModel) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(3)
    ) {
        items(count = seriesinfo.books.size) {
            val model = seriesinfo.books[it]

            BookSeriesContentItem(
                entity = model,
                index = it,
                onBookClick = {
                    onBookClick(model.book)
                }
            )
        }
    }
}

@Composable
fun BookSeriesContentItem(
    entity: SeriesBook,
    index: Int,
    onBookClick: () -> Unit,
) {
    ConstraintLayout(modifier = Modifier.padding(8.dp)) {
        val (cover, title) = createRefs()

        ElevatedCard(
            modifier = Modifier
                .height(150.dp)
                .constrainAs(cover) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            onClick = onBookClick
        ) {
            GlideImage(
                modifier = Modifier.fillMaxSize(),
                model = entity.cover,
                contentDescription = "",
                contentScale = ContentScale.Crop
            )
        }

        Text(
            modifier = Modifier
                .constrainAs(title) {
                    top.linkTo(cover.bottom, 6.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    width = Dimension.fillToConstraints
                },
            text = entity.title,
            style = MaterialTheme.typography.bodySmall
        )
    }
}

@Composable
@Preview(widthDp = 411, heightDp = 700)
fun PreviewBookSeriesScreen() {
    BookSeriesScreen(
        state = BookSeriesState(),
        onNavUp = {},
        onBookClick = {}
    )
}