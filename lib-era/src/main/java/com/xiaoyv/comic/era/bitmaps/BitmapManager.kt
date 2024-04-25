package com.xiaoyv.comic.era.bitmaps

import android.graphics.Bitmap
import android.graphics.Rect

object BitmapManager {
    //    static int partSize = 1 << CoreSettings.getInstance().bitmapSize;
    fun getBitmap(name: String?, width: Int, height: Int, config: Bitmap.Config?): BitmapRef {
        return BitmapRef(bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565))
    }

    fun release(ref: BitmapRef?) {
        if (ref?.bitmap != null) {
            ref.bitmap?.recycle()
        }
    }

    /*
    public static void release(final List<Bitmaps> bitmapsToRecycle) {
        try {
            if (LengthUtils.isNotEmpty(bitmapsToRecycle)) {
                for (Bitmaps b : bitmapsToRecycle) {
                    if(b!=null) {
                        b.finalize();
                    }

                }
            }
        } catch (Throwable t) {
            LOG.e(t);
        }
    }*/
    fun getBitmapBufferSize(width: Int, height: Int, config: Bitmap.Config?): Int {
        return getPixelSizeInBytes(config) * width * height
    }

    fun getBitmapBufferSize(parentBitmap: Bitmap?, childSize: Rect): Int {
        var bytes = 4
        if (parentBitmap != null) {
            bytes = getPixelSizeInBytes(parentBitmap.getConfig())
        }
        return bytes * childSize.width() * childSize.height()
    }

    fun getPixelSizeInBytes(config: Bitmap.Config?): Int {
        return when (config) {
            Bitmap.Config.ALPHA_8 -> 1
            Bitmap.Config.ARGB_4444, Bitmap.Config.RGB_565 -> 2
            Bitmap.Config.ARGB_8888 -> 4
            else -> 4
        }
    }
}
