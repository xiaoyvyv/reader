package com.xiaoyv.comic.reader.data.repository.bookinfo

import com.xiaoyv.comic.datasource.BookModel
import com.xiaoyv.comic.reader.data.entity.BookSeriesEntity

/**
 * [BookInfoRepository]
 *
 * @author why
 * @since 5/1/24
 */
interface BookInfoRepository {

    suspend fun loadBookInfo(model: BookModel?): Result<BookSeriesEntity>
}