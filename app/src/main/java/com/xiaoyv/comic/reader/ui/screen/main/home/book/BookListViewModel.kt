package com.xiaoyv.comic.reader.ui.screen.main.home.book

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.xiaoyv.comic.reader.config.types.FeedType
import com.xiaoyv.comic.reader.data.defaultPaging
import com.xiaoyv.comic.reader.data.repository.book.BookListRepository
import com.xiaoyv.comic.reader.data.repository.book.BookListRepositoryImpl
import com.xiaoyv.comic.reader.ui.utils.mutableStateFlowOf

/**
 * [BookListViewModel]
 *
 * @author why
 * @since 4/26/24
 */
class BookListViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: BookListRepository = BookListRepositoryImpl()

    private val _state = mutableStateFlowOf<BookListState>(BookListState.Initial)
    internal val state get() = _state

    private val _listType = mutableStateFlowOf<FeedType>(FeedType.SmallGrid)
    internal val listType get() = _listType

    internal val bookList = defaultPaging {
        repository.pageSource
    }
}