package com.xiaoyv.comic.reader.data.repository.remote

import com.xiaoyv.comic.datasource.book.remote.impl.RemoteSeriesInfo
import com.xiaoyv.comic.datasource.series.RemoteSeriesModel
import com.xiaoyv.comic.datasource.series.SeriesFactory
import com.xiaoyv.comic.datasource.series.SeriesModel
import com.xiaoyv.comic.datasource.utils.runCatchingPrint
import com.xiaoyv.comic.reader.application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * [RemoteSeriesRepositoryImpl]
 *
 * @author why
 * @since 5/6/24
 */
class RemoteSeriesRepositoryImpl : RemoteSeriesRepository {

    override suspend fun loadSeriesInfo(model: SeriesModel): Result<RemoteSeriesInfo> {
        return withContext(Dispatchers.IO) {
            require(model is RemoteSeriesModel) { "mode($model) must be RemoteSeriesModel" }
            runCatchingPrint {
                SeriesFactory.create(application, model)
                    .getSeriesInfo(model.seriesId)
                    .let { info ->
                        require(info is RemoteSeriesInfo)

                        info
                    }
            }
        }
    }
}