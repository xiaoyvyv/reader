package com.xiaoyv.comic.reader.ui.screen.feature.bookreader

import androidx.paging.LoadState
import com.xiaoyv.comic.datasource.book.BookDataSource
import com.xiaoyv.comic.datasource.book.BookModel
import com.xiaoyv.comic.datasource.book.BookPage

/**
 * [BookReaderState]
 *
 * @author why
 * @since 5/1/24
 */
data class BookReaderState(
    val loadState: LoadState = LoadState.Loading,
    val pages: List<BookPage<BookModel, out BookDataSource<BookModel>>> = emptyList()
)