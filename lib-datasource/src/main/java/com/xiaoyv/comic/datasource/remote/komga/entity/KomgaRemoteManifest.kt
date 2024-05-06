package com.xiaoyv.comic.datasource.remote.komga.entity

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * [KomgaRemoteManifest]
 *
 * @author why
 * @since 5/5/24
 */

@Keep
@Parcelize
data class KomgaRemoteManifest(
    @SerializedName("context") var context: String? = null,
    @SerializedName("links") var links: List<Link>? = null,
    @SerializedName("metadata") var metadata: Metadata? = null,
    @SerializedName("readingOrder") var readingOrder: List<ReadingOrder>? = null,
    @SerializedName("resources") var resources: List<Resource>? = null,
    @SerializedName("toc") var toc: List<Toc>? = null
) : Parcelable {

    @Keep
    @Parcelize
    data class Link(
        @SerializedName("href") var href: String? = null,
        @SerializedName("rel") var rel: String? = null,
        @SerializedName("type") var type: String? = null
    ) : Parcelable

    @Keep
    @Parcelize
    data class Metadata(
        @SerializedName("belongsTo") var belongsTo: BelongsTo? = null,
        @SerializedName("conformsTo") var conformsTo: String? = null,
        @SerializedName("contributor") var contributor: List<String?>? = null,
        @SerializedName("language") var language: String? = null,
        @SerializedName("modified") var modified: String? = null,
        @SerializedName("numberOfPages") var numberOfPages: Int = 0,
        @SerializedName("published") var published: String? = null,
        @SerializedName("rendition") var rendition: Rendition? = null,
        @SerializedName("title") var title: String? = null
    ) : Parcelable

    @Keep
    @Parcelize
    data class ReadingOrder(
        @SerializedName("href") var href: String? = null,
        @SerializedName("type") var type: String? = null,
        @SerializedName("width") var width: Int = 0,
        @SerializedName("height") var height: Int = 0,
    ) : Parcelable

    @Keep
    @Parcelize
    data class Resource(
        @SerializedName("href") var href: String? = null,
        @SerializedName("type") var type: String? = null
    ) : Parcelable

    @Keep
    @Parcelize
    data class Toc(
        @SerializedName("children") var children: List<Children?>? = null,
        @SerializedName("href") var href: String? = null,
        @SerializedName("title") var title: String? = null
    ) : Parcelable

    @Keep
    @Parcelize
    data class BelongsTo(
        @SerializedName("series") var series: List<Sery?>? = null
    ) : Parcelable

    @Keep
    @Parcelize
    data class Rendition(
        @SerializedName("layout") var layout: String? = null
    ) : Parcelable

    @Keep
    @Parcelize
    data class Sery(
        @SerializedName("name") var name: String? = null,
        @SerializedName("position") var position: Double = 0.0
    ) : Parcelable

    @Keep
    @Parcelize
    data class Children(
        @SerializedName("href") var href: String? = null,
        @SerializedName("title") var title: String? = null
    ) : Parcelable
}