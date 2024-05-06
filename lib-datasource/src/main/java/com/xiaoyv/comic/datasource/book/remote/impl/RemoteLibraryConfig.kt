package com.xiaoyv.comic.datasource.book.remote.impl

import android.os.Parcelable
import android.util.Base64
import androidx.annotation.Keep
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize


private val LOCAL_FILE_FAKE_REMOTE_CONFIG = RemoteLibraryConfig(
    type = RemoteLibraryType.TYPE_UNKNOWN,
    baseUrl = "http://localhost",
    username = "",
    password = ""
)

/**
 * [RemoteLibraryConfig]
 *
 * @author why
 * @since 5/4/24
 */
@Parcelize
@Keep
data class RemoteLibraryConfig(
    @RemoteLibraryType @SerializedName("type") val type: Int,
    @SerializedName("baseUrl") val baseUrl: String,
    @SerializedName("username") var username: String,
    @SerializedName("password") var password: String,
    @SerializedName("language") var language: String = "",
) : Parcelable {

    val basicAuthorization: String
        get() {
            val encode = Base64.encode(("$username:$password").toByteArray(), Base64.NO_WRAP)
            return "Basic ${encode.decodeToString()}"
        }
}
