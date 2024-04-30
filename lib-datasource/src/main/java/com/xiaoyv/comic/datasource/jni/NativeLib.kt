package com.xiaoyv.comic.datasource.jni

import androidx.annotation.Keep

@Keep
object NativeLib {
    init {
        NativeContext.init()
    }

    /**
     * A native method that is implemented by the 'era' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
}