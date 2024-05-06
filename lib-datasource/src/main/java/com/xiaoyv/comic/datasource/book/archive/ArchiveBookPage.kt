package com.xiaoyv.comic.datasource.book.archive

import android.graphics.Bitmap
import com.xiaoyv.comic.datasource.book.BookPage
import com.xiaoyv.comic.datasource.book.FileBookModel

/**
 * [ArchiveBookPage]
 *
 * @author why
 * @since 5/1/24
 */
class ArchiveBookPage(
    override val dataSource: com.xiaoyv.comic.datasource.book.archive.ArchiveBookDataSource,
    override var page: Int = 0
) : BookPage<FileBookModel, ArchiveBookDataSource> {

    override var pageWidth: Int = 0
    override var pageHeight: Int = 0
    override var pageRatio: Float = 1f

    override fun initPageMeta(): BookPage<FileBookModel, ArchiveBookDataSource> {
        TODO("Not yet implemented")
    }

    override fun renderPage(): Bitmap {
        TODO("Not yet implemented")
    }

    override fun destroy() {

    }
}