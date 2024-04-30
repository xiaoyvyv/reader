@file:OptIn(ExperimentalFoundationApi::class)

package com.xiaoyv.comic.reader.ui.screen.main.home

import android.app.Application
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SecondaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.xiaoyv.comic.i18n.RS
import com.xiaoyv.comic.reader.ui.component.Page
import com.xiaoyv.comic.reader.ui.screen.main.home.book.BookListScreen
import com.xiaoyv.comic.reader.ui.screen.main.home.bookshelf.BookShelfScreen
import com.xiaoyv.comic.reader.ui.screen.main.home.file.BookFileScreen
import com.xiaoyv.comic.reader.ui.utils.copy
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTabScreen(
    viewModel: HomeTabViewModel,
    onNavUp: () -> Unit,
    onNavTo: (String) -> Unit
) {

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            viewModel.convert(it)
        }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Scaffold(
            modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = stringResource(id = RS.app_name),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    },
                    actions = {
                        IconButton(onClick = { }) {
                            Icon(
                                imageVector = Icons.Filled.Search,
                                contentDescription = "Localized description"
                            )
                        }
                    },
                    scrollBehavior = scrollBehavior,
                )
            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues.copy(bottom = 0.dp))
                ) {
                    val pagerState = rememberPagerState(pageCount = { 3 })
                    val tabs = remember {
                        listOf(
                            Page(
                                labelResId = RS.bookshelf_mine,
                                content = { BookListScreen(onNavTo) }
                            ),
                            Page(
                                labelResId = RS.bookshelf_store,
                                content = { BookShelfScreen(onNavTo) }
                            ),
                            Page(
                                labelResId = RS.bookshelf_file,
                                content = {
                                    BookFileScreen(
                                        curentPage = pagerState.currentPage,
                                        onNavTo = onNavTo
                                    )
                                }
                            ),
                        )
                    }

                    SecondaryTabRow(
                        selectedTabIndex = pagerState.currentPage,
                    ) {
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
                                        text = stringResource(id = title.labelResId),
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                }
                            )
                        }
                    }

                    LaunchedEffect(pagerState) {
                        // 从 snapshotFlow 读取 currentPage 收集
                        snapshotFlow { pagerState.currentPage }.collect { page ->
                            // 例如，对每次页面更改执行某些操作:
                            // viewModel.sendPageSelectedEvent(page)
                            Log.d("Page change", "Page changed to $page")
                        }
                    }

                    HorizontalPager(
                        modifier = Modifier.fillMaxSize(),
                        state = pagerState
                    ) { page ->
                        tabs[page].content()
                    }
                }
            }
        )
    }
}

@Preview
@Composable
fun Preview() {

    HomeTabScreen(viewModel = HomeTabViewModel(Application()), onNavUp = {}, onNavTo = {})
}