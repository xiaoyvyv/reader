package com.xiaoyv.comic.datasource.remote.komga.entity

import android.os.Parcelable

import kotlinx.parcelize.Parcelize

import androidx.annotation.Keep

import com.google.gson.annotations.SerializedName


/**
 * [KomgaRemoteUser]
 *
 * @author why
 * @since 5/4/24
 */
@Keep
@Parcelize
data class KomgaRemoteUser(
    @SerializedName("email") var email: String? = null,
    @SerializedName("id") var id: String? = null,
    @SerializedName("roles") var roles: List<String>? = null,
    @SerializedName("sharedAllLibraries") var sharedAllLibraries: Boolean = false,
    @SerializedName("sharedLibrariesIds") var sharedLibrariesIds: List<String>? = null
) : Parcelable