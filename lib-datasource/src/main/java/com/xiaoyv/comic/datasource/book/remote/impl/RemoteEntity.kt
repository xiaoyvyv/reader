package com.xiaoyv.comic.datasource.book.remote.impl

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.xiaoyv.comic.datasource.series.SeriesBook
import com.xiaoyv.comic.datasource.series.SeriesInfo
import kotlinx.parcelize.Parcelize
import java.util.Date

/**
 * [RemoteLibraryEntity]
 *
 * @author why
 * @since 5/4/24
 */
@Parcelize
data class RemoteLibraryEntity(
    var config: RemoteLibraryConfig,
    var id: String = "",
    var title: String = ""
) : Parcelable

@Parcelize
data class RemoteBookUserEntity(
    var username: String = "",
    var id: String = "",
    var roles: List<String> = emptyList(),
) : Parcelable

class RemoteSeriesInfo(
    val config: RemoteLibraryConfig,
    id: String? = null,
    typeId: String? = null,
    typeName: String? = null,
    cover: String? = null,
    title: String? = null,
    status: String? = null,
    language: String? = null,
    booksCount: Int = 0,
    summary: String? = null,
    created: Date? = null,
    publisher: String? = null,
    tags: String? = null,
    authors: String? = null,
    links: String? = null,
    books: List<SeriesBook> = emptyList()
) : SeriesInfo(
    id,
    typeId,
    typeName,
    cover,
    title,
    status,
    language,
    booksCount,
    summary,
    created,
    publisher,
    tags,
    authors,
    links,
    books
)

@Parcelize
data class RemoteBookDetailEntity(
    var config: RemoteLibraryConfig,
    var id: String? = null,
    var typeId: String? = null,
    var title: String = "",
    var summary: String? = null,
    var metaTitle: String = "",
    var seriesId: String? = null,
    var seriesTitle: String? = null,
    var authors: String? = null,
    var publisher: String? = null,
    var created: Date? = null,
    var lastModified: Date? = null,
    var number: Int = 0,
    var sizeBytes: Long = 0,
    var format: String = "",
    var pageCount: Int = 0,
    var status: String = "",
    var cover: String = "",
    var pages: List<Page>? = null,
) : Parcelable {

    @Parcelize
    data class Page(
        var number: Int = 0,
        var url: String = "",
        var width: Int = 0,
        var height: Int = 0,
        var mime: String = ""
    ) : Parcelable
}