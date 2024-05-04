package com.xiaoyv.comic.datasource.remote.komga.entity

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName
import java.util.Date


/**
 * [KomgaRemoteSeries]
 *
 * @author why
 * @since 5/4/24
 */
@Keep
@Parcelize
data class KomgaRemoteSeries(
    @SerializedName("content") var content: List<KomgaRemoteSeriesContent>? = listOf(),
    @SerializedName("empty") var empty: Boolean = false,
    @SerializedName("first") var first: Boolean = false,
    @SerializedName("last") var last: Boolean = false,
    @SerializedName("number") var number: Int = 0,
    @SerializedName("numberOfElements") var numberOfElements: Int = 0,
    @SerializedName("pageable") var pageable: Pageable? = null,
    @SerializedName("size") var size: Int = 0,
    @SerializedName("sort") var sort: Sort? = null,
    @SerializedName("totalElements") var totalElements: Int = 0,
    @SerializedName("totalPages") var totalPages: Int = 0
) : Parcelable

@Keep
@Parcelize
data class KomgaRemoteSeriesContent(
    @SerializedName("booksCount") var booksCount: Int = 0,
    @SerializedName("booksInProgressCount") var booksInProgressCount: Int = 0,
    @SerializedName("booksMetadata") var booksMetadata: BooksMetadata? = null,
    @SerializedName("booksReadCount") var booksReadCount: Int = 0,
    @SerializedName("booksUnreadCount") var booksUnreadCount: Int = 0,
    @SerializedName("created") var created: Date? = null,
    @SerializedName("deleted") var deleted: Boolean = false,
    @SerializedName("fileLastModified") var fileLastModified: Date? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("lastModified") var lastModified: Date? = null,
    @SerializedName("libraryId") var libraryId: String? = null,
    @SerializedName("metadata") var metadata: Metadata? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("oneshot") var oneshot: Boolean = false,
    @SerializedName("url") var url: String? = null,
) : Parcelable

@Keep
@Parcelize
data class Pageable(
    @SerializedName("offset") var offset: Int = 0,
    @SerializedName("pageNumber") var pageNumber: Int = 0,
    @SerializedName("pageSize") var pageSize: Int = 0,
    @SerializedName("paged") var paged: Boolean = false,
    @SerializedName("sort") var sort: Sort? = Sort(),
    @SerializedName("unpaged") var unpaged: Boolean = false
) : Parcelable

@Keep
@Parcelize
data class Sort(
    @SerializedName("empty") var empty: Boolean = false,
    @SerializedName("sorted") var sorted: Boolean = false,
    @SerializedName("unsorted") var unsorted: Boolean = false
) : Parcelable

@Keep
@Parcelize
data class BooksMetadata(
    @SerializedName("authors") var authors: List<Author?>? = null,
    @SerializedName("created") var created: String? = null,
    @SerializedName("lastModified") var lastModified: String? = null,
    @SerializedName("releaseDate") var releaseDate: String? = null,
    @SerializedName("summary") var summary: String? = null,
    @SerializedName("summaryNumber") var summaryNumber: String? = null,
    @SerializedName("tags") var tags: List<String?>? = null
) : Parcelable

@Keep
@Parcelize
data class Metadata(
    @SerializedName("ageRating") var ageRating: String? = null,
    @SerializedName("ageRatingLock") var ageRatingLock: Boolean = false,
    @SerializedName("alternateTitles") var alternateTitles: List<AlternateTitle?>? = null,
    @SerializedName("alternateTitlesLock") var alternateTitlesLock: Boolean = false,
    @SerializedName("created") var created: String? = null,
    @SerializedName("genres") var genres: List<String>? = null,
    @SerializedName("genresLock") var genresLock: Boolean = false,
    @SerializedName("language") var language: String? = null,
    @SerializedName("languageLock") var languageLock: Boolean = false,
    @SerializedName("lastModified") var lastModified: String? = null,
    @SerializedName("links") var links: List<Link?>? = null,
    @SerializedName("linksLock") var linksLock: Boolean = false,
    @SerializedName("publisher") var publisher: String? = null,
    @SerializedName("publisherLock") var publisherLock: Boolean = false,
    @SerializedName("readingDirection") var readingDirection: String? = null,
    @SerializedName("readingDirectionLock") var readingDirectionLock: Boolean = false,
    @SerializedName("sharingLabels") var sharingLabels: List<String>? = null,
    @SerializedName("sharingLabelsLock") var sharingLabelsLock: Boolean = false,
    @SerializedName("status") var status: String? = null,
    @SerializedName("statusLock") var statusLock: Boolean = false,
    @SerializedName("summary") var summary: String? = null,
    @SerializedName("summaryLock") var summaryLock: Boolean = false,
    @SerializedName("tags") var tags: List<String>? = null,
    @SerializedName("tagsLock") var tagsLock: Boolean = false,
    @SerializedName("title") var title: String? = null,
    @SerializedName("titleLock") var titleLock: Boolean = false,
    @SerializedName("titleSort") var titleSort: String? = null,
    @SerializedName("titleSortLock") var titleSortLock: Boolean = false,
    @SerializedName("totalBookCount") var totalBookCount: Int = 0,
    @SerializedName("totalBookCountLock") var totalBookCountLock: Boolean = false
) : Parcelable

@Keep
@Parcelize
data class Author(
    @SerializedName("name") var name: String? = null,
    @SerializedName("role") var role: String? = null
) : Parcelable

@Keep
@Parcelize
data class AlternateTitle(
    @SerializedName("label") var label: String? = null,
    @SerializedName("title") var title: String? = null
) : Parcelable

@Keep
@Parcelize
data class Link(
    @SerializedName("label") var label: String? = null,
    @SerializedName("url") var url: String? = null
) : Parcelable