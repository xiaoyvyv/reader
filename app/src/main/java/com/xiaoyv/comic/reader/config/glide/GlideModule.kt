package com.xiaoyv.comic.reader.config.glide

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.xiaoyv.comic.datasource.book.BookPage
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryFactory
import okhttp3.OkHttpClient
import java.io.InputStream
import java.util.concurrent.TimeUnit

@GlideModule
class GlideModule : AppGlideModule() {

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry.prepend(String::class.java, InputStream::class.java, BookCoverLoader.Factory())
        registry.append(BookPage::class.java, Bitmap::class.java, BookPageLoader.Factory())
        registry.replace(
            GlideUrl::class.java,
            InputStream::class.java,
            OkHttpUrlLoader.Factory(
                OkHttpClient.Builder()
                    .cookieJar(RemoteLibraryFactory.cookiejar)
                    .callTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .build()
            )
        )
    }

    override fun applyOptions(context: Context, builder: GlideBuilder) {

    }
}
