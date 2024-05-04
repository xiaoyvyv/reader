package com.xiaoyv.comic.datasource.remote.komga

import android.content.Context
import com.xiaoyv.comic.datasource.cookie.PersistentCookieJar
import com.xiaoyv.comic.datasource.cookie.cache.SetCookieCache
import com.xiaoyv.comic.datasource.cookie.persistence.SharedPrefsCookiePersistor
import com.xiaoyv.comic.datasource.remote.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.remote.RemoteLibraryFactory
import com.xiaoyv.comic.era.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

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