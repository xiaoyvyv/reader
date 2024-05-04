package com.xiaoyv.comic.reader.config.glide

import android.graphics.Bitmap
import com.bumptech.glide.Priority
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.data.DataFetcher
import com.bumptech.glide.load.model.ModelLoader
import com.bumptech.glide.load.model.ModelLoaderFactory
import com.bumptech.glide.load.model.MultiModelLoaderFactory
import com.bumptech.glide.signature.ObjectKey
import com.xiaoyv.comic.datasource.BookDataSource
import com.xiaoyv.comic.datasource.BookModel
import com.xiaoyv.comic.datasource.BookPage
import com.xiaoyv.comic.datasource.FileBookModel
import com.xiaoyv.comic.reader.ui.utils.debugLog
import java.io.IOException

/**
 * [BookPageLoader]
 *
 * @author why
 * @since 5/1/24
 */
class BookPageLoader :
    ModelLoader<BookPage<FileBookModel, out BookDataSource<FileBookModel>>, Bitmap> {
    override fun buildLoadData(
        model: BookPage<FileBookModel, out BookDataSource<FileBookModel>>,
        width: Int,
        height: Int,
        options: Options
    ): ModelLoader.LoadData<Bitmap> {
        return ModelLoader.LoadData(
            ObjectKey(model.dataSource.model.file.absolutePath + "?page=" + model.page),
            BookPageDataFetcher(model)
        )
    }

    override fun handles(model: BookPage<FileBookModel, out BookDataSource<FileBookModel>>): Boolean {
        return true
    }

    @Suppress("UNCHECKED_CAST")
    class Factory : ModelLoaderFactory<BookPage<*, *>, Bitmap> {
        override fun build(multiFactory: MultiModelLoaderFactory): ModelLoader<BookPage<*, out BookDataSource<*>>, Bitmap> {
            return BookPageLoader() as ModelLoader<BookPage<*, out BookDataSource<*>>, Bitmap>
        }

        override fun teardown() {
            // 可以在这里清理资源
        }
    }

    class BookPageDataFetcher(private val model: BookPage<FileBookModel, out BookDataSource<FileBookModel>>) :
        DataFetcher<Bitmap> {

        override fun loadData(
            priority: Priority,
            callback: DataFetcher.DataCallback<in Bitmap>
        ) {
            runCatching {
                callback.onDataReady(model.renderPage())
            }.onFailure {
                debugLog { "图片加载失败：${model.page}" }
                callback.onLoadFailed(IOException(it))
            }
        }

        override fun cleanup() {
            runCatching { model.destroy() }
        }

        override fun cancel() {

        }

        override fun getDataClass(): Class<Bitmap> {
            return Bitmap::class.java
        }

        override fun getDataSource(): DataSource {
            return DataSource.LOCAL
        }
    }
}
