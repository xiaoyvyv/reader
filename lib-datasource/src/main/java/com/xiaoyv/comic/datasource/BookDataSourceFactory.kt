@file:Suppress("UNCHECKED_CAST")

package com.xiaoyv.comic.datasource

import android.app.Application
import com.xiaoyv.comic.datasource.impl.archive.ArchiveBookDataSource
import com.xiaoyv.comic.datasource.impl.djvu.DjvuBookDataSource
import com.xiaoyv.comic.datasource.impl.epub.EpubBookDataSource
import com.xiaoyv.comic.datasource.impl.mobi.MobiBookDataSource
import com.xiaoyv.comic.datasource.impl.pdf.PdfBookDataSource
import com.xiaoyv.comic.datasource.impl.remote.RemoteDataSource
import java.io.File

/**
 * [BookDataSourceFactory]
 *
 * @author why
 * @since 5/1/24
 */
object BookDataSourceFactory {

    fun create(application: Application, bookModel: BookModel): BookDataSource<BookModel> {
        if (bookModel is RemoteBookModel) {
            return RemoteDataSource(application, bookModel) as BookDataSource<BookModel>
        }

        if (bookModel is FileBookModel) {
            when (bookModel.file.extension.lowercase()) {
                "epub" -> {
                    return EpubBookDataSource(application, bookModel) as BookDataSource<BookModel>
                }

                "mobi" -> {
                    return MobiBookDataSource(application, bookModel) as BookDataSource<BookModel>
                }

                "pdf" -> {
                    return PdfBookDataSource(application, bookModel) as BookDataSource<BookModel>
                }

                "djvu" -> {
                    return DjvuBookDataSource(application, bookModel) as BookDataSource<BookModel>
                }

                "rar", "zip", "cbr", "cbz" -> {
                    return ArchiveBookDataSource(
                        application,
                        bookModel
                    ) as BookDataSource<BookModel>
                }
            }
        }

        error("Not support! $bookModel")
    }
}