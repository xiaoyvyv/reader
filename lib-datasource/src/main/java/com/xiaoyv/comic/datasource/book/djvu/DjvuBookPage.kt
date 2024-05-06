package com.xiaoyv.comic.datasource.book.djvu

import android.graphics.Bitmap
import android.util.Log
import androidx.annotation.Keep
import com.xiaoyv.comic.datasource.book.BookPage
import com.xiaoyv.comic.datasource.book.FileBookModel
import com.xiaoyv.comic.datasource.utils.NativeContext
import com.xiaoyv.comic.datasource.jni.codec.PageLink
import com.xiaoyv.comic.datasource.jni.codec.PageTextBox
import com.xiaoyv.comic.datasource.utils.TempHolder
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

/**
 * [DjvuBookPage]
 *
 * @author why
 * @since 4/30/24
 */
@Keep
class DjvuBookPage(
    override val dataSource: DjvuBookDataSource,
    override var page: Int = 0
) : BookPage<FileBookModel, DjvuBookDataSource> {

    override var pageWidth: Int = 0
    override var pageHeight: Int = 0
    override var pageRatio: Float = 0f

    override fun initPageMeta(): BookPage<FileBookModel, DjvuBookDataSource> {
        return this
    }

    override fun renderPage(): Bitmap {
        Log.e("Render", "Page: $page")

        TempHolder.lock.lock()
        try {
            require(dataSource.docHandle != 0L)
            require(dataSource.pageCount > 0)

            val pageHandle = DjvuBookDocument.getPage(dataSource.docHandle, page).also {
                require(it != 0L)
            }

            var count = 0
            while ((pageWidth == 0 || pageHeight == 0) && count < 5) {
                pageWidth = getWidth(pageHandle)
                pageHeight = getHeight(pageHandle)
                count++
                runBlocking { delay(50) }
            }

//            pageRatio = if (pageHeight == 0) 1f else pageWidth.toFloat() / pageHeight.toFloat()

            require(pageWidth > 0 && pageHeight > 0) { "Page size error! [w=$pageWidth, h=$pageHeight]" }

            // 注意这里  Bitmap.Config.RGB_565
            val bitmap = Bitmap.createBitmap(pageWidth, pageHeight, Bitmap.Config.RGB_565)
            val result = renderPageBitmap(
                pageHandle = pageHandle,
                contextHandle = dataSource.contextHandle,
                targetWidth = pageWidth,
                targetHeight = pageHeight,
                pageSliceX = 0,
                pageSliceY = 0,
                pageSliceWidth = pageWidth,
                pageSliceHeight = pageHeight,
                bitmap = bitmap,
                renderMode = dataSource.renderMode
            )

            free(pageHandle)

            require(result)

            return bitmap
        } finally {
            TempHolder.lock.unlock()
        }
    }

    override fun destroy() {

    }

    companion object {

        init {
            NativeContext.init()
        }

        @JvmStatic
        @Synchronized
        external fun getWidth(pageHandle: Long): Int

        @JvmStatic
        @Synchronized
        external fun getHeight(pageHandle: Long): Int

        @JvmStatic
        external fun renderPage(
            pageHandle: Long,
            contextHandle: Long,
            targetWidth: Int,
            targetHeight: Int,
            pageSliceX: Int,
            pageSliceY: Int,
            pageSliceWidth: Int,
            pageSliceHeight: Int,
            buffer: IntArray,
            renderMode: Int
        ): Boolean

        @JvmStatic
        external fun renderPageBitmap(
            pageHandle: Long,
            contextHandle: Long,
            targetWidth: Int,
            targetHeight: Int,
            pageSliceX: Int,
            pageSliceY: Int,
            pageSliceWidth: Int,
            pageSliceHeight: Int,
            bitmap: Bitmap,
            renderMode: Int
        ): Boolean

        @JvmStatic
        external fun free(pageHandle: Long)

        @JvmStatic
        external fun getPageLinks(docHandle: Long, pageNo: Int): ArrayList<PageLink>?

        @JvmStatic
        external fun getPageText(
            docHandle: Long,
            pageNo: Int,
            contextHandle: Long,
            pattern: String?
        ): List<PageTextBox>?
    }
}