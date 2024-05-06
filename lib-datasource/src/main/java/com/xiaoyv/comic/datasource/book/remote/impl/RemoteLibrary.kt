package com.xiaoyv.comic.datasource.book.remote.impl

import android.content.Context

/**
 * [RemoteLibrary]
 *
 * @author why
 * @since 5/4/24
 */
interface RemoteLibrary {
    val context: Context
    val config: RemoteLibraryConfig

    /**
     * 获取分类数据
     */
    suspend fun getType(): Result<List<RemoteLibraryEntity>>

    /**
     * 获取分类数据的具体条目信息（分页查询）
     */
    suspend fun getTypeSeries(
        typeId: String,
        page: Int,
        pageSize: Int
    ): Result<List<RemoteSeriesInfo>>

    suspend fun getSeriesById(seriesId: String): Result<RemoteSeriesInfo>


    suspend fun getBookDetail(
        bookId: String,
        seriesId: String? = null
    ): Result<RemoteBookDetailEntity>


    suspend fun login(): Result<RemoteBookUserEntity>
}