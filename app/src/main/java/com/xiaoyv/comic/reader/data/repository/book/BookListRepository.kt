package com.xiaoyv.comic.reader.data.repository.book

import androidx.paging.PagingSource
import com.xiaoyv.comic.reader.data.entity.BookSeriesEntity

/**
 * [BookListRepository]
 *
 * @author why
 * @since 4/27/24
 */
interface BookListRepository {
    val pageSource: PagingSource<Int, BookSeriesEntity>
}