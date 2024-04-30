package com.xiaoyv.comic.datasource.jni.bitmaps

import android.graphics.Bitmap
import androidx.annotation.Keep
import java.util.concurrent.atomic.AtomicInteger

private val SEQ = AtomicInteger()

@Keep
data class BitmapRef @JvmOverloads constructor(
    @Keep
    @JvmField
    var bitmap: Bitmap?,

    @Keep
    @JvmField
    val width: Int = bitmap?.getWidth() ?: 0,

    @Keep
    @JvmField
    val height: Int = bitmap?.getHeight() ?: 0,

    @Keep
    @JvmField
    val size: Int = if (bitmap == null) 0 else {
        BitmapManager.getBitmapBufferSize(bitmap.width, bitmap.height, bitmap.getConfig())
    }
) {
    private val id = SEQ.incrementAndGet()

    @Throws(Throwable::class)
    protected fun finalize() {
        recycle()
    }

    fun isRecycled(): Boolean {
        runCatching {
            if (bitmap != null) {
                if (!requireNotNull(bitmap).isRecycled) {
                    return false
                }
                bitmap = null
            }
        }
        return true
    }

    fun recycle() {
        runCatching {
            if (!isRecycled()) {
                bitmap?.recycle()
            }
            bitmap = null
        }
    }

    override fun toString(): String {
        return "BitmapRef [id=$id, width=$width, height=$height, size=$size]"
    }
}
