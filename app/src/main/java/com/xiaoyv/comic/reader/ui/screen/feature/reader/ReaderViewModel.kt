package com.xiaoyv.comic.reader.ui.screen.feature.reader

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.xiaoyv.comic.datasource.impl.archive.ArchiveBookUnRar
import com.xiaoyv.comic.reader.ui.utils.debugLog

/**
 * [ReaderViewModel]
 *
 * @author why
 * @since 4/29/24
 */
class ReaderViewModel(application: Application) : AndroidViewModel(application) {

    private var fileHandle: Long = 0

    init {
        runCatching {
            fileHandle = ArchiveBookUnRar.loadFile("/sdcard/MT2/Amazing Spider.cbr")
            val entries = ArchiveBookUnRar.getPages(fileHandle)

            debugLog { "  entries.entries: ${entries.size}" }

            entries.entries.forEach {
                debugLog { "Cbr: key: ${it.key} = ${it.value}" }
            }

            val s = application.cacheDir.absolutePath + "/tmp.jpg"
            val extract = ArchiveBookUnRar.extractPage(fileHandle, 2, s)

            debugLog { "extract:$extract" }
        }.onFailure {
            it.printStackTrace()
        }

        ArchiveBookUnRar.free(fileHandle)
    }

    override fun onCleared() {

        debugLog { "Cbr: clean" }
    }
}