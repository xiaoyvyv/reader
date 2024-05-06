package com.xiaoyv.comic.datasource.book.epub

import android.content.Context
import com.xiaoyv.comic.datasource.book.FileBookModel
import com.xiaoyv.comic.datasource.book.pdf.PdfBookDataSource
import com.xiaoyv.comic.datasource.utils.createDir
import org.jsoup.Jsoup
import java.io.FileOutputStream
import java.util.zip.ZipFile

/**
 * [EpubBookDataSource]
 *
 * @author why
 * @since 4/29/24
 */
class EpubBookDataSource(
    override val context: Context,
    override val model: FileBookModel
) : PdfBookDataSource(context, model) {
    private var zipFile: ZipFile? = null

    override fun load() {
        super.load()
        zipFile = ZipFile(model.file)
    }

    override fun getCover(): String {
        val zip = zipFile ?: return ""
        val coverDir = createDir(saveDir.absolutePath + "/cover")
        val container = zip.getEntry("META-INF/container.xml")
        val containerText = zip.getInputStream(container).readBytes().decodeToString()
        val rootPath = Jsoup.parse(containerText).select("rootfile").attr("full-path")
        val volOpf = zip.getEntry(rootPath)
        val volOpfText = zip.getInputStream(volOpf).readBytes().decodeToString()
        val volOpfDoc = Jsoup.parse(volOpfText)
        val coverId = volOpfDoc.select("meta[name=cover]").attr("content")
        val cover = volOpfDoc.select("item[id=$coverId]").attr("href")
        val stream = zip.getInputStream(zip.getEntry(cover))
        val coverFileName = cover.substringAfter("/")
        val coverFilePath = coverDir.absolutePath + "/" + coverFileName
        FileOutputStream(coverFilePath).use { out ->
            stream.use { it.copyTo(out) }
        }
        return coverFilePath
    }

    override fun supportExtension(): List<String> {
        return listOf("epub")
    }

    override fun destroy() {
        runCatching {
            zipFile?.close()
            zipFile = null
        }
    }

}