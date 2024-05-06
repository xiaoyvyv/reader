package com.xiaoyv.comic.reader.ui.screen.main.remote.page

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryConfig
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryEntity
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryType
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
    val remoteLibraryEntity: RemoteLibraryEntity,
) : ViewModel() {

    internal val pageList = defaultPaging {
        val config = RemoteLibraryConfig(
            type = RemoteLibraryType.TYPE_KOMGA,
            username = "2333@qq.com",
            password = "2333@qq.com",
            baseUrl = "https://manga.pilipiliultra.com"
        )

        remoteDataRepository.getPageSource(config, remoteLibraryEntity.id)
    }

    class Factory(private val remoteLibraryEntity: RemoteLibraryEntity) :
        ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(RemoteTabPageViewModel::class.java)) {
                return RemoteTabPageViewModel(
                    remoteDataRepository = RemoteDataRepositoryImpl(),
                    remoteLibraryEntity = remoteLibraryEntity
                ) as T
            }
            return super.create(modelClass)
        }
    }
}
