package com.xiaoyv.comic.reader.ui.screen.feature.bookreader

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
import com.xiaoyv.comic.reader.ui.utils.requireString


@VisibleForTesting
internal const val ARG_BOOK_READER_MODEL = "model"
const val ROUTE_BOOK_READER = "bookreader"

internal class ReaderViewArguement(val model: BookModel) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        model = BookModelHelper.deserialize(savedStateHandle.requireString(ARG_BOOK_READER_MODEL))
    )
}

fun NavController.navigateBookReader(
    model: BookModel,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    val newRoute = "$ROUTE_BOOK_READER/${BookModelHelper.serialize(model)}"
    navigate(newRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.addBookReaderScreen(navController: NavController) {
    // 阅读页
    composable(
        route = "$ROUTE_BOOK_READER/{$ARG_BOOK_READER_MODEL}",
        arguments = listOf(navArgument(ARG_BOOK_READER_MODEL) { type = NavType.StringType })
    ) {
        BookReaderRoute(
            onNavUp = navController::navigateUp
        )
    }
}
