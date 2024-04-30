package com.xiaoyv.comic.datasource.impl.djvu

import androidx.annotation.Keep
import com.xiaoyv.comic.datasource.jni.NativeContext

/**
 * [DjvuBookOutline]
 *
 * @author why
 * @since 4/30/24
 */
@Keep
object DjvuBookOutline {
    init {
        NativeContext.init()
    }

    external fun open(dochandle: Long): Long

    external fun expConsp(expr: Long): Boolean

    external fun getTitle(expr: Long): String?

    external fun getLink(expr: Long, dochandle: Long): String?

    external fun getNext(expr: Long): Long

    external fun getChild(expr: Long): Long
}