package com.xiaoyv.comic.datasource.jni.codec

import androidx.annotation.Keep

@Keep
class OutlineLink @JvmOverloads constructor(
    @Keep
    @JvmField
    var title: String,

    @Keep
    @JvmField val link: String? = null,

    @Keep
    @JvmField var level: Int = 0,

    @Keep
    @JvmField var docHandle: Long = 0,

    @Keep
    @JvmField var linkUri: String? = null,

    @Keep
    @JvmField
    var contentSrc: String? = null,
) : CharSequence {

    override val length: Int
        get() = title.length

    override fun get(index: Int): Char {
        return title[index]
    }

    /**
     * {@inheritDoc}
     *
     * @see CharSequence.subSequence
     */
    override fun subSequence(startIndex: Int, endIndex: Int): CharSequence {
        return title.subSequence(startIndex, endIndex)
    }

    /**
     * {@inheritDoc}
     *
     * @see Object.toString
     */
    override fun toString(): String {
        return title
    }
}
