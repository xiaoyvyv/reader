package com.xiaoyv.comic.datasource.impl.djvu

import androidx.annotation.Keep
import com.xiaoyv.comic.datasource.jni.NativeContext
import com.xiaoyv.comic.datasource.jni.codec.CodecPageInfo

/**
 * [DjvuBookDocument]
 *
 * @author why
 * @since 4/30/24
 */
@Keep
object DjvuBookDocument {
    init {
        NativeContext.init()
    }

    external fun getMeta(docHandle: Long, key: String): String?

    external fun getMetaKeys(docHandle: Long): String?

    external fun getPageInfo(
        docHandle: Long,
        pageNumber: Int,
        contextHandle: Long,
        cpi: CodecPageInfo
    ): Int

    external fun open(contextHandle: Long, fileName: String): Long

    external fun getPage(docHandle: Long, pageNumber: Int): Long

    external fun getPageCount(docHandle: Long): Int

    external fun free(docHandle: Long)
}