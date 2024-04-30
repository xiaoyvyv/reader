package com.xiaoyv.comic.datasource.jni.codec

import androidx.annotation.Keep

class CodecPageInfo @JvmOverloads constructor(
    /**
     * page width (in pixels)
     */
    @Keep
    @JvmField
    var width: Int = 0,

    /**
     * page height (in pixels)
     */
    @Keep
    @JvmField
    var height: Int = 0,

    /**
     * page resolution (in dots per inch)
     */
    @Keep
    @JvmField
    var dpi: Int = 0,

    /**
     * initial page orientation
     */
    @Keep
    @JvmField
    var rotation: Int = 0,

    /**
     * page version
     */
    @Keep
    @JvmField
    var version: Int = 0,
)
