package com.xiaoyv.comic.datasource.remote

import android.os.Parcelable
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
    var id: String = "",
    var title: String = ""
) : Parcelable

@Parcelize
data class RemoteBookSeriesEntity(
    var config: RemoteLibraryConfig,
    var id: String = "",
    var libraryId: String = "",
    var name: String = "",
    var cover: String = "",
    var bookCount: Int = 0,
    var title: String = "",
    var summary: String = "",
    var publisher: String = "",
    var genres: List<String> = emptyList(),
    var authors: List<String> = emptyList(),
    var language: String = "",
    var status: String = "",
    var created: Date? = null,
    var lastModified: Date? = null,
    var tags: List<String>? = null,
    var alternateTitles: List<String>? = null,
    var books: List<RemoteBookDetailEntity>? = null
) : Parcelable

@Parcelize
data class RemoteBookDetailEntity(
    var config: RemoteLibraryConfig,
    var id: String? = null,
    var title: String = "",
    var seriesId: String? = null,
    var seriesTitle: String? = null,
    var libraryId: String? = null,
    var number: Int = 0,
    var sizeBytes: Long = 0,
    var type: String = "",
    var pageCount: Int = 0,
    var status: String = "",
    var cover: String = "",
) : Parcelable

@Parcelize
data class RemoteBookUserEntity(
    var username: String = "",
    var id: String = "",
    var roles: List<String> = emptyList(),
) : Parcelable

@Parcelize
data class RemoteBookManifestEntity(
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