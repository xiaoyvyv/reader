package com.xiaoyv.comic.datasource.impl

import android.content.Context
import android.util.Log
import com.xiaoyv.comic.datasource.utils.md5
import java.io.File

/**
 * [BookDataSource]
 *
 * @author why
 * @since 4/29/24
 */
interface BookDataSource {
    val context: Context
    val file: File

    val fileName: String
        get() = file.name.orEmpty().trim()

    val fileNameWithoutExtension: String
        get() = file.nameWithoutExtension.trim()

    fun load()

    @Throws(Exception::class)
    fun getCover(): String

    @Throws(Exception::class)
    fun getPageCount(): Int = 0

    /**
     * 支持的格式，不带点
     */
    fun supportExtension(): List<String>

    fun createDataDir(deleteAllInDir: Boolean = false): File {
        val extensions = supportExtension()
        val typeName = if (extensions.isEmpty()) "default" else extensions.joinToString("-")
        val dir = context.cacheDir.absolutePath + "/source/$typeName/${file.absolutePath.md5()}"
        return File(dir).apply {
            if (exists() && deleteAllInDir) {
                deleteRecursively()
            }
            if (!exists()) {
                mkdirs()
            }
        }
    }

    fun destroy()

    fun log(message: () -> Any?) {
        Log.i(javaClass.simpleName, message().toString())
    }
}