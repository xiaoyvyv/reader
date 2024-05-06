package com.xiaoyv.comic.datasource.remote

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
    suspend fun getLibraries(): Result<List<RemoteLibraryEntity>>

    /**
     * 获取每个分类数据的全部分页系列条目
     */
    suspend fun getLibraryBookSeries(
        libraryId: String,
        page: Int,
        pageSize: Int
    ): Result<List<RemoteBookSeriesEntity>>

    suspend fun getBookDetail(
        seriesId: String,
        libraryId: String
    ): Result<RemoteBookSeriesEntity>

    suspend fun getBookManifest(bookId: String, libraryId: String): Result<RemoteBookManifestEntity>

    suspend fun login(): Result<RemoteBookUserEntity>
}