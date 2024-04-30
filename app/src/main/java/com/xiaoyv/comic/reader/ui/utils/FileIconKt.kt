package com.xiaoyv.comic.reader.ui.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.InsertDriveFile
import androidx.compose.material.icons.rounded.AudioFile
import androidx.compose.material.icons.rounded.FileOpen
import androidx.compose.material.icons.rounded.Folder
import androidx.compose.material.icons.rounded.FolderZip
import androidx.compose.material.icons.rounded.Image
import androidx.compose.ui.graphics.vector.ImageVector
import com.xiaoyv.comic.datasource.utils.FileExtension.archiveFileExtensions
import com.xiaoyv.comic.datasource.utils.FileExtension.audioExtensions
import com.xiaoyv.comic.datasource.utils.FileExtension.codeFileExtensions
import com.xiaoyv.comic.datasource.utils.FileExtension.imageExtensions
import com.xiaoyv.comic.datasource.utils.FileExtension.installFileExtensions
import com.xiaoyv.comic.datasource.utils.FileExtension.officeFileExtensions
import com.xiaoyv.comic.datasource.utils.FileExtension.textFileExtensions
import com.xiaoyv.comic.datasource.utils.FileExtension.videoExtensions
import java.io.File

/**
 * [iconVector]
 *
 * @author why
 * @since 4/29/24
 */
fun File.iconVector(): ImageVector {
    val element = extension.lowercase()
    return when {
        isDirectory -> Icons.Rounded.Folder
        videoExtensions.contains(element) -> Icons.Rounded.AudioFile
        imageExtensions.contains(element) -> Icons.Rounded.Image
        audioExtensions.contains(element) -> Icons.Rounded.AudioFile
        archiveFileExtensions.contains(element) -> Icons.Rounded.FolderZip
        textFileExtensions.contains(element) -> Icons.Rounded.FileOpen
        codeFileExtensions.contains(element) -> Icons.Rounded.FileOpen
        officeFileExtensions.contains(element) -> Icons.Rounded.FileOpen
        installFileExtensions.contains(element) -> Icons.AutoMirrored.Rounded.InsertDriveFile
        else -> Icons.AutoMirrored.Rounded.InsertDriveFile
    }
}