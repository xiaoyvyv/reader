package com.xiaoyv.comic.reader.ui.screen.feature.bookinfo

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.comic.datasource.BookModel
import com.xiaoyv.comic.datasource.BookModelHelper
import com.xiaoyv.comic.reader.ui.screen.feature.bookreader.navigateBookReader
import com.xiaoyv.comic.reader.ui.utils.requireString
import com.xiaoyv.comic.reader.ui.utils.urlDecode
import com.xiaoyv.comic.reader.ui.utils.urlEncode
import java.io.File


@VisibleForTesting
internal const val ARG_ROUTE_BOOK_INFO_MODEL = "model"
const val ROUTE_BOOK_INFO = "bookinfo"

internal class BookInfoArguement(val model: BookModel) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        model = BookModelHelper.deserialize(savedStateHandle.requireString(ARG_ROUTE_BOOK_INFO_MODEL))
    )
}

fun NavController.navigateBookInfo(
    model: BookModel,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    val newRoute = "$ROUTE_BOOK_INFO/${BookModelHelper.serialize(model)}"
    navigate(newRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.addBookInfoScreen(navController: NavController) {
    // 阅读页
    composable(
        route = "$ROUTE_BOOK_INFO/{$ARG_ROUTE_BOOK_INFO_MODEL}",
        arguments = listOf(navArgument(ARG_ROUTE_BOOK_INFO_MODEL) { type = NavType.StringType })
    ) {
        BookInfoRoute(
            onNavUp = navController::navigateUp,
            onReadClick = {
                it.model?.let { model ->
                    navController.navigateBookReader(model)
                }
            }
        )
    }
}
