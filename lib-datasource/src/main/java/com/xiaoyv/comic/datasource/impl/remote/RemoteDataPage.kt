package com.xiaoyv.comic.datasource.impl.remote

import android.graphics.Bitmap
import com.xiaoyv.comic.datasource.BookPage
import com.xiaoyv.comic.datasource.RemoteBookModel

/**
 * [RemoteDataPage]
 *
 * @author why
 * @since 5/4/24
 */
class RemoteDataPage(
    override val dataSource: RemoteDataSource,
    override val page: Int
) : BookPage<RemoteBookModel, RemoteDataSource> {

    override var pageWidth: Int = 0
    override var pageHeight: Int = 0
    override var pageRatio: Float = 0f

    override fun initPageMeta(): BookPage<RemoteBookModel, RemoteDataSource> {
        return this
    }

    override fun renderPage(): Bitmap {
        TODO("Not yet implemented")
    }

    override fun destroy() {

    }
}