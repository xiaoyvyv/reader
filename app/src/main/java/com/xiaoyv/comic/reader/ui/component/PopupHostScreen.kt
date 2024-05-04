package com.xiaoyv.comic.reader.ui.component

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xiaoyv.comic.reader.navigation.materialFadeThroughIn
import com.xiaoyv.comic.reader.navigation.materialFadeThroughOut

@Composable
fun PopupHostScreen(
    visible: Boolean,
    onDismissRequest: () -> Unit,
    content: @Composable ColumnScope.() -> Unit,
) {
    AnimatedVisibility(
        visible = visible,
        enter = materialFadeThroughIn(),
        exit = materialFadeThroughOut()
    ) {
        BackHandler(onBack = onDismissRequest)
        Column(modifier = Modifier.fillMaxSize()) {
            content()
        }
    }
}
