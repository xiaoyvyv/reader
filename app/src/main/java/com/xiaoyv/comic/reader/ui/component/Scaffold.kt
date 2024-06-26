package com.xiaoyv.comic.reader.ui.component

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

/**
 * [ScaffoldWrap]
 *
 * @author why
 * @since 4/27/24
 */
@Composable
fun ScaffoldWrap(
    modifier: Modifier = Modifier,
    topBar: @Composable () -> Unit = {},
    bottomBar: @Composable () -> Unit = {},
    snackbarHost: @Composable ((SnackbarHostState) -> Unit)? = null,
    floatingActionButton: @Composable () -> Unit = {},
    floatingActionButtonPosition: FabPosition = FabPosition.End,
    containerColor: Color = MaterialTheme.colorScheme.background,
    contentColor: Color = contentColorFor(containerColor),
    contentWindowInsets: WindowInsets = ScaffoldDefaults.contentWindowInsets,
    content: @Composable (PaddingValues) -> Unit
) {
    val snackbarState = remember { SnackbarHostState() }
    val delegateSnackbarState = remember { DelegateSnackbarHostState(snackbarState) }

    CompositionLocalProvider(LocalSnackbarHostState provides delegateSnackbarState) {
        Scaffold(
            modifier,
            topBar,
            bottomBar,
            snackbarHost = {
                if (snackbarHost != null) snackbarHost(snackbarState) else SnackbarHost(
                    modifier = Modifier.navigationBarsPadding(),
                    hostState = snackbarState,
                )
            },
            floatingActionButton,
            floatingActionButtonPosition,
            containerColor,
            contentColor,
            contentWindowInsets,
        ) {
            content(it)
        }
    }
}