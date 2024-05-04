package com.xiaoyv.comic.datasource.impl.remote

import android.content.Context
import com.xiaoyv.comic.datasource.BookDataSource
import com.xiaoyv.comic.datasource.BookMetaData
import com.xiaoyv.comic.datasource.BookPage
import com.xiaoyv.comic.datasource.RemoteBookModel
import com.xiaoyv.comic.datasource.remote.RemoteBookEntity
import com.xiaoyv.comic.datasource.remote.RemoteLibraryFactory
import kotlinx.coroutines.runBlocking

/**
 * [RemoteDataSource]
 *
 * @author why
 * @since 5/4/24
 */
class RemoteDataSource(
    override val context: Context,
    override val model: RemoteBookModel,
) : BookDataSource<RemoteBookModel> {
    private val remoteLibrary = RemoteLibraryFactory.create(context, model.config)
    private var bookDetail: RemoteBookEntity? = null

    override var pageCount: Int = 0

    override fun load() {
        runBlocking {
            bookDetail = remoteLibrary.getBookDetail(model.bookId, model.libraryId)
                .getOrNull()
        }
    }

    override fun getCover(): String {
        return bookDetail?.cover.orEmpty()
    }

    override fun getPage(page: Int): BookPage<RemoteBookModel, out BookDataSource<RemoteBookModel>> {
        return RemoteDataPage(this, page)
    }

    override fun getMetaInfo(): BookMetaData {
        val entity = requireNotNull(bookDetail)
        return BookMetaData(
            format = "REMOTE",
            encryption = "",
            author = entity.authors.joinToString(","),
            title = entity.title,
            subject = entity.summary,
            keywords = entity.tags.orEmpty().joinToString(","),
            creator = entity.publisher,
            producer = entity.publisher,
            creationDate = entity.created.toString(),
            modDate = entity.lastModified.toString()
        )
    }

    override fun supportExtension(): List<String> {
        return listOf("https", "http")
    }

    override fun destroy() {

    }
}