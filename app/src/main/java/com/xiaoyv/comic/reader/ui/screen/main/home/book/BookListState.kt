package com.xiaoyv.comic.reader.ui.screen.main.home.book

import com.xiaoyv.comic.reader.data.entity.BookSeriesEntity

/**
 * [BookListState]
 *
 * @author why
 * @since 4/26/24
 */
internal sealed interface BookListState {
    data object Initial : BookListState

    data object Empty : BookListState

    data class BookList(val items: List<BookSeriesEntity>) : BookListState
}
