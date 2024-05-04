package com.xiaoyv.comic.datasource.utils

import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * [lastModifiedText]
 *
 * @author why
 * @since 4/29/24
 */
fun File.lastModifiedText(format: String = "yyyy-MM-dd"): String {
    runCatching {
        val formatter = SimpleDateFormat(format, Locale.getDefault())
        return formatter.format(Date(lastModified()))
    }
    return ""
}
