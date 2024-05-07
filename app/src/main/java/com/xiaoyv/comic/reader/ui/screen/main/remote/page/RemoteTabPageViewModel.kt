package com.xiaoyv.comic.reader.ui.screen.main.remote.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryEntity
import com.xiaoyv.comic.reader.data.defaultPaging
import com.xiaoyv.comic.reader.data.repository.remote.RemoteDataRepository
import com.xiaoyv.comic.reader.data.repository.remote.RemoteDataRepositoryImpl

/**
 * [RemoteTabPageViewModel]
 *
 * @author why
 * @since 5/4/24
 */
class RemoteTabPageViewModel(
    val remoteDataRepository: RemoteDataRepository,
    val libraryEntity: RemoteLibraryEntity,
) : ViewModel() {

    internal val pageList = defaultPaging {
        remoteDataRepository.getPageSource(
            config = libraryEntity.config,
            libraryId = libraryEntity.id
        )
    }

    class Factory(private val libraryEntity: RemoteLibraryEntity) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RemoteTabPageViewModel::class.java)) {
                return RemoteTabPageViewModel(
                    remoteDataRepository = RemoteDataRepositoryImpl(),
                    libraryEntity = libraryEntity
                ) as T
            }
            return super.create(modelClass)
        }
    }
}
