package com.xiaoyv.comic.datasource.book.archive

import android.content.Context
import com.xiaoyv.comic.datasource.book.BookDataSource
import com.xiaoyv.comic.datasource.book.BookMetaData
import com.xiaoyv.comic.datasource.book.BookPage
import com.xiaoyv.comic.datasource.book.FileBookModel
import com.xiaoyv.comic.datasource.utils.FileExtension.imageExtensions
import com.xiaoyv.comic.datasource.utils.createDir
import java.io.FileOutputStream
import java.util.zip.ZipFile

/**
 * [ArchiveBookDataSource]
 *
 * @author why
 * @since 4/30/24
 */
class ArchiveBookDataSource(
    override val context: Context,
    override val model: FileBookModel
) : BookDataSource<FileBookModel> {
    private var zipFile: ZipFile? = null
    private var rarFileHandle: Long = 0

    override var pageCount: Int = 0

    private val saveDir by lazy { createDataDir() }

    override fun load() {
        when (model.file.extension.lowercase()) {
            "cbr" -> {
                rarFileHandle =
                    com.xiaoyv.comic.datasource.book.archive.ArchiveBookUnRar.loadFile(model.file.absolutePath)
            }

            "cbz" -> {
                zipFile = ZipFile(model.file)
            }

            "cbt" -> {

            }

            "cb7" -> {

            }
        }
    }

    override fun getCover(): String {
        when (model.file.extension.lowercase()) {
            "cbr" -> {
                require(rarFileHandle != 0L) { "open rar fail!" }
                val map = com.xiaoyv.comic.datasource.book.archive.ArchiveBookUnRar.getPages(
                    rarFileHandle
                )

                require(map.isNotEmpty()) { "No content" }
                val sortedMap = map.entries.sortedBy { it.value }
                val (firstPageIndex, firstPageName) = sortedMap.first()

                val coverDir = createDir(saveDir.absolutePath + "/cover")
                val coverFilePath = coverDir.absolutePath + "/" + firstPageName

                com.xiaoyv.comic.datasource.book.archive.ArchiveBookUnRar.extractPage(
                    rarFileHandle,
                    firstPageIndex,
                    coverFilePath
                )

                return coverFilePath
            }

            "cbz" -> {
                val zip = requireNotNull(zipFile)
                val entries = zip.entries().toList()
                    .filter {
                        imageExtensions.contains(
                            it.name.substringAfterLast(".").lowercase()
                        )
                    }
                    .sortedBy { it.name }

                require(entries.isNotEmpty()) { "No content" }
                val entry = entries.first()
                val coverDir = createDir(saveDir.absolutePath + "/cover")
                val coverFilePath = coverDir.absolutePath + "/" + entry.name.substringAfter("/")

                zip.getInputStream(entry).use { stream ->
                    FileOutputStream(coverFilePath).use { out ->
                        stream.copyTo(out)
                    }
                }

                return coverFilePath
            }

            "cbt" -> {

            }
        }
        return ""
    }

    override fun getPage(page: Int): BookPage<FileBookModel, ArchiveBookDataSource> {
        return com.xiaoyv.comic.datasource.book.archive.ArchiveBookPage(this)
    }

    override fun getMetaInfo(): BookMetaData {
        TODO("Not yet implemented")
    }

    override fun supportExtension(): List<String> {
        return listOf("cbr", "cbz", "cbt", "zip", "tar", "rar")
    }

    override fun destroy() {
        runCatching {
            zipFile?.close()
            zipFile = null
        }

        runCatching {
            if (rarFileHandle != 0L) {
                com.xiaoyv.comic.datasource.book.archive.ArchiveBookUnRar.free(rarFileHandle)
                rarFileHandle = 0
            }
        }
    }
}