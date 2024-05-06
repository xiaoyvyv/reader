package com.xiaoyv.comic.datasource.remote.komga.entity

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * [KomgaRemoteSeriesBooks]
 *
 * @author why
 * @since 5/5/24
 */
@Keep
@Parcelize
data class KomgaRemoteSeriesBooks(
    @SerializedName("content") var content: List<Content>? = listOf(),
    @SerializedName("empty") var empty: Boolean = false,
    @SerializedName("first") var first: Boolean = false,
    @SerializedName("last") var last: Boolean = false,
    @SerializedName("number") var number: Int = 0,
    @SerializedName("numberOfElements") var numberOfElements: Int = 0,
    @SerializedName("pageable") var pageable: Pageable? = Pageable(),
    @SerializedName("size") var size: Int = 0,
    @SerializedName("sort") var sort: SortX? = SortX(),
    @SerializedName("totalElements") var totalElements: Int = 0,
    @SerializedName("totalPages") var totalPages: Int = 0
) : Parcelable {

    @Keep
    @Parcelize
    data class Content(
        @SerializedName("created") var created: String? = null,
        @SerializedName("deleted") var deleted: Boolean = false,
        @SerializedName("fileHash") var fileHash: String? = null,
        @SerializedName("fileLastModified") var fileLastModified: String? = null,
        @SerializedName("id") var id: String? = null,
        @SerializedName("lastModified") var lastModified: String? = null,
        @SerializedName("libraryId") var libraryId: String? = null,
        @SerializedName("media") var media: Media? = null,
        @SerializedName("metadata") var metadata: Metadata? = null,
        @SerializedName("name") var name: String? = null,
        @SerializedName("number") var number: Int = 0,
        @SerializedName("oneshot") var oneshot: Boolean = false,
        @SerializedName("readProgress") var readProgress: ReadProgress? = null,
        @SerializedName("seriesId") var seriesId: String? = null,
        @SerializedName("seriesTitle") var seriesTitle: String? = null,
        @SerializedName("size") var size: String? = null,
        @SerializedName("sizeBytes") var sizeBytes: Long = 0,
        @SerializedName("url") var url: String? = null
    ) : Parcelable

    @Keep
    @Parcelize
    data class Pageable(
        @SerializedName("offset") var offset: Int = 0,
        @SerializedName("pageNumber") var pageNumber: Int = 0,
        @SerializedName("pageSize") var pageSize: Int = 0,
        @SerializedName("paged") var paged: Boolean = false,
        @SerializedName("sort") var sort: SortX? = SortX(),
        @SerializedName("unpaged") var unpaged: Boolean = false
    ) : Parcelable

    @Keep
    @Parcelize
    data class SortX(
        @SerializedName("empty") var empty: Boolean = false,
        @SerializedName("sorted") var sorted: Boolean = false,
        @SerializedName("unsorted") var unsorted: Boolean = false
    ) : Parcelable

    @Keep
    @Parcelize
    data class Media(
        @SerializedName("comment") var comment: String? = null,
        @SerializedName("epubDivinaCompatible") var epubDivinaCompatible: Boolean = false,
        @SerializedName("mediaProfile") var mediaProfile: String? = null,
        @SerializedName("mediaType") var mediaType: String? = null,
        @SerializedName("pagesCount") var pagesCount: Int = 0,
        @SerializedName("status") var status: String? = null
    ) : Parcelable

    @Keep
    @Parcelize
    data class Metadata(
        @SerializedName("authors") var authors: List<Author?>? = null,
        @SerializedName("authorsLock") var authorsLock: Boolean = false,
        @SerializedName("created") var created: String? = null,
        @SerializedName("isbn") var isbn: String? = null,
        @SerializedName("isbnLock") var isbnLock: Boolean = false,
        @SerializedName("lastModified") var lastModified: String? = null,
        @SerializedName("linksLock") var linksLock: Boolean = false,
        @SerializedName("number") var number: String? = null,
        @SerializedName("numberLock") var numberLock: Boolean = false,
        @SerializedName("numberSort") var numberSort: Double = 0.0,
        @SerializedName("numberSortLock") var numberSortLock: Boolean = false,
        @SerializedName("releaseDate") var releaseDate: String? = null,
        @SerializedName("releaseDateLock") var releaseDateLock: Boolean = false,
        @SerializedName("summary") var summary: String? = null,
        @SerializedName("summaryLock") var summaryLock: Boolean = false,
        @SerializedName("tagsLock") var tagsLock: Boolean = false,
        @SerializedName("title") var title: String? = null,
        @SerializedName("titleLock") var titleLock: Boolean = false
    ) : Parcelable

    @Keep
    @Parcelize
    data class ReadProgress(
        @SerializedName("completed") var completed: Boolean = false,
        @SerializedName("created") var created: String? = null,
        @SerializedName("deviceId") var deviceId: String? = null,
        @SerializedName("deviceName") var deviceName: String? = null,
        @SerializedName("lastModified") var lastModified: String? = null,
        @SerializedName("page") var page: Int = 0,
        @SerializedName("readDate") var readDate: String? = null
    ) : Parcelable

    @Keep
    @Parcelize
    data class Author(
        @SerializedName("name") var name: String? = null,
        @SerializedName("role") var role: String? = null
    ) : Parcelable
}