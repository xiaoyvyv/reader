package com.xiaoyv.comic.reader.data.repository.bookinfo

import com.xiaoyv.comic.datasource.BOOK_MODEL_FILE
import com.xiaoyv.comic.datasource.BOOK_MODEL_REMOTE
import com.xiaoyv.comic.datasource.BookDataSourceFactory
import com.xiaoyv.comic.datasource.BookModel
import com.xiaoyv.comic.reader.application
import com.xiaoyv.comic.reader.data.entity.BookEntity
import com.xiaoyv.comic.reader.data.entity.BookSeriesEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * [BookInfoRepositoryImpl]
 *
 * @author why
 * @since 5/1/24
 */
class BookInfoRepositoryImpl : BookInfoRepository {

    override suspend fun loadBookInfo(model: BookModel?): Result<BookSeriesEntity> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val bookModel = requireNotNull(model)

                BookDataSourceFactory.create(application, bookModel).use {
                    it.load()

                    BookSeriesEntity(
                        model = bookModel,
                        metaData = it.getMetaInfo(),
                        cover = it.getCover(),
                        books = listOf(BookEntity(model = bookModel))
                    )
                }
            }
        }
    }
}