package com.xiaoyv.comic.datasource.book.remote.impl.komga

import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteLibraries
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteManifest
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteSeries
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteSeriesBook
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteSeriesBooks
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteSeriesContent
import com.xiaoyv.comic.datasource.book.remote.impl.komga.entity.KomgaRemoteUser
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * [KomgaRemoteInterface]
 *
 * @author why
 * @since 5/4/24
 */
interface KomgaRemoteInterface {

    @GET("/api/v1/libraries")
    suspend fun getLibrary(
        @Header("Authorization") authorization: String,
    ): KomgaRemoteLibraries

    /**
     * https://manga.pilipiliultra.com/api/v1/series?page=0&size=500&sort=metadata.titleSort,asc&library_id=0F4KDWTWDP9HK
     */
    @GET("/api/v1/series")
    suspend fun getSeries(
        @Header("Authorization") authorization: String,
        @Query("page") page: Int,
        @Query("size") size: Int,
        @Query("library_id") libraryId: String,
        @Query("sort") sort: String = "metadata.titleSort,asc"
    ): KomgaRemoteSeries

    @GET("/api/v2/users/me")
    suspend fun getMeInfo(
        @Header("Authorization") authorization: String,
        @Query("remember-me") remember: Boolean = true
    ): KomgaRemoteUser

    @GET("/api/v1/series/{seriesId}")
    suspend fun getSeriesDetail(
        @Header("Authorization") authorization: String,
        @Path("seriesId") seriesId: String
    ): KomgaRemoteSeriesContent

    @GET("/api/v1/series/{seriesId}/books")
    suspend fun getSeriesBooks(
        @Header("Authorization") authorization: String,
        @Path("seriesId") seriesId: String,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 500,
        @Query("sort") sort: String = "metadata.numberSort,asc"
    ): KomgaRemoteSeriesBooks

    @GET("/api/v1/books/{bookId}")
    suspend fun getBookDetail(
        @Header("Authorization") authorization: String,
        @Path("bookId") bookId: String
    ): KomgaRemoteSeriesBook

    @GET("/api/v1/books/{bookId}/manifest")
    suspend fun getBookManifest(
        @Header("Authorization") authorization: String,
        @Path("bookId") bookId: String
    ): KomgaRemoteManifest
}