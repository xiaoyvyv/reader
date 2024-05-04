package com.xiaoyv.comic.datasource.impl.pdf

import android.graphics.Bitmap
import android.util.Log
import com.artifex.mupdf.fitz.RectI
import com.artifex.mupdf.fitz.android.AndroidDrawDevice
import com.xiaoyv.comic.datasource.BookPage
import com.xiaoyv.comic.datasource.FileBookModel
import com.xiaoyv.comic.datasource.utils.TempHolder

/**
 * [PdfBookPage]
 *
 * @author why
 * @since 5/1/24
 */
class PdfBookPage(
    override val dataSource: PdfBookDataSource,
    override var page: Int = 0
) : BookPage<FileBookModel, PdfBookDataSource> {
    val document get() = requireNotNull(dataSource.document)

    override var pageWidth: Int = 0
    override var pageHeight: Int = 0
    override var pageRatio: Float = 1f

    override fun initPageMeta(): BookPage<FileBookModel, PdfBookDataSource> {
        TempHolder.lock.lock()
        try {
            val docPage = document.loadPage(page)
            val ctm = AndroidDrawDevice.fitPageWidth(docPage, dataSource.displayMetrics.widthPixels)
            val ibox = RectI(docPage.bounds.transform(ctm))
            pageWidth = ibox.x1 - ibox.x0
            pageHeight = ibox.y1 - ibox.y0
            pageRatio = if (pageHeight == 0) 1f else pageWidth.toFloat() / pageHeight.toFloat()
            docPage.destroy()
            return this
        } finally {
            TempHolder.lock.unlock()
        }
    }

    override fun renderPage(): Bitmap {
        TempHolder.lock.lock()
        try {
            val time = System.currentTimeMillis()
            require(dataSource.pageCount > 0) { "No content" }
            require(!dataSource.needsPassword) { "Needs password" }

            Log.e("Render", "渲染${page}")

            val scale = 1f
            val page = document.loadPage(page)
            val ctm = AndroidDrawDevice.fitPageWidth(page, dataSource.displayMetrics.widthPixels)
            val links = page.getLinks()
            if (links != null) for (link in links) link.getBounds().transform(ctm)
            if (scale != 1f) ctm.scale(scale)
            val spend = System.currentTimeMillis() - time
            val bitmap = AndroidDrawDevice.drawPage(page, ctm)
            Log.e("Render", "渲染${this.page} 花费：$spend")
            page.destroy()
            return bitmap
        } finally {
            TempHolder.lock.unlock()
        }
    }

    override fun destroy() {

    }
}