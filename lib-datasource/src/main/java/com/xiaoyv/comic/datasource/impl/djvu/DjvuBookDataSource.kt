package com.xiaoyv.comic.datasource.impl.djvu

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.Keep
import com.xiaoyv.comic.datasource.impl.BookDataSource
import com.xiaoyv.comic.datasource.jni.NativeContext
import com.xiaoyv.comic.datasource.utils.createDir
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.io.File
import java.io.FileOutputStream

/**
 * [DjvuBookDataSource]
 *
 * @author why
 * @since 4/30/24
 */
@Keep
class DjvuBookDataSource(override val context: Context, override val file: File) : BookDataSource {
    private var contextHandle: Long = 0
    private var docHandle: Long = 0

    /**
     * DJVU Render Mode
     *
     * - 0 color
     * - 1 black
     * - 2 color only
     * - 3 mask
     * - 4 backgroud,
     * - 5 foreground
     */
    private var renderMode = 0

    private val saveDir by lazy { createDataDir() }

    @Synchronized
    private external fun create(): Long

    @Synchronized
    private external fun free(contextHandle: Long)

    override fun load() {
        contextHandle = create()
        docHandle = DjvuBookDocument.open(contextHandle, file.absolutePath)
    }

    override fun getCover(): String {
        require(getPageCount() > 0) { "No content" }
        val pageHandle = DjvuBookDocument.getPage(docHandle, 1)

        require(pageHandle != 0L) { "Page read fail!" }

        var width = 0
        var height = 0
        var count = 0
        while ((width == 0 || height == 0) && count < 5) {
            width = DjvuBookPage.getWidth(pageHandle)
            height = DjvuBookPage.getHeight(pageHandle)
            count++
            runBlocking { delay(50) }
        }

        require(width > 0 && height > 0) { "Page size error! [w=$width, h=$height]" }

        // 注意这里  Bitmap.Config.RGB_565
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565)
        val result = DjvuBookPage.renderPageBitmap(
            pageHandle = pageHandle,
            contextHandle = contextHandle,
            targetWidth = width,
            targetHeight = height,
            pageSliceX = 0,
            pageSliceY = 0,
            pageSliceWidth = width,
            pageSliceHeight = height,
            bitmap = bitmap,
            renderMode = renderMode
        )

        require(result) { "Render cover error" }

        val coverDir = createDir(saveDir.absolutePath + "/cover")
        val coverFileName = "$fileNameWithoutExtension.png"
        val coverFilePath = coverDir.absolutePath + "/" + coverFileName

        FileOutputStream(coverFilePath).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            bitmap.recycle()
        }

        DjvuBookPage.free(pageHandle)

        return coverFilePath
    }

    override fun getPageCount(): Int {
        require(contextHandle != 0L && docHandle != 0L) { "Djvu open fail!" }
        return DjvuBookDocument.getPageCount(docHandle)
    }

    override fun supportExtension(): List<String> {
        return listOf("djvu")
    }

    override fun destroy() {
        DjvuBookDocument.free(docHandle)
        docHandle = 0

        free(contextHandle)
        contextHandle = 0
    }

    companion object {
        init {
            NativeContext.init()
        }
    }
}