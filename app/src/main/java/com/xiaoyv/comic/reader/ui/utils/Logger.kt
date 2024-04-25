package com.xiaoyv.comic.reader.ui.utils

import android.util.Log

/**
 * Class: [debugLog]
 *
 * @author why
 * @since 3/4/24
 */
inline fun debugLog(tag: String = "ComicLogger", message: () -> Any? = { "" }) {
    Log.e(tag, message().toString())
}