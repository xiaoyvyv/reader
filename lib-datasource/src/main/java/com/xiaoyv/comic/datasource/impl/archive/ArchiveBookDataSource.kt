package com.xiaoyv.comic.datasource.impl.archive

import android.content.Context
import com.xiaoyv.comic.datasource.impl.BookDataSource
import com.xiaoyv.comic.datasource.utils.FileExtension.imageExtensions
import com.xiaoyv.comic.datasource.utils.createDir
import java.io.File
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
    override val file: File
) : BookDataSource {
    private var zipFile: ZipFile? = null
    private var rarFileHandle: Long = 0

    private val saveDir by lazy { createDataDir() }

    override fun load() {
        when (file.extension.lowercase()) {
            "cbr" -> {
                rarFileHandle = ArchiveBookUnRar.loadFile(file.absolutePath)
            }

            "cbz" -> {
                zipFile = ZipFile(file)
            }

            "cbt" -> {

            }

            "cb7" -> {

            }
        }
    }

    override fun getCover(): String {
        when (file.extension.lowercase()) {
            "cbr" -> {
                require(rarFileHandle != 0L) { "open rar fail!" }
                val map = ArchiveBookUnRar.getPages(rarFileHandle)

                require(map.isNotEmpty()) { "No content" }
                val sortedMap = map.entries.sortedBy { it.value }
                val (firstPageIndex, firstPageName) = sortedMap.first()

                val coverDir = createDir(saveDir.absolutePath + "/cover")
                val coverFilePath = coverDir.absolutePath + "/" + firstPageName

                ArchiveBookUnRar.extractPage(rarFileHandle, firstPageIndex, coverFilePath)

                return coverFilePath
            }

            "cbz" -> {
                val zip = requireNotNull(zipFile)
                val entries = zip.entries().toList()
                    .filter { imageExtensions.contains(it.name.substringAfterLast(".").lowercase()) }
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
                ArchiveBookUnRar.free(rarFileHandle)
                rarFileHandle = 0
            }
        }
    }
}