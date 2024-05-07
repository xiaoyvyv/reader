package com.xiaoyv.comic.reader.data.repository.file

import com.xiaoyv.comic.reader.data.entity.FileEntity
import java.io.File

/**
 * [BookFileRepository]
 *
 * @author why
 * @since 4/27/24
 */
interface BookFileRepository {

    suspend fun listFiles(dir: File): List<FileEntity>


    suspend fun scanBooks(): Result<List<FileEntity>>
}