package com.xiaoyv.comic.reader.ui.utils

import java.security.MessageDigest


/**
 * [md5]
 *
 * @author why
 * @since 4/29/24
 */
fun String.md5(): String {
    val bytes = toByteArray()
    val md = MessageDigest.getInstance("MD5")
    val digest = md.digest(bytes)
    return digest.joinToString("") { "%02x".format(it) }
}
