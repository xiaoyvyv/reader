package com.xiaoyv.comic.era.bitmaps

import android.graphics.Bitmap
import java.util.concurrent.atomic.AtomicInteger

private val SEQ = AtomicInteger()

class BitmapRef(
    var bitmap: Bitmap?,
    @JvmField val width: Int = bitmap?.getWidth() ?: 0,
    @JvmField val height: Int = bitmap?.getHeight() ?: 0,
    @JvmField val size: Int = if (bitmap == null) 0 else
        BitmapManager.getBitmapBufferSize(bitmap.width, bitmap.height, bitmap.getConfig())
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
