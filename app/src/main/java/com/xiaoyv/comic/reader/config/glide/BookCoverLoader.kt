package com.xiaoyv.comic.reader.config.glide

import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.xiaoyv.comic.datasource.BookDataSource
import com.xiaoyv.comic.datasource.BookDataSourceFactory
import com.xiaoyv.comic.datasource.BookModel
import com.xiaoyv.comic.datasource.FileBookModel
import com.xiaoyv.comic.datasource.utils.FileExtension.readerFileExtensions
import com.xiaoyv.comic.reader.application
import java.io.File
import java.io.FileInputStream
import java.io.IOException
import java.io.InputStream


/**
 * [BookCoverLoader]
 *
 * @author why
 * @since 4/29/24
 */
class BookCoverLoader : ModelLoader<String, InputStream> {
    override fun buildLoadData(
        model: String,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<InputStream> {
        return ModelLoader.LoadData(ObjectKey(model), MobiCoverDataFetcher(model))
    }

    override fun handles(model: String): Boolean {
        if (model.startsWith("file://") || model.startsWith("/")) {
            return readerFileExtensions.contains(
                model.substringAfterLast("/")
                    .substringAfterLast(".")
                    .lowercase()
            )
        }
        return false
    }

    class Factory : ModelLoaderFactory<String, InputStream> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<String, InputStream> {
            return BookCoverLoader()
        }

        override fun teardown() {
            // 可以在这里清理资源
        }
    }

    class MobiCoverDataFetcher(private val model: String) : DataFetcher<InputStream> {
        private var callback: DataFetcher.DataCallback<in InputStream>? = null
        private var bookDataSource: BookDataSource<out BookModel>? = null
        private var stream: InputStream? = null


        override fun loadData(
            priority: Priority,
            callback: DataFetcher.DataCallback<in InputStream>
        ) {
            val file = File(model)
            if (!file.canRead()) {
                callback.onLoadFailed(IOException("Permission Denied"))
                return
            }

            this.callback = callback

            runCatching {
                bookDataSource =
                    BookDataSourceFactory.create(application, FileBookModel(file.absolutePath))
                        .apply {
                            load()
                            stream = FileInputStream(getCover())
                            callback.onDataReady(stream)
                        }
            }.onFailure {
                callback.onLoadFailed(
                    IOException(
                        "Glide fetch resource error for $file",
                        it
                    )
                )
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
}
