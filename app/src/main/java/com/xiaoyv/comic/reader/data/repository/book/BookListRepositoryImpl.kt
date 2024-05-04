package com.xiaoyv.comic.reader.data.repository.book

import android.net.Uri
import com.xiaoyv.comic.datasource.FileBookModel
import com.xiaoyv.comic.reader.data.defaultPagingSource
import com.xiaoyv.comic.reader.data.entity.BookEntity
import kotlinx.coroutines.delay
import java.io.File

/**
 * [BookListRepositoryImpl]
 *
 * @author why
 * @since 4/27/24
 */
class BookListRepositoryImpl : BookListRepository {
    private val random = listOf(
        BookEntity(
            name = "我推的孩子",
            model = FileBookModel("")
        ),
        BookEntity(
            name = "迷宫饭",
            model = FileBookModel("")
        ),
        BookEntity(
            name = "一周一次买下同班同学的那些事",
            model = FileBookModel("")
        ),
        BookEntity(
            name = "魔都精兵的奴隶",
            model = FileBookModel("")
        )
    )

    override val pageSource
        get() = defaultPagingSource { current, size ->
            val list = arrayListOf<BookEntity>().apply {
                repeat(size) {
                    add(random.random().copy(name = "Key: $current, Size: $size"))
                }
            }
            delay(1000)

            if (System.currentTimeMillis() % 2L == 0L) {
                error("")
            }

            list
        }
}