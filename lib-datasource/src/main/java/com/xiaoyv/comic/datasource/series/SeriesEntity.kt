package com.xiaoyv.comic.datasource.series

import com.xiaoyv.comic.datasource.book.BookMetaData
import com.xiaoyv.comic.datasource.book.BookModel
import java.util.Date

/**
 * [SeriesInfo]
 *
 * @author why
 * @since 5/6/24
 */
open class SeriesInfo(
    open var id: String? = null,
    open var typeId: String? = null,
    open var typeName: String? = null,
    open var cover: String? = null,
    open var title: String? = null,
    open var status: String? = null,
    open var language: String? = null,
    open var booksCount: Int = 0,
    open var summary: String? = null,
    open var created: Date? = null,
    open var publisher: String? = null,
    open var tags: String? = null,
    open var authors: String? = null,
    open var links: String? = null,
    open var books: List<SeriesBook> = emptyList()
)

data class SeriesBook(
    val cover: String,
    val title: String,
    val number: Int,
    val book: BookModel,
    val bookMeta: BookMetaData
)
