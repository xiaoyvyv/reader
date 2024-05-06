package com.xiaoyv.comic.reader.ui.screen.feature.bookseries

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.xiaoyv.comic.datasource.series.SeriesModel
import com.xiaoyv.comic.datasource.series.SeriesModelHelper
import com.xiaoyv.comic.reader.ui.screen.feature.bookinfo.navigateBookInfo
import com.xiaoyv.comic.reader.ui.utils.requireString


@VisibleForTesting
internal const val ARG_BOOK_SERIES_MODEL = "model"
const val ROUTE_BOOK_SERIES = "bookseries"

internal class BookSeriesArguement(val model: SeriesModel) {
    constructor(savedStateHandle: SavedStateHandle) : this(
        model = SeriesModelHelper.deserialize(savedStateHandle.requireString(ARG_BOOK_SERIES_MODEL))
    )
}

fun NavController.navigateBookSeries(
    model: SeriesModel,
    navOptions: NavOptionsBuilder.() -> Unit = {}
) {
    val newRoute = "$ROUTE_BOOK_SERIES/${SeriesModelHelper.serialize(model)}"
    navigate(newRoute) {
        navOptions()
    }
}

fun NavGraphBuilder.addBookSeriesScreen(navController: NavController) {
    composable(
        route = "$ROUTE_BOOK_SERIES/{$ARG_BOOK_SERIES_MODEL}",
        arguments = listOf(navArgument(ARG_BOOK_SERIES_MODEL) { type = NavType.StringType })
    ) {
        BookSeriesRoute(
            onNavUp = navController::navigateUp,
            onBookClick = {
                navController.navigateBookInfo(it)
            }
        )
    }
}
