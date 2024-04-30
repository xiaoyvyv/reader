package com.xiaoyv.comic.reader.data.repository.book

import android.net.Uri
import com.xiaoyv.comic.reader.data.defaultPagingSource
import com.xiaoyv.comic.reader.data.entity.BookEntity
import kotlinx.coroutines.delay

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
            uri = Uri.parse("https://lain.bgm.tv/pic/cover/l/35/b2/303186_oE6Bo.jpg")
        ),
        BookEntity(
            name = "迷宫饭",
            uri = Uri.parse("https://lain.bgm.tv/pic/cover/l/3b/19/105206_7e2mV.jpg")
        ),
        BookEntity(
            name = "一周一次买下同班同学的那些事",
            uri = Uri.parse("https://lain.bgm.tv/pic/cover/l/0d/e8/370560_sgCDF.jpg")
        ),
        BookEntity(
            name = "魔都精兵的奴隶",
            uri = Uri.parse("https://lain.bgm.tv/pic/cover/l/8b/bf/271005_P1j2c.jpg")
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