package com.xiaoyv.comic.datasource.jni.codec

import android.graphics.RectF
import androidx.annotation.Keep

@Keep
data class PageTextBox @JvmOverloads constructor(
    @Keep
    @JvmField
    var text: String? = ""
) : RectF()
