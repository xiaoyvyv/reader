package com.xiaoyv.comic.reader.ui.page.bookshelf

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.xiaoyv.comic.reader.ui.utils.supportWideScreen


@Composable
fun BookShelfScreen(
    viewModel: BookShelfViewModel
) {

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) {
            viewModel.convert(it)
        }

    Surface(modifier = Modifier.supportWideScreen()) {
        Scaffold(
            topBar = {

            },
            content = { paddingValues ->
                Column(
                    modifier = Modifier.padding(paddingValues)
                ) {
                    Button(
                        onClick = {
                            launcher.launch("application/x-mobipocket-ebook")
                        }
                    ) {
                        Text(text = "转 Mobi ")
                    }
                }
            }
        )
    }
}