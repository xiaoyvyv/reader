package com.xiaoyv.comic.reader.data.repository.bookinfo

import android.net.Uri
import com.xiaoyv.comic.datasource.BookDataSourceFactory
import com.xiaoyv.comic.datasource.BookModel
import com.xiaoyv.comic.datasource.FileBookModel
import com.xiaoyv.comic.reader.application
import com.xiaoyv.comic.reader.data.entity.BookEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * [BookInfoRepositoryImpl]
 *
 * @author why
 * @since 5/1/24
 */
class BookInfoRepositoryImpl : BookInfoRepository {

    override suspend fun loadBookInfo(model: BookModel?): Result<BookEntity> {
        return runCatching {
            withContext(Dispatchers.IO) {
                val bookModel = requireNotNull(model)

                BookDataSourceFactory.create(application, bookModel).use {
                    it.load()

                    BookEntity(
                        model = bookModel,
                        metaData = it.getMetaInfo()
                    )
                }
            }
        }
    }
}