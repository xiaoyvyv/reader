package com.xiaoyv.comic.reader.config.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.xiaoyv.comic.datasource.impl.BookDataSource
import com.xiaoyv.comic.datasource.impl.archive.ArchiveBookDataSource
import com.xiaoyv.comic.datasource.impl.djvu.DjvuBookDataSource
import com.xiaoyv.comic.datasource.impl.epub.EpubBookDataSource
import com.xiaoyv.comic.datasource.impl.mobi.MobiBookDataSource
import com.xiaoyv.comic.datasource.impl.pdf.PdfBookDataSource
import com.xiaoyv.comic.datasource.utils.FileExtension.readerFileExtensions
import com.xiaoyv.comic.reader.application
import com.xiaoyv.comic.reader.data.entity.FileEntity
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream


/**
 * [FileEntityLoader]
 *
 * @author why
 * @since 4/29/24
 */
class FileEntityLoader : ModelLoader<FileEntity, InputStream> {
    override fun buildLoadData(
        model: FileEntity,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream> {
        return ModelLoader.LoadData(ObjectKey(model), MobiCoverDataFetcher(model))
    }

    override fun handles(model: FileEntity): Boolean {
        return readerFileExtensions.contains(model.extension.lowercase())
    }

    class Factory : ModelLoaderFactory<FileEntity, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<FileEntity, InputStream> {
            return FileEntityLoader()
        }

        override fun teardown() {
            // 可以在这里清理资源
        }
    }
}

class MobiCoverDataFetcher(private val model: FileEntity) : DataFetcher<InputStream> {
    private var callback: DataFetcher.DataCallback<in InputStream>? = null
    private var bookDataSource: BookDataSource? = null
    private var stream: InputStream? = null

    override fun loadData(priority: Priority, callback: DataFetcher.DataCallback<in InputStream>) {
        if (!model.file.canRead()) {
            callback.onLoadFailed(IOException("Permission Denied"))
            return
        }

        this.callback = callback

        runCatching {
            when (model.extension.lowercase()) {
                "epub" -> {
                    bookDataSource = EpubBookDataSource(application, model.file).apply {
                        load()
                        stream = FileInputStream(getCover())
                        callback.onDataReady(stream)
                    }
                }

                "mobi" -> {
                    bookDataSource = MobiBookDataSource(application, model.file).apply {
                        load()
                        stream = FileInputStream(getCover())
                        callback.onDataReady(stream)
                    }
                }

                "pdf" -> {
                    bookDataSource = PdfBookDataSource(application, model.file).apply {
                        load()
                        stream = FileInputStream(getCover())
                        callback.onDataReady(stream)
                    }
                }

                "djvu" -> {
                    bookDataSource = DjvuBookDataSource(application, model.file).apply {
                        load()
                        stream = FileInputStream(getCover())
                        callback.onDataReady(stream)
                    }
                }

                "rar", "zip", "cbr", "cbz" -> {
                    bookDataSource = ArchiveBookDataSource(application, model.file).apply {
                        load()
                        stream = FileInputStream(getCover())
                        callback.onDataReady(stream)
                    }
                }

                else -> {
                    error("Not support! ${model.file}")
                }
            }
        }.onFailure {
            callback.onLoadFailed(IOException("Glide fetch resource error for ${model.file}", it))
        }
    }

    override fun cleanup() {
        runCatching { stream?.close() }
        runCatching { bookDataSource?.destroy() }

        callback = null
    }

    override fun cancel() {
        // 可以在这里取消加载操作
    }

    override fun getDataClass(): Class<InputStream> {
        return InputStream::class.java
    }

    override fun getDataSource(): DataSource {
        return DataSource.LOCAL
    }
}