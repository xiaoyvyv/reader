package com.xiaoyv.comic.reader.data.entity

import android.os.Parcelable
import com.xiaoyv.comic.datasource.BookModel
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue

/**
 * [BookEntity]
 *
 * @author why
 * @since 5/5/24
 */
@Parcelize
data class BookEntity(
    var model: @RawValue BookModel,
    var id: String = "",
    var seriesId: String = "",
    var seriesTitle: String = "",
    var title: String = "",
    var cover: String = "",
) : Parcelable
