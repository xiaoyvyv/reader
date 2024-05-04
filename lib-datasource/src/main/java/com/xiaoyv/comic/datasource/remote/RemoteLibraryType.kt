package com.xiaoyv.comic.datasource.remote

import androidx.annotation.IntDef

/**
 * [RemoteLibraryType]
 *
 * @author why
 * @since 5/4/24
 */
@Retention(AnnotationRetention.SOURCE)
@IntDef(RemoteLibraryType.TYPE_KOMGA)
annotation class RemoteLibraryType {
    companion object {
        const val TYPE_KOMGA = 1
    }
}
