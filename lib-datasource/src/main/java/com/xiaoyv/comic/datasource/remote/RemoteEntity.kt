package com.xiaoyv.comic.datasource.remote

import java.util.Date

/**
 * [RemoteLibraryEntity]
 *
 * @author why
 * @since 5/4/24
 */
data class RemoteLibraryEntity(
    var id: String = "",
    var title: String = ""
)

data class RemoteBookEntity(
    var id: String = "",
    var libraryId: String = "",
    var name: String = "",
    var cover: String = "",
    var bookCount: Int = 0,
    var title: String = "",
    var summary: String = "",
    var publisher: String = "",
    var genres: List<String> = emptyList(),
    var authors: List<String> = emptyList(),
    var language: String = "",
    var status: String = "",
    var created: Date? = null,
    var lastModified: Date? = null,
    var tags: List<String>? = null,
    var alternateTitles: List<String>? = null
)

data class RemoteBookUserEntity(
    var username: String = "",
    var id: String = "",
    var roles: List<String> = emptyList(),
)