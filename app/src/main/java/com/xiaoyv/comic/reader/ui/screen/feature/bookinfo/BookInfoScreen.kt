package com.xiaoyv.comic.reader.ui.screen.feature.bookinfo

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BookmarkAdd
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import com.bumptech.glide.integration.compose.CrossFade
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.xiaoyv.comic.datasource.book.BookMetaData
import com.xiaoyv.comic.reader.R
import com.xiaoyv.comic.reader.data.entity.BookEntity
import com.xiaoyv.comic.reader.ui.component.ComicAppBar
import com.xiaoyv.comic.reader.ui.component.ImageViewer
import com.xiaoyv.comic.reader.ui.component.LocalPopupHostState
import com.xiaoyv.comic.reader.ui.component.PageStateScreen
import com.xiaoyv.comic.reader.ui.component.ScaffoldWrap
import com.xiaoyv.comic.reader.ui.component.rememberPopupHostState
import com.xiaoyv.comic.reader.ui.utils.isNotLoading

/**
 * [BookInfoRoute]
 *
 * @author why
 * @since 5/1/24
 */
@Composable
fun BookInfoRoute(
    onNavUp: () -> Unit,
    onReadClick: (BookEntity) -> Unit
) {
    val viewModel = viewModel<BookInfoViewModel>()

    val bookState by viewModel.uiState.collectAsStateWithLifecycle()

    BookInfoScreen(
        bookState = bookState,
        onNavUp = onNavUp,
        onReadClick = { onReadClick(bookState.bookEntity) }
    )
}

@Composable
fun BookInfoScreen(
    bookState: BookInfoState,
    onNavUp: () -> Unit,
    onReadClick: () -> Unit
) {
    ScaffoldWrap(
        topBar = {
            ComicAppBar(
                title = "书籍详情",
                colors = TopAppBarDefaults
                    .topAppBarColors()
                    .copy(containerColor = Color.Transparent),
                actions = {
                    if (bookState.loadState.isNotLoading) {
                        IconButton(onClick = {}) {
                            Icon(
                                imageVector = Icons.Default.BookmarkAdd,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                },
                onNavigationIconClick = onNavUp,
            )
        },
        floatingActionButton = {
            if (bookState.loadState.isNotLoading) {
                ExtendedFloatingActionButton(
                    onClick = onReadClick,
                    icon = { Icon(Icons.Default.PlayArrow, null) },
                    text = { Text(text = "开始阅读") },
                )
            }
        }
    ) { padding ->
        val popupHostState = LocalPopupHostState.current

        PageStateScreen(loadState = bookState.loadState) {
            Column {
                // Header
                BookInfoHeader(
                    modifier = Modifier.padding(top = padding.calculateTopPadding()),
                    bookEntity = bookState.bookEntity,
                    onCoverClick = {
                        popupHostState.show {
                            ImageViewer(
                                item = bookState.bookEntity.cover,
                                onImageTap = {
                                    popupHostState.hide()
                                }
                            )
                        }
                    }
                )

                Text(
                    modifier = Modifier.padding(16.dp),
                    text = "简介",
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    text = bookState.bookEntity.metaData.subject,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 8,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun BookInfoHeader(
    modifier: Modifier,
    bookEntity: BookEntity,
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
        val (background, space, cover, title, author, type) = createRefs()

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
            model = bookEntity.cover,
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
                model = bookEntity.cover,
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
            text = bookEntity.metaData.title,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Bold,
            minLines = 2,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier
                .padding(vertical = 4.dp)
                .constrainAs(author) {
                    start.linkTo(title.start)
                    bottom.linkTo(type.top)
                    end.linkTo(parent.end, 12.dp)
                    width = Dimension.fillToConstraints
                },
            text = bookEntity.metaData.author,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

        Text(
            modifier = Modifier.constrainAs(type) {
                start.linkTo(title.start)
                bottom.linkTo(cover.bottom)
                end.linkTo(parent.end, 12.dp)
                width = Dimension.fillToConstraints
            },
            text = bookEntity.metaData.format,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )

    }
}


@Composable
@Preview(widthDp = 411, heightDp = 700)
fun PreviewBookInfoScreen() {
    val popupHostState = rememberPopupHostState()
    CompositionLocalProvider(LocalPopupHostState provides popupHostState) {
        BookInfoScreen(
            bookState = BookInfoState(
                loadState = LoadState.NotLoading(true),
                bookEntity = BookEntity(
                    metaData = BookMetaData(
                        subject = "你好"
                    )
                )
            ),
            onNavUp = {},
            onReadClick = {}
        )
    }
}