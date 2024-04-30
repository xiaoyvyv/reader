package com.xiaoyv.comic.reader.data.entity

import com.xiaoyv.comic.datasource.utils.FileExtension.imageExtensions
import com.xiaoyv.comic.datasource.utils.FileExtension.readerFileExtensions
import java.io.File

/**
 * [FileEntity]
 *
 * @author why
 * @since 4/28/24
 */
data class FileEntity(
    var file: File,
    var cdParent: Boolean = false,
    var extension: String = "",
    var length: Long = 0,
    var lengthText: String = "",
    var date: String = "",
) {
    val isBook: Boolean
        get() = readerFileExtensions.contains(extension.lowercase())

    val isImage: Boolean
        get() = imageExtensions.contains(extension.lowercase())
}
