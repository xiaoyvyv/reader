package com.xiaoyv.comic.reader.data.repository.file

import android.Manifest
import android.os.Build
import android.os.Environment
import androidx.core.content.PermissionChecker
import com.xiaoyv.comic.reader.application
import com.xiaoyv.comic.reader.data.entity.FileEntity
import com.xiaoyv.comic.reader.ui.utils.formatFileSize
import com.xiaoyv.comic.reader.ui.utils.formatTime
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * [BookFileRepositoryImpl]
 *
 * @author why
 * @since 4/27/24
 */
class BookFileRepositoryImpl : BookFileRepository {

    override suspend fun listFiles(dir: File): List<FileEntity> {


        return withContext(Dispatchers.IO) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                check(Environment.isExternalStorageManager()) { "Permission denied!" }
            } else {
                check(
                    PermissionChecker.checkSelfPermission(
                        application,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ) == PermissionChecker.PERMISSION_GRANTED
                ) { "Permission denied!" }
            }


            if (!dir.isDirectory) {
                return@withContext listOf()
            }

            val entities = dir.listFiles().orEmpty()
                .sortedBy { it.name.lowercase() }
                .sortedBy { if (it.isDirectory) 0 else 1 }
                .map {
                    FileEntity(
                        file = it,
                        extension = it.extension,
                        date = it.lastModified().formatTime("yyyy-MM-dd"),
                        length = it.length(),
                        lengthText = it.length().formatFileSize(),
                        cdParent = false
                    )
                }
                .toMutableList()

            entities.add(
                0, FileEntity(
                    file = dir.parentFile ?: Environment.getExternalStorageDirectory(),
                    cdParent = true
                )
            )

            entities
        }
    }
}