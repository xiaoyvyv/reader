package com.xiaoyv.comic.datasource.jni.codec

import android.graphics.RectF
import androidx.annotation.Keep

@Keep
class TextChar(
    x0: Float,
    y0: Float,
    x1: Float,
    y1: Float,
    @JvmField var c: Int
) : RectF(x0, y0, x1, y1)
