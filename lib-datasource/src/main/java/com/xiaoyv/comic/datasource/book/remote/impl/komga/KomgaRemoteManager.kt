package com.xiaoyv.comic.datasource.book.remote.impl.komga

import android.content.Context
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryFactory
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * [KomgaRemoteManager]
 *
 * @author why
 * @since 5/4/24
 */
class KomgaRemoteManager(val context: Context, val config: RemoteLibraryConfig) {

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(config.baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(RemoteLibraryFactory.okhttp)
            .build()
    }

    val komga: KomgaRemoteInterface by lazy {
        retrofit.create(KomgaRemoteInterface::class.java)
    }
}