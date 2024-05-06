package com.xiaoyv.comic.datasource.impl.remote

import android.graphics.Bitmap
import com.xiaoyv.comic.datasource.BookPage
import com.xiaoyv.comic.datasource.RemoteBookModel
import com.xiaoyv.comic.datasource.remote.RemoteBookManifestEntity

/**
 * [RemotePage]
 *
 * @author why
 * @since 5/4/24
 */
class RemotePage(
    override val dataSource: RemoteDataSource,
    override val page: Int,
    val manifestPage: RemoteBookManifestEntity.Page?
) : BookPage<RemoteBookModel, RemoteDataSource> {

    override var pageWidth: Int = 0
    override var pageHeight: Int = 0
    override var pageRatio: Float = 0f

    init {
        pageWidth = manifestPage?.width ?: 0
        pageHeight = manifestPage?.height ?: 0
        pageRatio = if (pageHeight == 0) 1f else pageWidth.toFloat() / pageHeight.toFloat()
    }

    override fun initPageMeta(): BookPage<RemoteBookModel, RemoteDataSource> {
        return this
    }

    override fun renderPage(): Bitmap {
        TODO("")
    }

    override fun renderPageUrl(): String {
        return manifestPage?.url.orEmpty()
    }

    override fun destroy() {

    }
}