package com.xiaoyv.comic.datasource.series

import android.content.Context

/**
 * [SeriesData]
 *
 * @author why
 * @since 5/6/24
 */
interface SeriesData {
    val context: Context
    val seriesModel: SeriesModel

    suspend fun getSeriesInfo(seriesId: String): SeriesInfo
}