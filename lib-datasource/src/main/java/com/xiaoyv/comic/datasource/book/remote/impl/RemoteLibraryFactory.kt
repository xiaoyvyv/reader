package com.xiaoyv.comic.datasource.book.remote.impl

import android.app.Application
import android.content.Context
import com.xiaoyv.comic.datasource.book.remote.impl.komga.KomgaRemoteLibrary
import com.xiaoyv.comic.datasource.utils.cookie.PersistentCookieJar
import com.xiaoyv.comic.datasource.utils.cookie.cache.SetCookieCache
import com.xiaoyv.comic.datasource.utils.cookie.persistence.SharedPrefsCookiePersistor
import com.xiaoyv.comic.era.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit

/**
 * [RemoteLibraryFactory]
 *
 * @author why
 * @since 5/4/24
 */
object RemoteLibraryFactory {
    lateinit var application: Application

    val cookiejar by lazy {
        PersistentCookieJar(
            SetCookieCache(),
            SharedPrefsCookiePersistor(
                application
            )
        )
    }

    val okhttp by lazy {
        OkHttpClient.Builder()
            .cookieJar(cookiejar)
            .callTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .connectTimeout(15, TimeUnit.SECONDS)
            .apply {
                if (BuildConfig.DEBUG) {
                    addNetworkInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                }
            }
            .build()
    }


    @JvmStatic
    fun create(context: Context, config: RemoteLibraryConfig): RemoteLibrary {
        return when (config.type) {
            RemoteLibraryType.TYPE_KOMGA -> KomgaRemoteLibrary(context, config)
            else -> error("not support remote library!")
        }
    }
}