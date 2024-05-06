package com.xiaoyv.comic.reader.ui.screen.main.home

import android.app.Application
import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.artifex.mupdf.fitz.Document
import com.artifex.mupdf.fitz.Matrix
import com.artifex.mupdf.fitz.android.AndroidDrawDevice
import com.xiaoyv.comic.reader.ui.utils.context
import com.xiaoyv.comic.reader.ui.utils.mutableStateFlowOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

data class BookShelfUiState(
    var loading: Boolean = true,
    var books: List<String> = emptyList(),
    var btnText: String = "默认值",
    var bitmap: Bitmap? = null,
)

class HomeTabViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = mutableStateFlowOf(BookShelfUiState(loading = true))
    val uiState get() = _uiState.asStateFlow()

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
//            loadCover(mobiFile)
            loadPdf(mobiFile)
        }
    }

    private fun loadPdf(file: File) {
        viewModelScope.launch(Dispatchers.IO) {
            var canvasW = 1080
            var canvasH = 1920

            val density: Float = context.resources.displayMetrics.density
            var widthInPt = canvasW / density * 72f
            var heightInPt = canvasH / density * 72f

            val document = Document.openDocument(file.absolutePath)
            val needsPassword = document.needsPassword()

            val metaTitle = document.getMetaData(Document.META_INFO_TITLE)
            val isReflowable = document.isReflowable()
            if (isReflowable) {
            }
            val pageCount = document.countPages()

            val page = document.loadPage(1)
            var fitPage = false
            var searchNeedle: String? = null
            var zoom = 1f
            try {
                val ctm: Matrix
                ctm =
                    if (fitPage) AndroidDrawDevice.fitPage(
                        page,
                        canvasW,
                        canvasH
                    ) else AndroidDrawDevice.fitPageWidth(page, canvasW)
                var links = page.getLinks()
                if (links != null) for (link in links) link.getBounds().transform(ctm)
                if (searchNeedle != null) {
                    var hits = page.search(searchNeedle)
                    if (hits != null) for (hit in hits) for (chr in hit) chr.transform(ctm)
                }
                if (zoom != 1f) ctm.scale(zoom)
                val bitmap = AndroidDrawDevice.drawPage(page, ctm)
                val width = bitmap.width
                val height = bitmap.height
                _uiState.update { it.copy(bitmap = bitmap) }
                page.destroy()
            } catch (x: Throwable) {
                x.printStackTrace()
            }
        }
    }

    private fun convertEpub(file: File) {
        val outFile = File(mobiOutDir, "convert-test")
//        NativeLib.mobi.convertToEpub(file.absolutePath, outFile.absolutePath)
        Log.e("Convert-Mobi", "convert: finish!")
    }

    private fun loadCover(file: File) {
        val outFile = File(mobiOutDir, "cover-test")
//        NativeLib.mobi.extractCover(file.absolutePath, outFile.absolutePath)
        Log.e("Convert-Mobi", "extract cover: finish!")
    }

}