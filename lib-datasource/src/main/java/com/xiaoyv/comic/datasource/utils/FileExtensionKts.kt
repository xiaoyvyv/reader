package com.xiaoyv.comic.datasource.utils

object FileExtension {

    /**
     * 视频
     */
    val videoExtensions = listOf(
        "mp4", "avi", "mov", "wmv", "flv",
        "mkv", "mpg", "mpeg", "rm", "rmvb",
        "3gp", "3g2", "webm", "ts", "vob",
        "m4v", "ogg", "ogv", "m2ts", "divx",
        "dat", "asf", "m2v"
    )

    /**
     * 音频
     */
    val audioExtensions = listOf(
        "mp3", "wav", "ogg", "m4a", "flac",
        "aac", "wma", "aiff", "ape", "opus",
        "amr", "mid", "midi", "pcm", "alac"
    )

    /**
     * 图片
     */
    val imageExtensions = listOf(
        "jpg", "jpeg", "png", "gif", "bmp", "webp",
        "tiff", "webp", "svg", "ico", "psd"
    )

    /**
     * 纯文本
     */
    val textFileExtensions = listOf("txt", "md", "rtf", "log", "ini")

    /**
     * 编程代码文件扩展名列表
     */
    val codeFileExtensions = listOf(
        "css", "js", "java", "kt", "c", "html", "htm",
        "cpp", "h", "hpp", "py", "rb",
        "php", "xml", "json", "yaml", "yml",
        "ini", "cfg", "conf", "sql", "sh",
        "bat", "ps1", "pl", "asp", "jsp",
        "ejs", "aspx", "jsp", "cs", "vb",
        "go", "lua", "swift", "scala", "perl"
    )

    /**
     * 办公文件扩展名
     */
    val officeFileExtensions = listOf(
        "doc", "docx", "xls", "xlsx", "ppt",
        "pptx", "odt", "ods", "odp", "pdf", "csv"
    )

    /**
     * 安装文件
     */
    val installFileExtensions = listOf(
        "exe", "msi", "dmg",
        "pkg", "deb", "rpm"
    )

    /**
     * 压缩文件
     */
    val archiveFileExtensions = listOf(
        "zip", "rar", "7z", "tar",
        "gz", "gzip", "bz2", "bzip2",
        "tar.gz", "tar.bz2"
    )

    /**
     * 阅读类
     */
    val readerFileExtensions = listOf(
        "mobi", "epub", "cbr", "cbz", "cbt", "djvu", "fb", "pdf"
    )
}