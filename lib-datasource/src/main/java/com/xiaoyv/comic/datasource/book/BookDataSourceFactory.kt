@file:Suppress("UNCHECKED_CAST")

package com.xiaoyv.comic.datasource.book

import android.content.Context
import com.xiaoyv.comic.datasource.book.djvu.DjvuBookDataSource
import com.xiaoyv.comic.datasource.book.epub.EpubBookDataSource
import com.xiaoyv.comic.datasource.book.mobi.MobiBookDataSource
import com.xiaoyv.comic.datasource.book.pdf.PdfBookDataSource
import com.xiaoyv.comic.datasource.book.remote.RemoteDataSource

/**
 * [BookDataSourceFactory]
 *
 * @author why
 * @since 5/1/24
 */
object BookDataSourceFactory {

    @JvmStatic
    fun create(context: Context, bookModel: BookModel): BookDataSource<BookModel> {
        var dataSource: BookDataSource<out BookModel>? = null
        when (bookModel) {
            // 远程数据源
            is RemoteBookModel -> {
                dataSource = RemoteDataSource(context, bookModel) as BookDataSource<BookModel>
            }
            // 本地数据源
            is FileBookModel -> {
                when (bookModel.file.extension.lowercase()) {
                    "epub" -> dataSource = EpubBookDataSource(context, bookModel)
                    "mobi" -> dataSource = MobiBookDataSource(context, bookModel)
                    "pdf" -> dataSource = PdfBookDataSource(context, bookModel)
                    "djvu" -> dataSource = DjvuBookDataSource(context, bookModel)
                    "rar", "zip", "cbr", "cbz" -> {
                        dataSource =
                            com.xiaoyv.comic.datasource.book.archive.ArchiveBookDataSource(
                                context,
                                bookModel
                            )
                    }
                }
            }
        }

        return requireNotNull(dataSource) { "Not support! $bookModel" } as BookDataSource<BookModel>
    }
}