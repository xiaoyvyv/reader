package com.xiaoyv.comic.reader.ui.utils

import android.app.Application
import androidx.lifecycle.AndroidViewModel


val AndroidViewModel.context: Application
    get() = getApplication()