package com.xiaoyv.comic.reader

import android.app.Application

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
    }
}