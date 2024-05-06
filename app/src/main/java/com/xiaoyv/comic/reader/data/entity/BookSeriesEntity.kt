package com.xiaoyv.comic.reader.data.entity

import android.os.Parcelable
import com.xiaoyv.comic.datasource.BookMetaData
import com.xiaoyv.comic.datasource.BookModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * [BookSeriesEntity]
 *
 * @author why
 * @since 4/27/24
 */
@Parcelize
data class BookSeriesEntity(
    var id: Long = 0,
    var name: String = "",
    var author: String = "",
    var size: Long = 0,
    var progress: Double = 0.0,
    var createAt: Long = System.currentTimeMillis(),
    var readAt: Long = 0,
    var type: String = "",
    var metaData: BookMetaData = BookMetaData(),
    var model: @RawValue BookModel? = null,
    var cover: String = "",
    var books: List<BookEntity> = emptyList()
) : Parcelable
