package com.xiaoyv.comic.reader

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Modifier
import com.google.accompanist.adaptive.calculateDisplayFeatures
import com.xiaoyv.comic.reader.ui.component.LocalPopupHostState
import com.xiaoyv.comic.reader.ui.component.PopupHostScreen
import com.xiaoyv.comic.reader.ui.component.rememberPopupHostState
import com.xiaoyv.comic.reader.ui.theme.ComicReaderTheme
import com.xiaoyv.comic.reader.ui.utils.debugLog


class MainActivity : ComponentActivity() {
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            debugLog { "权限申请结果：${it.resultCode == RESULT_OK}" }
        }

    private val launcherPermission =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) {
            debugLog { "权限申请结果: $it" }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            ComicReaderTheme {
                val windowSize = calculateWindowSizeClass(this)
                val displayFeatures = calculateDisplayFeatures(this)
                val popupHostState = rememberPopupHostState()

                CompositionLocalProvider(LocalPopupHostState provides popupHostState) {
                    Box(modifier = Modifier.fillMaxSize()) {
                        MainPage(
                            windowSize = windowSize,
                            displayFeatures = displayFeatures,
                        )

                        PopupHostScreen(
                            visible = popupHostState.visible,
                            onDismissRequest = popupHostState::hide,
                            content = popupHostState.content,
                        )
                    }
                }
            }
        }

        requestPermission()
    }


    /**
     * 检查Android 11或更高版本的文件权限
     */
    private fun requestPermission() {
        // Android 11 (Api 30) 或更高版本的写文件权限需要特殊申请，需要动态申请管理所有文件的权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                val uri = Uri.fromParts("package", packageName, null)
                intent.setData(uri)
                try {
                    launcher.launch(intent)
                } catch (e: Exception) {
                    Toast.makeText(this, "授权错误！", Toast.LENGTH_SHORT).show()
                    try {
                        val intent2 = Intent()
                        intent2.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                        launcher.launch(intent2)
                    } catch (e2: Exception) {
                        Toast.makeText(this, "授权错误！", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        } else {
            launcherPermission.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)

            debugLog {
                "此手机版本小于Android 11，版本为：API ${Build.VERSION.SDK_INT}，不需要申请文件管理权限"
            }
        }
    }
}