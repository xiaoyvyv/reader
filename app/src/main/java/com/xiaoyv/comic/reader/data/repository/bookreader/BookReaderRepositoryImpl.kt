package com.xiaoyv.comic.reader.data.repository.bookreader

import com.xiaoyv.comic.datasource.BookDataSource
import com.xiaoyv.comic.datasource.BookModel
import com.xiaoyv.comic.datasource.BookPage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * [BookReaderRepositoryImpl]
 *
 * @author why
 * @since 5/1/24
 */
class BookReaderRepositoryImpl : BookReaderRepository {

    override suspend fun loadPages(dataSource: BookDataSource< BookModel>?): Result<List<BookPage<BookModel, out BookDataSource<BookModel>>>> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val source = requireNotNull(dataSource)
                val pages = arrayListOf<BookPage<BookModel, out BookDataSource<BookModel>>>()
                repeat(source.pageCount) {
                    pages.add(source.getPage(it).initPageMeta())
                }
                pages
            }
        }
    }
}