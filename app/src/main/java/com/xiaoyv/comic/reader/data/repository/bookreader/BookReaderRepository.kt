package com.xiaoyv.comic.reader.data.repository.bookreader

import com.xiaoyv.comic.datasource.BookDataSource
import com.xiaoyv.comic.datasource.BookModel
import com.xiaoyv.comic.datasource.BookPage

/**
 * [BookReaderRepository]
 *
 * @author why
 * @since 5/1/24
 */
interface BookReaderRepository {

    suspend fun loadPages(dataSource: BookDataSource<BookModel>?): Result<List<BookPage<BookModel, out BookDataSource<BookModel>>>>
}