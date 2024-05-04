package com.xiaoyv.comic.datasource

import android.os.Parcelable
import android.util.Base64
import androidx.annotation.Keep
import com.google.gson.Gson
import com.xiaoyv.comic.datasource.remote.RemoteLibraryConfig
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import okio.ByteString.Companion.decodeBase64
import java.io.File

const val BOOK_MODEL_FILE = 1
const val BOOK_MODEL_REMOTE = 2

/**
 * [BookModel]
 *
 * @author why
 * @since 5/4/24
 */
@Keep
interface BookModel {
    val fileName: String
    val key: String
    val type: Int
    val coverModel: Any?
}

object BookModelHelper {
    private val gson = Gson()

    fun serialize(bookModel: BookModel): String {
        val type = bookModel.type.toString()
        val data = gson.toJson(bookModel)
        val string = "$type|$data"
        return Base64.encode(string.toByteArray(), Base64.NO_WRAP)
            .decodeToString()
    }

    fun deserialize(base64String: String): BookModel {
        val split = Base64.decode(base64String, Base64.NO_WRAP)
            .decodeToString()
            .split("|")
        val type = split[0].toIntOrNull() ?: -1
        val json = split[1]

        if (type == BOOK_MODEL_FILE) {
            return gson.fromJson(json, FileBookModel::class.java)
        }
        if (type == BOOK_MODEL_REMOTE) {
            return gson.fromJson(json, RemoteBookModel::class.java)
        }

        error("error type! $type")
    }
}

@Keep
@Parcelize
class FileBookModel(@Keep @JvmField val filePath: String) : BookModel, Parcelable {
    val file: File
        get() = File(filePath)

    override val type: Int
        get() = BOOK_MODEL_FILE

    override val fileName: String
        get() = file.name

    override val key: String
        get() = file.absolutePath

    override val coverModel: Any?
        get() = file.absolutePath
}

@Keep
@Parcelize
class RemoteBookModel(
    @Keep @JvmField var config: RemoteLibraryConfig,
    @Keep @JvmField var bookId: String = "",
    @Keep @JvmField var libraryId: String = "",
) : BookModel, Parcelable {
    override val type: Int
        get() = BOOK_MODEL_REMOTE

    override val fileName: String
        get() = ""

    override val key: String
        get() = bookId

    override val coverModel: Any?
        get() = ""
}