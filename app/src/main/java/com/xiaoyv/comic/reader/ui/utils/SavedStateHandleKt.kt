package com.xiaoyv.comic.reader.ui.utils

import androidx.lifecycle.SavedStateHandle

inline fun SavedStateHandle.requireString(key: String, default: () -> String = { "" }): String {
    return get<String>(key) ?: default()
}