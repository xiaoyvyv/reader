package com.xiaoyv.comic.reader.config.ann

import androidx.annotation.IntDef

@IntDef(
    ContentType.TYPE_SINGLE_PANE,
    ContentType.TYPE_DUAL_PANE,
)
@Retention(AnnotationRetention.SOURCE)
annotation class ContentType {
    companion object {
        const val TYPE_SINGLE_PANE = 1
        const val TYPE_DUAL_PANE = 2
    }
}