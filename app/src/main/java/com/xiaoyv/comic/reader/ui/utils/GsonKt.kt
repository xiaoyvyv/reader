package com.xiaoyv.comic.reader.ui.utils

import com.google.gson.Gson

/**
 * [toJson]
 *
 * @author why
 * @since 5/4/24
 */
val gson: Gson by lazy { Gson().newBuilder().create() }
val gsontPretty: Gson by lazy { gson.newBuilder().setPrettyPrinting().create() }

fun Any?.toJson(pretty: Boolean = false): String {
    return if (pretty) gsontPretty.toJson(this)
    else gson.toJson(this)
}