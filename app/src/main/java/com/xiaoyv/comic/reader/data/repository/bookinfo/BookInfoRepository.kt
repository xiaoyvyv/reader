package com.xiaoyv.comic.reader.data.repository.bookinfo

import com.xiaoyv.comic.datasource.book.BookModel
import com.xiaoyv.comic.reader.data.entity.BookEntity

/**
 * [BookInfoRepository]
 *
 * @author why
 * @since 5/1/24
 */
interface BookInfoRepository {

    suspend fun loadBookInfo(model: BookModel?): Result<BookEntity>
}