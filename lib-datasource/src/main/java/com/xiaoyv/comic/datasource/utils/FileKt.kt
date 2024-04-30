package com.xiaoyv.comic.datasource.utils

import java.io.File

/**
 * [createDir]
 *
 * @author why
 * @since 4/29/24
 */
fun createDir(path: String, deleteAll: Boolean = false): File {
    val file = File(path)
    if (file.exists()) {
        if (file.isFile) {
            file.delete()
            file.mkdirs()
        }
    } else {
        file.mkdirs()
    }

    if (deleteAll) {
        file.deleteRecursively()
        file.mkdirs()
    }
    return file
}