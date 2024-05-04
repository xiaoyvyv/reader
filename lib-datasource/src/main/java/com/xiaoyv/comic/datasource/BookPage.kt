package com.xiaoyv.comic.datasource

import android.graphics.Bitmap

/**
 * [BookPage]
 *
 * @author why
 * @since 5/1/24
 */
interface BookPage<T : BookModel, D : BookDataSource<T>> {
    val dataSource: D
    val page: Int

    var pageWidth: Int
    var pageHeight: Int

    /**
     * 宽高比
     */
    var pageRatio: Float

    fun initPageMeta(): BookPage<T, D>

    fun renderPage(): Bitmap

    fun destroy()
}