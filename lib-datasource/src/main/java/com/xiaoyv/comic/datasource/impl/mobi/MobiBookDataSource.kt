package com.xiaoyv.comic.datasource.impl.mobi

import android.content.Context
import com.xiaoyv.comic.datasource.FileBookModel
import com.xiaoyv.comic.datasource.impl.pdf.PdfBookDataSource
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
    override val model: FileBookModel
) : PdfBookDataSource(context, model) {

    override fun getCover(): String {
        val coverDir = createDir(saveDir.absolutePath + "/cover", true)
        val coverPathPre = coverDir.absolutePath + "/${model.file.nameWithoutExtension}"
        MobiExtractor.extractCover(model.file.absolutePath, coverPathPre)
        return coverDir.listFiles()?.firstOrNull()?.absolutePath.orEmpty()
    }

    override fun supportExtension(): List<String> {
        return listOf("mobi")
    }

    override fun destroy() {

    }
}