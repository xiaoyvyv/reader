package com.xiaoyv.comic.datasource.book.djvu

import android.content.Context
import android.graphics.Bitmap
import androidx.annotation.Keep
import com.xiaoyv.comic.datasource.book.BookDataSource
import com.xiaoyv.comic.datasource.book.BookMetaData
import com.xiaoyv.comic.datasource.book.BookPage
import com.xiaoyv.comic.datasource.book.FileBookModel
import com.xiaoyv.comic.datasource.utils.NativeContext
import com.xiaoyv.comic.datasource.utils.TempHolder
import com.xiaoyv.comic.datasource.utils.createDir
import com.xiaoyv.comic.datasource.utils.lastModifiedText
import java.io.File
import java.io.FileOutputStream

/**
 * [DjvuBookDataSource]
 *
 * @author why
 * @since 4/30/24
 */
@Keep
class DjvuBookDataSource(
    override val context: Context,
    override val model: FileBookModel
) : BookDataSource<FileBookModel> {
    internal var contextHandle: Long = 0
    internal var docHandle: Long = 0

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
    internal var renderMode = 0

    override var pageCount: Int = 0

    internal val saveDir by lazy { createDataDir() }

    @Synchronized
    private external fun create(): Long

    @Synchronized
    private external fun free(contextHandle: Long)

    override fun load() {
        TempHolder.lock.lock()
        try {
            contextHandle = create()
            docHandle = DjvuBookDocument.open(contextHandle, model.file.absolutePath)
            require(docHandle != 0L)
            pageCount = DjvuBookDocument.getPageCount(docHandle)
        } finally {
            TempHolder.lock.unlock()
        }
    }

    override fun getCover(): String {
        val coverDir = createDir(saveDir.absolutePath + "/cover")
        val coverFileName = "${model.file.nameWithoutExtension}.png"
        val coverFilePath = coverDir.absolutePath + "/" + coverFileName

        // 缓存数据
        if (File(coverFilePath).let { it.isFile && it.exists() }) {
            return coverFilePath
        }

        val bitmap = getPage(1).initPageMeta().renderPage()

        FileOutputStream(coverFilePath).use { out ->
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)
            bitmap.recycle()
        }

        return coverFilePath
    }

    override fun getPage(page: Int): BookPage<FileBookModel, DjvuBookDataSource> {
        return DjvuBookPage(this, page)
    }

    override fun getMetaInfo(): BookMetaData {
        require(docHandle != 0L)

        val metaInfos = DjvuBookDocument.getMetaKeys(docHandle)
            .orEmpty()
            .split(",")
            .filter { it.isNotEmpty() }
            .map { it to DjvuBookDocument.getMeta(docHandle, it) }

        val subject = metaInfos.joinToString("\n") { it.first + ":" + it.second.orEmpty() }

        return BookMetaData(
            title = model.file.nameWithoutExtension,
            subject = subject.ifBlank { model.file.absolutePath },
            author = "未知",
            format = model.file.extension.uppercase(),
            creator = "未知",
            producer = "",
            creationDate = model.file.lastModifiedText(),
            modDate = model.file.lastModifiedText()
        )
    }

    override fun supportExtension(): List<String> {
        return listOf("djvu")
    }

    override fun destroy() {
        TempHolder.lock.lock()
        try {
            if (docHandle != 0L) {
                docHandle = 0
                DjvuBookDocument.free(docHandle)
            }

            if (contextHandle != 0L) {
                free(contextHandle)
                contextHandle = 0
            }
        } finally {
            TempHolder.lock.unlock()
        }
    }

    companion object {
        init {
            NativeContext.init()
        }
    }
}