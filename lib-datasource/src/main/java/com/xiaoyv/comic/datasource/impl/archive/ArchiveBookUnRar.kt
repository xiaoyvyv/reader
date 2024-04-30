package com.xiaoyv.comic.datasource.impl.archive

import androidx.annotation.Keep
import com.xiaoyv.comic.datasource.jni.NativeContext

/**
 * [ArchiveBookUnRar]
 *
 * @author why
 * @since 4/30/24
 */
@Keep
object ArchiveBookUnRar {
    init {
        NativeContext.init()
    }

    @Synchronized
    external fun loadFile(filePath: String): Long

    external fun getPages(fileHandle: Long): Map<Int, String>

    external fun extractPage(fileHandle: Long, index: Int, outpath: String): Boolean

    @Synchronized
    external fun free(fileHandle: Long)
}