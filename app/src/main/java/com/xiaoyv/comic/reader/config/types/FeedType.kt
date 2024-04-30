package com.xiaoyv.comic.reader.config.types

/**
 * [FeedType]
 *
 * @author why
 * @since 4/27/24
 */
sealed interface FeedType {
    data object SmallGrid : FeedType
    data object LargeGrid : FeedType
    data object List : FeedType
}
