package com.xiaoyv.comic.datasource

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * [BookMetaData]
 *
 * @author why
 * @since 5/2/24
 */
@Parcelize
data class BookMetaData(
    var format: String = "",
    var encryption: String = "",
    var author: String = "",
    var title: String = "",
    var subject: String = "",
    var keywords: String = "",
    var creator: String = "",
    var producer: String = "",
    var creationDate: String = "",
    var modDate: String = "",
) : Parcelable

