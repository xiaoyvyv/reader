package com.xiaoyv.comic.reader.ui.page.bookshelf

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.xiaoyv.comic.era.NativeLib
import com.xiaoyv.comic.era.NativeMobi
import com.xiaoyv.comic.reader.ui.utils.context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

class BookShelfViewModel(application: Application) : AndroidViewModel(application) {
    private val mobiDir by lazy {
        requireNotNull(context.getExternalFilesDir("mobi")).apply {
            if (!exists()) mkdirs()
        }
    }
    private val mobiOutDir by lazy {
        requireNotNull(context.getExternalFilesDir("mobi-out")).apply {
            if (!exists()) mkdirs()
        }
    }

    fun convert(it: Uri?) {
        viewModelScope.launch(Dispatchers.IO) {
            val uri = requireNotNull(it)
            val mobiFile = File(mobiDir, "mock.mobi")

            context.contentResolver.openInputStream(uri).use { input ->
                mobiFile.outputStream().use { out ->
                    requireNotNull(input).copyTo(out)
                }
            }


//            convertEpub(mobiFile)
            loadCover(mobiFile)
        }
    }

    private fun convertEpub(file: File) {
        val outFile = File(mobiOutDir, "convert-test")
        NativeLib.mobi.convertToEpub(file.absolutePath, outFile.absolutePath)
        Log.e("Convert-Mobi", "convert: finish!")
    }

    private fun loadCover(file: File) {
        val outFile = File(mobiOutDir, "cover-test")
        NativeLib.mobi.extractCover(file.absolutePath, outFile.absolutePath)
        Log.e("Convert-Mobi", "extract cover: finish!")
    }
}