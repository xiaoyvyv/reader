package com.xiaoyv.comic.reader.ui.utils

import java.net.URLDecoder
import java.net.URLEncoder

val URL_CHARACTER_ENCODING: String = Charsets.UTF_8.name()

val String.urlEncode: String
    get() = URLEncoder.encode(this, URL_CHARACTER_ENCODING)

val String.urlDecode: String
    get() = URLDecoder.decode(this, URL_CHARACTER_ENCODING)