package com.xiaoyv.comic.reader.ui.component

import android.graphics.drawable.Icon
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable

data class Page(
    val icon: Icon? = null,
    @StringRes val labelResId: Int = 0,
    val content: @Composable () -> Unit,
)