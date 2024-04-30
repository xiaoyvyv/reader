package com.xiaoyv.comic.datasource.impl.pdf

import android.content.Context
import android.graphics.Bitmap
import com.artifex.mupdf.fitz.Document
import com.artifex.mupdf.fitz.Matrix
import com.artifex.mupdf.fitz.android.AndroidDrawDevice
import com.xiaoyv.comic.datasource.impl.BookDataSource
import com.xiaoyv.comic.datasource.utils.createDir
import java.io.File
import java.io.FileOutputStream
import kotlin.math.abs
import kotlin.math.roundToInt

/**
 * [PdfBookDataSource]
 *
 * @author why
 * @since 4/29/24
 */
class PdfBookDataSource(
    override val context: Context,
    override val file: File
) : BookDataSource {
    private var document: Document? = null
    private var pageCount: Int = 0
    private var needsPassword: Boolean = false

    private val saveDir by lazy { createDataDir() }

    override fun load() {
        document = Document.openDocument(file.absolutePath).apply {
            pageCount = countPages()
            needsPassword = needsPassword()
        }
    }


    override fun getCover(): String {
        require(pageCount > 0) { "No content" }
        require(!needsPassword) { "Needs password" }
        val doc = requireNotNull(document)
        val page = doc.loadPage(1)
        val scale = 1f
        val matrix = Matrix()
        val bitmap = Bitmap.createBitmap(
            abs(page.bounds.x1 * scale - page.bounds.x0 * scale).roundToInt(),
            abs(page.bounds.y1 * scale - page.bounds.y0 * scale).roundToInt(),
            Bitmap.Config.ARGB_8888
        )
        val device = AndroidDrawDevice(bitmap)
        matrix.scale(scale)
        page.run(device, matrix)

        val coverDir = createDir(saveDir.absolutePath + "/cover")
        val coverFileName = "$fileNameWithoutExtension.png"
        val coverFilePath = coverDir.absolutePath + "/" + coverFileName

        FileOutputStream(coverFilePath).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            bitmap.recycle()
        }
        device.destroy()
        return coverFilePath
    }

    override fun supportExtension(): List<String> {
        return listOf("pdf")
    }

    override fun destroy() {
        runCatching {
            document?.destroy()
            document = null
        }
    }
}