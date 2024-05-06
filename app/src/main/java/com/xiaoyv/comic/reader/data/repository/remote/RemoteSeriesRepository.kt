package com.xiaoyv.comic.reader.data.repository.remote

import com.xiaoyv.comic.datasource.book.remote.impl.RemoteSeriesInfo
import com.xiaoyv.comic.datasource.series.SeriesInfo
import com.xiaoyv.comic.datasource.series.SeriesModel

/**
 * [RemoteSeriesRepository]
 *
 * @author why
 * @since 5/6/24
 */
interface RemoteSeriesRepository {

    suspend fun loadSeriesInfo(model: SeriesModel): Result<RemoteSeriesInfo>
}