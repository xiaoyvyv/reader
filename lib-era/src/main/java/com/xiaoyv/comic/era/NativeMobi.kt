package com.xiaoyv.comic.era

class NativeMobi {
    init {
        System.loadLibrary("era")
    }

    /**
     * Mobi 转为 Epub
     *
     * @param mobiPath Mobi 文件路径
     * @param outPath 输出文件路径前缀，实际路径会拼接 `.epub`
     */
    external fun convertToEpub(mobiPath: String, outPath: String): Int

    /**
     * 提取 Mobi 封面
     *
     * @param mobiPath Mobi 文件路径
     * @param outPath 输出文件路径前缀，实际路径会拼接 `_cover.jpg`
     */
    external fun extractCover(mobiPath: String, outPath: String): Int
}