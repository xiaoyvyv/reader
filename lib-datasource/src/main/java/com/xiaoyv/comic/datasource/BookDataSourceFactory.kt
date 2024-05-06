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
        var dataSource: BookDataSource<out BookModel>? = null

        if (bookModel is RemoteBookModel) {
            dataSource = RemoteDataSource(application, bookModel) as BookDataSource<BookModel>
        } else if (bookModel is FileBookModel) {
            when (bookModel.file.extension.lowercase()) {
                "epub" -> dataSource = EpubBookDataSource(application, bookModel)
                "mobi" -> dataSource = MobiBookDataSource(application, bookModel)
                "pdf" -> dataSource = PdfBookDataSource(application, bookModel)
                "djvu" -> dataSource = DjvuBookDataSource(application, bookModel)
                "rar", "zip", "cbr", "cbz" -> {
                    dataSource = ArchiveBookDataSource(application, bookModel)
                }
            }
        }

        return requireNotNull(dataSource) { "Not support! $bookModel" } as BookDataSource<BookModel>
    }
}