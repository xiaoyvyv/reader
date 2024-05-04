package com.xiaoyv.comic.reader.ui.screen.main.home.file

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.integration.compose.GlideImage
import com.xiaoyv.comic.reader.data.entity.FileEntity
import com.xiaoyv.comic.reader.ui.component.LazyList
import com.xiaoyv.comic.reader.ui.component.LocalPopupHostState
import com.xiaoyv.comic.reader.ui.component.LocalSnackbarHostState
import com.xiaoyv.comic.reader.ui.component.PageStateScreen
import com.xiaoyv.comic.reader.ui.utils.iconVector
import kotlinx.coroutines.launch
import java.io.File

/**
 * [BookFileScreen]
 *
 * @author why
 * @since 4/26/24
 */
@Composable
fun BookFileRoute(
    curentPage: Int,
    onBookFileClick: (FileEntity) -> Unit
) {
    val viewModel = viewModel<BookFileViewModel>()
    val fileState by viewModel.fileState.collectAsStateWithLifecycle()

    BackHandler(curentPage == 2) {
        if (viewModel.canNavUp()) {
            viewModel.navUp()
        }
    }

    BookFileScreen(
        fileState = fileState,
        onEnterDir = viewModel::navTo,
        onBookFileClick = onBookFileClick
    )
}

@Composable
fun BookFileScreen(
    fileState: BookFileState,
    onEnterDir: (FileEntity) -> Unit,
    onBookFileClick: (FileEntity) -> Unit
) {
    // 状态视图绑定
    PageStateScreen(
        loadState = fileState.loadState,
        itemCount = { fileState.files.size },
        onRetryClick = {

        }
    ) {
        LazyList(modifier = Modifier.fillMaxSize()) {
            items(fileState.files.size) {
                val entity = fileState.files[it]
                if (entity.isBook || entity.isImage) {
                    BookFileScreenBookItem(
                        fileEntity = entity,
                        index = it,
                        onEnterDir = onEnterDir,
                        onBookFileClick = onBookFileClick
                    )
                } else {
                    BookFileScreenNormalItem(
                        fileEntity = entity,
                        index = it,
                        onEnterDir = onEnterDir
                    )
                }
            }
        }
    }
}

@Composable
fun BookFileScreenNormalItem(
    fileEntity: FileEntity,
    index: Int,
    onEnterDir: (FileEntity) -> Unit,
) {
//    val popupHostState = LocalPopupHostState.current
//    val hostState = LocalSnackbarHostState.current
    val scope = rememberCoroutineScope()
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        onClick = {
            if (fileEntity.cdParent || fileEntity.file.isDirectory) {
                onEnterDir(fileEntity)
            } else {
//                popupHostState.show {
//                    Text(text = "你好")
//                }
                scope.launch {
//                    hostState.showSnackbar("你好")
                }
            }
        }
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (icon, title) = createRefs()
            Image(
                modifier = Modifier
                    .size(24.dp)
                    .constrainAs(icon) {
                        start.linkTo(parent.start, 12.dp)
                        top.linkTo(parent.top, 12.dp)
                        bottom.linkTo(parent.bottom, 12.dp)
                    },
                imageVector = fileEntity.file.iconVector(),
                colorFilter = if (fileEntity.file.isDirectory) {
                    ColorFilter.tint(MaterialTheme.colorScheme.primary)
                } else {
                    ColorFilter.tint(MaterialTheme.colorScheme.secondary)
                },
                contentDescription = null
            )
            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        start.linkTo(icon.end, 12.dp)
                        top.linkTo(parent.top, 12.dp)
                        end.linkTo(parent.end, 12.dp)
                        bottom.linkTo(parent.bottom, 12.dp)
                        width = Dimension.fillToConstraints
                    },
                text = if (fileEntity.cdParent) ".." else fileEntity.file.name.orEmpty(),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
fun BookFileScreenBookItem(
    fileEntity: FileEntity,
    index: Int,
    onEnterDir: (FileEntity) -> Unit,
    onBookFileClick: (FileEntity) -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp),
        onClick = {
            if (fileEntity.cdParent || fileEntity.file.isDirectory) {
                onEnterDir(fileEntity)
            } else {
                onBookFileClick(fileEntity)
            }
        }
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
            val (cover, title, subtitle, info) = createRefs()

            ElevatedCard(
                modifier = Modifier
                    .width(80.dp)
                    .aspectRatio(3 / 4f)
                    .constrainAs(cover) {
                        top.linkTo(parent.top, 12.dp)
                        start.linkTo(parent.start, 12.dp)
                        bottom.linkTo(parent.bottom, 12.dp)
                    }
            ) {
                GlideImage(
                    modifier = Modifier.fillMaxSize(),
                    model = fileEntity.file.absolutePath,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }

            Text(
                modifier = Modifier
                    .constrainAs(title) {
                        top.linkTo(parent.top, 12.dp)
                        end.linkTo(parent.end, 12.dp)
                        start.linkTo(cover.end, 12.dp)
                        width = Dimension.fillToConstraints
                    },
                text = fileEntity.file.name,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyLarge
            )

            Text(
                modifier = Modifier
                    .constrainAs(subtitle) {
                        top.linkTo(title.bottom, 6.dp)
                        end.linkTo(title.end)
                        start.linkTo(title.start)
                        width = Dimension.fillToConstraints
                    },
                text = fileEntity.file.path,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                modifier = Modifier
                    .constrainAs(info) {
                        end.linkTo(subtitle.end)
                        start.linkTo(subtitle.start)
                        bottom.linkTo(cover.bottom)
                        width = Dimension.fillToConstraints
                    },
                text = "${fileEntity.extension.uppercase()} ${fileEntity.lengthText} ${fileEntity.date} ",
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
@Preview(widthDp = 360)
fun PreviewBookItem() {
    Column {
        BookFileScreenNormalItem(
            fileEntity = FileEntity(file = File("你好、你、\nf非阿发阿发af\n asfasfa")),
            index = 0,
            onEnterDir = {}
        )
        BookFileScreenBookItem(
            fileEntity = FileEntity(file = File("你好")),
            index = 0,
            onEnterDir = {},
            onBookFileClick = {}
        )
    }
}