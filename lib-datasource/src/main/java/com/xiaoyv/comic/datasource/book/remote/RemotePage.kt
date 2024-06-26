package com.xiaoyv.comic.datasource.book.remote

import android.graphics.Bitmap
import com.xiaoyv.comic.datasource.book.BookPage
import com.xiaoyv.comic.datasource.book.RemoteBookModel
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteBookDetailEntity

/**
 * [RemotePage]
 *
 * @author why
 * @since 5/4/24
 */
class RemotePage(
    override val dataSource: RemoteDataSource,
    override val page: Int,
    val pageInfo: RemoteBookDetailEntity.Page?,
) : BookPage<RemoteBookModel, RemoteDataSource> {

    override var pageWidth: Int = 0
    override var pageHeight: Int = 0
    override var pageRatio: Float = 0f

    init {
        pageWidth = pageInfo?.width ?: 0
        pageHeight = pageInfo?.height ?: 0
        pageRatio = if (pageHeight == 0) 1f else pageWidth.toFloat() / pageHeight.toFloat()
    }

    override fun initPageMeta(): BookPage<RemoteBookModel, RemoteDataSource> {
        return this
    }

    override fun renderPage(): Bitmap {
        TODO("Not yet implemented")
    }

    override fun renderPageUrl(): String {
        return pageInfo?.url.orEmpty()
    }

    override fun destroy() {

    }
}