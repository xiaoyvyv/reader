package com.xiaoyv.comic.datasource.series

import android.os.Parcelable
import android.util.Base64
import androidx.annotation.Keep
import com.google.gson.Gson
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryConfig
import kotlinx.parcelize.Parcelize
import java.io.File

const val SERIES_MODEL_FILE = 1
const val SERIES_MODEL_REMOTE = 2

/**
 * [SeriesModel]
 *
 * @author why
 * @since 5/4/24
 */
@Keep
interface SeriesModel {
    val fileName: String
    val key: String
    val type: Int
}

object SeriesModelHelper {
    private val gson = Gson()

    fun serialize(seriesModel: SeriesModel): String {
        val type = seriesModel.type.toString()
        val data = gson.toJson(seriesModel)
        val string = "$type|$data"
        return Base64.encode(string.toByteArray(), Base64.NO_WRAP)
            .decodeToString()
    }

    fun deserialize(base64String: String): SeriesModel {
        val split = Base64.decode(base64String, Base64.NO_WRAP)
            .decodeToString()
            .split("|")
        val type = split[0].toIntOrNull() ?: -1
        val json = split[1]

        if (type == SERIES_MODEL_FILE) {
            return gson.fromJson(json, FileSeriesModel::class.java)
        }
        if (type == SERIES_MODEL_REMOTE) {
            return gson.fromJson(json, RemoteSeriesModel::class.java)
        }

        error("error type! $type")
    }
}

@Keep
@Parcelize
class FileSeriesModel(@Keep @JvmField val filePath: String) : SeriesModel, Parcelable {
    val file: File
        get() = File(filePath)

    override val type: Int
        get() = SERIES_MODEL_FILE

    override val fileName: String
        get() = file.name

    override val key: String
        get() = file.absolutePath
}

@Keep
@Parcelize
class RemoteSeriesModel(
    @Keep @JvmField var config: RemoteLibraryConfig,
    @Keep @JvmField var seriesId: String = "",
) : SeriesModel, Parcelable {
    override val type: Int
        get() = SERIES_MODEL_REMOTE

    override val fileName: String
        get() = ""

    override val key: String
        get() = seriesId
}