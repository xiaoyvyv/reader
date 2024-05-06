package com.xiaoyv.comic.datasource.series.file

import android.content.Context
import com.xiaoyv.comic.datasource.book.BookDataSourceFactory
import com.xiaoyv.comic.datasource.book.FileBookModel
import com.xiaoyv.comic.datasource.series.FileSeriesModel
import com.xiaoyv.comic.datasource.series.SeriesBook
import com.xiaoyv.comic.datasource.series.SeriesData
import com.xiaoyv.comic.datasource.series.SeriesInfo

/**
 * [FileSeriesData]
 *
 * @author why
 * @since 5/6/24
 */
class FileSeriesData(
    override val context: Context,
    override val seriesModel: FileSeriesModel
) : SeriesData {

    override suspend fun getSeriesInfo(seriesId: String): SeriesInfo {
        val metaInfo = BookDataSourceFactory.create(context, FileBookModel(seriesModel.filePath))
            .use {
                it.load()
                it.getMetaInfo()
            }

        return SeriesInfo(
            cover = seriesModel.filePath,
            title = metaInfo.title,
            summary = metaInfo.subject,
            books = listOf(
                SeriesBook(
                    cover = seriesModel.filePath,
                    title = metaInfo.title,
                    number = 1,
                    bookMeta = metaInfo,
                    book = FileBookModel(seriesModel.filePath)
                )
            )
        )
    }
}