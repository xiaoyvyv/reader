package com.xiaoyv.comic.datasource.series

import android.content.Context
import com.xiaoyv.comic.datasource.series.file.FileSeriesData
import com.xiaoyv.comic.datasource.series.remote.RemoteSeriesData

/**
 * [SeriesFactory]
 *
 * @author why
 * @since 5/6/24
 */
object SeriesFactory {

    @JvmStatic
    fun create(context: Context, seriesModel: SeriesModel): SeriesData {
        return when (seriesModel) {
            is FileSeriesModel -> FileSeriesData(context, seriesModel)
            is RemoteSeriesModel -> RemoteSeriesData(context, seriesModel)
            else -> error("")
        }
    }
}