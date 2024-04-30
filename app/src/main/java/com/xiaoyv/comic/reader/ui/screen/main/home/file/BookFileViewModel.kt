package com.xiaoyv.comic.reader.ui.screen.main.home.file

import android.app.Application
import android.os.Environment
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import com.xiaoyv.comic.reader.data.entity.FileEntity
import com.xiaoyv.comic.reader.data.repository.file.BookFileRepository
import com.xiaoyv.comic.reader.data.repository.file.BookFileRepositoryImpl
import com.xiaoyv.comic.reader.ui.utils.debugLog
import com.xiaoyv.comic.reader.ui.utils.mutableStateFlowOf
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File

/**
 * [BookFileViewModel]
 *
 * @author why
 * @since 4/26/24
 */
class BookFileViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookFileRepository = BookFileRepositoryImpl()

    private val root by lazy {
        runCatching { Environment.getExternalStorageDirectory() }
            .getOrDefault(File(""))
    }

    private val _fileState = mutableStateFlowOf<BookFileState>(BookFileState())
    internal val fileState get() = _fileState

    private val _currentDir = mutableStateFlowOf<File>(root)
    internal val currentDir get() = _currentDir

    init {
        refresh()
    }

    fun canNavUp(): Boolean {
        val parent = _currentDir.value.parentFile ?: root
        return parent.canRead()
    }

    fun navUp() {
        _currentDir.update {
            _currentDir.value.parentFile ?: root
        }
        refresh()
    }

    fun navTo(fileEntity: FileEntity) {
        // 上一级
        if (fileEntity.cdParent) {
            if (canNavUp()) {
                navUp()
            }
            return
        }

        // 打开文件夹
        if (fileEntity.file.isDirectory) {
            _currentDir.update { fileEntity.file }
            refresh()
        }
    }

    private fun refresh() {
        viewModelScope.launch(context = CoroutineExceptionHandler { _, throwable ->
            _fileState.update {
                it.copy(
                    loadState = LoadState.Error(throwable),
                    files = emptyList()
                )
            }
        }) {
            _fileState.update {
                it.copy(
                    loadState = LoadState.Loading,
                    files = emptyList()
                )
            }

            val listFiles = repository.listFiles(currentDir.value)

            _fileState.update {
                it.copy(
                    loadState = LoadState.NotLoading(true),
                    files = listFiles
                )
            }
        }
    }
}