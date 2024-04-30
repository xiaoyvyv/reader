package com.xiaoyv.comic.datasource.jni.codec

import android.graphics.RectF
import androidx.annotation.Keep

@Keep
class PageLink @JvmOverloads constructor(
    @Keep
    @JvmField
    var url: String? = null,

    @Keep
    @JvmField
    var sourceRect: RectF? = null,

    @Keep
    @JvmField
    var targetPage: Int = -1,

    @Keep
    @JvmField
    var targetRect: RectF? = null,

    @Keep
    @JvmField
    var number: Int = 0,
) {

    constructor(l: String?, source: IntArray) : this(
        url = l,
        sourceRect = RectF(
            source[0].toFloat(),
            source[1].toFloat(),
            source[2].toFloat(),
            source[3].toFloat()
        )
    )
}
