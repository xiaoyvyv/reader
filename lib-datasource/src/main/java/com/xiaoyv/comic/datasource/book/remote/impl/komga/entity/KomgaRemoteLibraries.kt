package com.xiaoyv.comic.datasource.book.remote.impl.komga.entity

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * [KomgaRemoteLibraries]
 *
 * @author why
 * @since 5/4/24
 */
class KomgaRemoteLibraries : ArrayList<KomgaRemoteLibraryItem>()

@Keep
@Parcelize
data class KomgaRemoteLibraryItem(
    @SerializedName("analyzeDimensions") var analyzeDimensions: Boolean = false,
    @SerializedName("convertToCbz") var convertToCbz: Boolean = false,
    @SerializedName("emptyTrashAfterScan") var emptyTrashAfterScan: Boolean = false,
    @SerializedName("hashFiles") var hashFiles: Boolean = false,
    @SerializedName("hashPages") var hashPages: Boolean = false,
    @SerializedName("id") var id: String? = null,
    @SerializedName("importBarcodeIsbn") var importBarcodeIsbn: Boolean = false,
    @SerializedName("importComicInfoBook") var importComicInfoBook: Boolean = false,
    @SerializedName("importComicInfoCollection") var importComicInfoCollection: Boolean = false,
    @SerializedName("importComicInfoReadList") var importComicInfoReadList: Boolean = false,
    @SerializedName("importComicInfoSeries") var importComicInfoSeries: Boolean = false,
    @SerializedName("importComicInfoSeriesAppendVolume") var importComicInfoSeriesAppendVolume: Boolean = false,
    @SerializedName("importEpubBook") var importEpubBook: Boolean = false,
    @SerializedName("importEpubSeries") var importEpubSeries: Boolean = false,
    @SerializedName("importLocalArtwork") var importLocalArtwork: Boolean = false,
    @SerializedName("importMylarSeries") var importMylarSeries: Boolean = false,
    @SerializedName("name") var name: String? = null,
    @SerializedName("oneshotsDirectory") var oneshotsDirectory: String? = null,
    @SerializedName("repairExtensions") var repairExtensions: Boolean = false,
    @SerializedName("root") var root: String? = null,
    @SerializedName("scanCbx") var scanCbx: Boolean = false,
    @SerializedName("scanDirectoryExclusions") var scanDirectoryExclusions: List<String?>? = null,
    @SerializedName("scanEpub") var scanEpub: Boolean = false,
    @SerializedName("scanForceModifiedTime") var scanForceModifiedTime: Boolean = false,
    @SerializedName("scanInterval") var scanInterval: String? = null,
    @SerializedName("scanOnStartup") var scanOnStartup: Boolean = false,
    @SerializedName("scanPdf") var scanPdf: Boolean = false,
    @SerializedName("seriesCover") var seriesCover: String? = null,
    @SerializedName("unavailable") var unavailable: Boolean = false
) : Parcelable