package com.xiaoyv.comic.reader.data.repository.bookreader

import com.xiaoyv.comic.datasource.book.BookDataSource
import com.xiaoyv.comic.datasource.book.BookModel
import com.xiaoyv.comic.datasource.book.BookPage

/**
 * [BookReaderRepository]
 *
 * @author why
 * @since 5/1/24
 */
interface BookReaderRepository {

    suspend fun loadPages(dataSource: BookDataSource<BookModel>?): Result<List<BookPage<BookModel, out BookDataSource<BookModel>>>>
}