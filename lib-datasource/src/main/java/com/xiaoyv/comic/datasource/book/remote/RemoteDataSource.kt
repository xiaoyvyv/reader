package com.xiaoyv.comic.datasource.book.remote

import android.content.Context
import com.xiaoyv.comic.datasource.book.BookDataSource
import com.xiaoyv.comic.datasource.book.BookMetaData
import com.xiaoyv.comic.datasource.book.BookPage
import com.xiaoyv.comic.datasource.book.RemoteBookModel
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteBookDetailEntity
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryFactory
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
    private var bookDetail: RemoteBookDetailEntity? = null

    override var pageCount = 0

    override fun load() {
        runBlocking {
            bookDetail = remoteLibrary.getBookDetail(model.bookId, model.seriesId)
                .getOrThrow()

            pageCount = bookDetail?.pageCount ?: 0
        }
    }

    override fun getCover(): String {
        return bookDetail?.cover.orEmpty()
    }

    override fun getPage(page: Int): BookPage<RemoteBookModel, out BookDataSource<RemoteBookModel>> {
        return RemotePage(
            dataSource = this,
            page = page,
            pageInfo = bookDetail?.pages?.getOrNull(page)
        )
    }

    override fun getMetaInfo(): BookMetaData {
        val entity = requireNotNull(bookDetail)
        return BookMetaData(
            format = entity.format,
            encryption = "",
            author = entity.authors.orEmpty(),
            title = entity.title,
            subject = entity.summary.orEmpty(),
            keywords = "",
            creator = entity.authors.orEmpty(),
            producer = entity.publisher.orEmpty(),
            creationDate = entity.created.toString(),
            modDate = entity.lastModified.toString()
        )
    }

    override fun supportExtension(): List<String> {
        return listOf("remote")
    }

    override fun destroy() {

    }


    /*  private var bookDetail: RemoteSeriesEntity? = null
      private var bookManifest: RemoteBookManifestEntity? = null

      override var pageCount: Int = 0

      override fun load() {
          runBlocking {
              val results = awaitAll(
                  async { remoteLibrary.getBookDetail(model.bookId).getOrNull() },
              )

  //            bookDetail = results[0] as? RemoteBookSeriesEntity
  //            pageCount = bookDetail?.bookCount ?: 0
  //
  //            bookManifest = results[1] as? RemoteBookManifestEntity
          }
      }

      override fun getCover(): String {
          return bookDetail?.cover.orEmpty()
      }

      override fun getPage(page: Int): BookPage<RemoteBookModel, out BookDataSource<RemoteBookModel>> {
          return RemotePage(
              dataSource = this,
              page = page,
              manifestPage = bookManifest?.pages?.getOrNull(page)
          )
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

      }*/
}