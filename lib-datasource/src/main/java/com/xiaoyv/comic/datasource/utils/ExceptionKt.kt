package com.xiaoyv.comic.datasource.utils

/**
 * [runCatching]
 *
 * @author why
 * @since 5/1/24
 */
inline fun <R> runCatchingPrint(block: () -> R): Result<R> {
    return try {
        Result.success(block())
    } catch (e: Throwable) {
        e.printStackTrace()
        Result.failure(e)
    }
}

inline fun <R> runCatchingDefault(default: R, block: () -> R): R {
    return try {
        block()
    } catch (e: Throwable) {
        default
    }
}