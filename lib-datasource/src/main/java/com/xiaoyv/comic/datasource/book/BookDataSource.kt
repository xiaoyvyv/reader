package com.xiaoyv.comic.datasource.book

import android.content.Context
import android.util.Log
import com.xiaoyv.comic.datasource.utils.md5
import java.io.Closeable
import java.io.File

/**
 * [BookDataSource]
 *
 * @author why
 * @since 4/29/24
 */
interface BookDataSource<T : BookModel> : Closeable {
    val context: Context
    val model: T

    val fileName: String
        get() = model.fileName

    var pageCount: Int

    fun load()

    @Throws(Exception::class)
    fun getCover(): String

    fun getPage(page: Int): BookPage<T, out BookDataSource<T>>

    fun getMetaInfo(): BookMetaData

    /**
     * 支持的格式，不带点
     */
    fun supportExtension(): List<String>

    fun createDataDir(deleteAllInDir: Boolean = false): File {
        val extensions = supportExtension()
        val typeName = if (extensions.isEmpty()) "default" else extensions.joinToString("-")
        val dir = context.cacheDir.absolutePath + "/source/$typeName/${model.key.md5()}"
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

    override fun close() {
        destroy()
    }

    fun log(message: () -> Any?) {
        Log.i(javaClass.simpleName, message().toString())
    }
}