package com.xiaoyv.comic.datasource.series.remote

import android.content.Context
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryFactory
import com.xiaoyv.comic.datasource.series.RemoteSeriesModel
import com.xiaoyv.comic.datasource.series.SeriesData
import com.xiaoyv.comic.datasource.series.SeriesInfo

/**
 * [RemoteSeriesData]
 *
 * @author why
 * @since 5/6/24
 */
class RemoteSeriesData(
    override val context: Context,
    override val seriesModel: RemoteSeriesModel
) : SeriesData {
    private val remoteLibrary = RemoteLibraryFactory.create(context, seriesModel.config)

    override suspend fun getSeriesInfo(seriesId: String): SeriesInfo {
        return remoteLibrary.getSeriesById(seriesId).getOrThrow()
    }
}