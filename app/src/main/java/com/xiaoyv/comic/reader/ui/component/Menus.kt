package com.xiaoyv.comic.reader.ui.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember

/**
 * [TopBarOverflowMenu]
 *
 * @author why
 * @since 5/4/24
 */
@Composable
fun TopBarOverflowMenu(
    items: List<String>,
    onMenuItemClick: (Int) -> Unit = {}
) {
    val rememberActionMenuState = remember { mutableStateOf(false) }

    IconButton(
        onClick = {
            rememberActionMenuState.value = true
        }
    ) {
        Icon(
            imageVector = Icons.Default.MoreVert,
            contentDescription = "更多"
        )
    }

    TopBarOverflowContent(
        items = items,
        state = rememberActionMenuState,
        onMenuItemClick = onMenuItemClick
    )
}

/**
 * 导航栏下拉菜单
 */
@Composable
fun TopBarOverflowContent(
    items: List<String>,
    state: MutableState<Boolean>,
    onMenuItemClick: (Int) -> Unit
) {
    DropdownMenu(
        expanded = state.value,
        onDismissRequest = { state.value = false },
    ) {
        items.forEachIndexed { index, item ->
            DropdownMenuItem(
                text = {
                    Text(
                        text = item,
                        style = MaterialTheme.typography.titleMedium
                    )
                },
                onClick = {
                    onMenuItemClick(index)
                    state.value = false
                }
            )
        }
    }
}
