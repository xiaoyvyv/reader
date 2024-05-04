package com.xiaoyv.comic.reader

import android.app.Application
import com.xiaoyv.comic.datasource.remote.RemoteLibraryFactory

lateinit var application: Application

/**
 * Class: [MainApplication]
 *
 * @author why
 * @since 3/5/24
 */
class MainApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        application = this

        RemoteLibraryFactory.application = this
    }
}