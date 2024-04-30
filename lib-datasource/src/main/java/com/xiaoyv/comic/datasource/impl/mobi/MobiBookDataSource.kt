package com.xiaoyv.comic.datasource.impl.mobi

import android.content.Context
import com.xiaoyv.comic.datasource.impl.BookDataSource
import com.xiaoyv.comic.datasource.utils.createDir
import java.io.File

/**
 * [MobiBookDataSource]
 *
 * @author why
 * @since 4/29/24
 */
class MobiBookDataSource(
    override val context: Context,
    override val file: File
) : BookDataSource {

    private val saveDir by lazy { createDataDir() }

    override fun load() {

    }

    override fun getCover(): String {
        val coverDir = createDir(saveDir.absolutePath + "/cover", true)
        val coverPathPre = coverDir.absolutePath + "/$fileNameWithoutExtension"
        MobiExtractor.extractCover(file.absolutePath, coverPathPre)
        return coverDir.listFiles()?.firstOrNull()?.absolutePath.orEmpty()
    }

    override fun supportExtension(): List<String> {
        return listOf("mobi")
    }

    override fun destroy() {

    }
}