package com.xiaoyv.comic.datasource.impl.djvu

import android.graphics.Bitmap
import androidx.annotation.Keep
import com.xiaoyv.comic.datasource.jni.NativeContext
import com.xiaoyv.comic.datasource.jni.codec.PageLink
import com.xiaoyv.comic.datasource.jni.codec.PageTextBox

/**
 * [DjvuBookPage]
 *
 * @author why
 * @since 4/30/24
 */
@Keep
object DjvuBookPage {
    init {
        NativeContext.init()
    }

    @Synchronized
    external fun getWidth(pageHandle: Long): Int

    @Synchronized
    external fun getHeight(pageHandle: Long): Int

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

    external fun free(pageHandle: Long)

    external fun getPageLinks(docHandle: Long, pageNo: Int): ArrayList<PageLink>?

    external fun getPageText(
        docHandle: Long,
        pageNo: Int,
        contextHandle: Long,
        pattern: String?
    ): List<PageTextBox>?
}