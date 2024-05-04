package com.xiaoyv.comic.datasource.impl.pdf

import android.content.Context
import android.graphics.Bitmap
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.annotation.CallSuper
import com.artifex.mupdf.fitz.Document
import com.xiaoyv.comic.datasource.BookDataSource
import com.xiaoyv.comic.datasource.BookMetaData
import com.xiaoyv.comic.datasource.BookModel
import com.xiaoyv.comic.datasource.BookPage
import com.xiaoyv.comic.datasource.FileBookModel
import com.xiaoyv.comic.datasource.utils.TempHolder
import com.xiaoyv.comic.datasource.utils.createDir
import com.xiaoyv.comic.datasource.utils.lastModifiedText
import java.io.File
import java.io.FileOutputStream

/**
 * [PdfBookDataSource]
 *
 * @author why
 * @since 4/29/24
 */
open class PdfBookDataSource(
    override val context: Context,
    override val model: FileBookModel
) : BookDataSource<FileBookModel> {
    internal val lock = Any()

    internal var document: Document? = null
    internal var needsPassword: Boolean = false

    internal val saveDir by lazy { createDataDir() }

    override var pageCount: Int = 0

    val displayMetrics by lazy {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics
    }


    @CallSuper
    override fun load() {
        TempHolder.lock.lock()
        try {
            document = Document.openDocument(model.file.absolutePath).apply {
                if (isReflowable) {
                    val screenWidth = displayMetrics.widthPixels
                    val screenHeight = screenWidth
//                    val screenHeight = screenWidth / (1620 / 2160f)
                    val densityDpi = displayMetrics.densityDpi

                    val layoutW = screenWidth * 72f / densityDpi
                    val layoutH = screenHeight * 72f / densityDpi

//                    layout(layoutW, layoutH, 2f)
                }
                pageCount = countPages()
                needsPassword = needsPassword()
            }
        } finally {
            TempHolder.lock.unlock()
        }
    }

    override fun getCover(): String {
        require(pageCount > 0) { "No content" }
        require(!needsPassword) { "Needs password" }

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

    override fun getPage(page: Int): BookPage<FileBookModel,PdfBookDataSource> {
        return PdfBookPage(dataSource = this, page = page)
    }

    override fun getMetaInfo(): BookMetaData {
        val doc = requireNotNull(document)
        return BookMetaData(
            title = doc.getMetaData(Document.META_INFO_TITLE).orEmpty()
                .ifBlank { model.file.nameWithoutExtension },
            subject = doc.getMetaData(Document.META_INFO_SUBJECT)
                .orEmpty()
                .ifBlank { model.file.absolutePath },
            author = doc.getMetaData(Document.META_INFO_AUTHOR)
                .orEmpty()
                .ifBlank { "未知" },
            format = doc.getMetaData(Document.META_FORMAT)
                .orEmpty()
                .ifBlank { model.file.extension.uppercase() },
            encryption = doc.getMetaData(Document.META_ENCRYPTION)
                .orEmpty(),
            keywords = doc.getMetaData(Document.META_INFO_KEYWORDS)
                .orEmpty()
                .ifBlank { model.file.nameWithoutExtension },
            creator = doc.getMetaData(Document.META_INFO_CREATOR)
                .orEmpty(),
            producer = doc.getMetaData(Document.META_INFO_PRODUCER)
                .orEmpty(),
            creationDate = doc.getMetaData(Document.META_INFO_CREATIONDATE)
                .orEmpty()
                .ifBlank { model.file.lastModifiedText() },
            modDate = doc.getMetaData(Document.META_INFO_MODIFICATIONDATE)
                .orEmpty()
                .ifBlank { model.file.lastModifiedText() },
        )
    }

    override fun supportExtension(): List<String> {
        return listOf("pdf")
    }

    override fun destroy() {
        runCatching {
            TempHolder.lock.lock()
            try {
                document?.destroy()
                document = null
            } finally {
                TempHolder.lock.unlock()
            }
        }
    }
}