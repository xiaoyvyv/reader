package com.xiaoyv.comic.era

object NativeLib {
    init {
        System.loadLibrary("era")
    }

    val mobi by lazy { NativeMobi() }

    /**
     * A native method that is implemented by the 'era' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String
}