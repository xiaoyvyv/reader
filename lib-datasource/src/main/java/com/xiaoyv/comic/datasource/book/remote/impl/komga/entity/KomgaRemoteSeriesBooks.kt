package com.xiaoyv.comic.datasource.book.remote.impl.komga.entity

import android.os.Parcelable
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


/**
 * [KomgaRemoteSeriesBooks]
 *
 * @author why
 * @since 5/5/24
 */
@Keep
@Parcelize
data class KomgaRemoteSeriesBooks(
    @SerializedName("content") var content: List<KomgaRemoteSeriesBook>? = listOf(),
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
}