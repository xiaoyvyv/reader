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
     * 获取每个分类数据的全部分页条目
     */
    suspend fun getLibraryBookList(
        libraryId: String,
        page: Int,
        pageSize: Int
    ): Result<List<RemoteBookEntity>>


    suspend fun getBookDetail(bookId: String, libraryId: String): Result<RemoteBookEntity>

    suspend fun login(): Result<RemoteBookUserEntity>
}