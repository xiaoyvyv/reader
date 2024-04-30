package com.xiaoyv.comic.reader.data.entity

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * [BookEntity]
 *
 * @author why
 * @since 4/27/24
 */
@Parcelize
data class BookEntity(
    var id: Long = 0,
    var name: String = "",
    var author: String = "",
    var uri: Uri? = null,
    var size: Long = 0,
    var progress: Double = 0.0,
    var createAt: Long = System.currentTimeMillis(),
    var readAt: Long = 0,
    var type: String = ""
) : Parcelable
