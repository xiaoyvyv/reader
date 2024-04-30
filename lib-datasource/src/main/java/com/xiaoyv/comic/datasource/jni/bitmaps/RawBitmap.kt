package com.xiaoyv.comic.datasource.jni.bitmaps

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.os.Parcelable
import androidx.annotation.Keep
import kotlinx.parcelize.Parcelize

@Keep
@Parcelize
class RawBitmap(
    var pixels: IntArray = IntArray(0),
    var width: Int = 0,
    var height: Int = 0,
    var hasAlpha: Boolean = false
) : Parcelable {

    constructor (width: Int, height: Int, hasAlpha: Boolean) : this(
        pixels = IntArray(width * height),
        width = width,
        height = height,
        hasAlpha = hasAlpha
    )

    constructor(bitmap: Bitmap, srcRect: Rect) : this(
        width = srcRect.width(),
        height = srcRect.height(),
        hasAlpha = bitmap.hasAlpha(),
        pixels = IntArray(srcRect.width() * srcRect.height())
    ) {
        bitmap.getPixels(pixels, 0, width, srcRect.left, srcRect.top, width, height)
    }

    constructor(bitmap: Bitmap, left: Int, top: Int, width: Int, height: Int) : this(
        pixels = IntArray(width * height),
        width = width,
        height = height,
        hasAlpha = bitmap.hasAlpha()
    ) {
        bitmap.getPixels(pixels, 0, width, left, top, width, height)
    }

    fun retrieve(bitmap: Bitmap, left: Int, top: Int, width: Int, height: Int) {
        this.width = width
        this.height = height
        bitmap.getPixels(pixels, 0, width, left, top, width, height)
    }

    fun draw(canvas: Canvas, x: Float, y: Float, paint: Paint?) {
        canvas.drawBitmap(pixels, 0, width, x, y, width, height, hasAlpha, paint)
    }

    fun toBitmap(bitmap: Bitmap) {
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)

    }

    fun toBitmap(): BitmapRef {
        val bitmap = BitmapManager.getBitmap("RawBitmap", width, height, Bitmap.Config.RGB_565);
        bitmap.bitmap?.setPixels(pixels, 0, width, 0, 0, width, height);
        return bitmap;
    }

    fun hasAlpha(): Boolean {
        return hasAlpha
    }

    fun fillAlpha(v: Int) {
        for (i in pixels.indices) {
            pixels[i] = 0x00ffffff and pixels[i] or (v shl 24)
        }
    }

    fun invert() {
        /*        LOG.d("invert", AppSP.get().lastBookPath);
        if (!MagicHelper.isNeedMagic() && BookType.DJVU.is(AppSP.get().lastBookPath)) {
            nativeInvert(pixels, width, height);
            return;
        }
        if (BookType.DJVU.is(AppSP.get().lastBookPath)) {
            return;
        }
        if (!(MagicHelper.isNeedMagic() && AppState.get().isCustomizeBgAndColors)) {
            if (!BookCSS.get().isTextFormat()) {
                nativeInvert(pixels, width, height);
            }
        }*/
    }

    fun contrast(contrast: Int) {
        nativeContrast(pixels, width, height, contrast * 256 / 100)
    }

    fun exposure(exposure: Int) {
        nativeExposure(pixels, width, height, exposure * 128 / 100)
    }

    fun autoLevels() {
        nativeAutoLevels2(pixels, width, height)
    }

    fun scaleHq4x(): BitmapRef {
        return scaleHq4x(this)
    }

    fun scaleHq3x(): BitmapRef {
        return scaleHq3x(this)
    }

    fun scaleHq2x(): BitmapRef {
        return scaleHq2x(this)
    }

    companion object {
        private fun invert(bitmap: Bitmap): Bitmap {
            val pixels = IntArray(bitmap.getWidth() * bitmap.getHeight())
            bitmap.getPixels(
                pixels,
                0,
                bitmap.getWidth(),
                0,
                0,
                bitmap.getWidth(),
                bitmap.getHeight()
            )
            nativeInvert(pixels, bitmap.getWidth(), bitmap.getHeight())
            val res = bitmap.copy(bitmap.getConfig(), true)
            res.setPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight())
            return res
        }

        fun scaleHq4x(src: RawBitmap): BitmapRef {
            val dest = RawBitmap(src.width * 4, src.height * 4, src.hasAlpha)
            src.fillAlpha(0x00)
            nativeHq4x(src.pixels, dest.pixels, src.width, src.height)
            dest.fillAlpha(0xFF)
            return dest.toBitmap()
        }

        fun scaleHq3x(src: RawBitmap): BitmapRef {
            val dest = RawBitmap(src.width * 3, src.height * 3, src.hasAlpha)
            src.fillAlpha(0x00)
            nativeHq3x(src.pixels, dest.pixels, src.width, src.height)
            dest.fillAlpha(0xFF)
            return dest.toBitmap()
        }

        fun scaleHq2x(src: RawBitmap): BitmapRef {
            val dest = RawBitmap(src.width * 2, src.height * 2, src.hasAlpha)
            src.fillAlpha(0x00)
            nativeHq2x(src.pixels, dest.pixels, src.width, src.height)
            dest.fillAlpha(0xFF)
            return dest.toBitmap()
        }

        private external fun nativeHq2x(src: IntArray, dst: IntArray, width: Int, height: Int)
        private external fun nativeHq3x(src: IntArray, dst: IntArray, width: Int, height: Int)
        private external fun nativeHq4x(src: IntArray, dst: IntArray, width: Int, height: Int)
        private external fun nativeInvert(src: IntArray, width: Int, height: Int)

        /**
         * contrast value 256 - normal
         */
        private external fun nativeContrast(src: IntArray, width: Int, height: Int, contrast: Int)

        /**
         * Exposure correction values -128...+128
         */
        private external fun nativeExposure(src: IntArray, width: Int, height: Int, exposure: Int)
        private external fun nativeAutoLevels(src: IntArray, width: Int, height: Int)
        private external fun nativeAutoLevels2(src: IntArray, width: Int, height: Int)
    }
}
