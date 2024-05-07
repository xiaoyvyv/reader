package com.xiaoyv.comic.reader.ui.component

import android.app.AlertDialog
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.CookieManager
import android.webkit.SslErrorHandler
import android.webkit.URLUtil
import android.webkit.WebChromeClient
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.UiComposable
import androidx.compose.ui.viewinterop.AndroidView
import com.xiaoyv.comic.datasource.book.remote.impl.RemoteLibraryFactory
import com.xiaoyv.comic.reader.ui.utils.debugLog
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull

/**
 * [WebViewWrap]
 *
 * @author why
 * @since 5/6/24
 */
@Composable
@UiComposable
fun WebViewWrap(
    modifier: Modifier = Modifier,
    url: String,
    onOpenUrl: (String) -> Unit = {},
    onLoadStart: (String) -> Unit = {},
    onLoadFinish: (String) -> Unit = {}
) {
    LaunchedEffect(url) {
        val httpUrl = url.toHttpUrlOrNull() ?: return@LaunchedEffect
        RemoteLibraryFactory.cookiejar.loadForRequest(httpUrl).forEach {
            val cookie="${it.name}=${it.value}"
            debugLog { "WebView: setcookie = $cookie" }
            CookieManager.getInstance().setCookie(url, cookie)
        }
        CookieManager.getInstance().flush()
    }

    AndroidView(
        modifier = modifier,
        factory = {
            WebView(it).apply {
                initSettings()
                webViewClient = WebViewClientWrap()
                webChromeClient = WebChromeClientWrap()

                debugLog { "WebView:init" }
            }
        },
        onReset = {
            (it.webViewClient as WebViewClientWrap).also { wrap ->
                wrap.onOpenUrl = null
                wrap.onLoadStart = null
                wrap.onLoadFinish = null
            }
            it.stopLoading()
            it.clearHistory()

            debugLog { "WebView:onReset web=${it.hashCode()}" }
        },
        onRelease = {
            it.stopLoading()
            it.clearHistory()
            it.destroy()

            debugLog { "WebView:onRelease" }
        },
        update = {
            (it.webViewClient as WebViewClientWrap).also { wrap ->
                wrap.onOpenUrl = onOpenUrl
                wrap.onLoadStart = onLoadStart
                wrap.onLoadFinish = onLoadFinish
            }

            it.stopLoading()
            it.clearHistory()
            it.loadUrl(url)

            debugLog { "WebView:update load $url web=${it.hashCode()}" }
        }
    )
}

/**
 * 设置项目
 */
@Suppress("DEPRECATION")
private fun WebView.initSettings() {
    settings.javaScriptEnabled = true
    // 设置屏幕适应，将图片调整到适合WebView的大小
    settings.useWideViewPort = true
    // 设置屏幕适应，缩放至屏幕的大小
    settings.loadWithOverviewMode = true
    // 缩放操作
    settings.setSupportZoom(true)
    // 设置使用内置的缩放控件
    settings.builtInZoomControls = true
    // 隐藏原生的缩放控件
    settings.displayZoomControls = false

    // 其他细节操作
    settings.allowFileAccess = true
    settings.allowContentAccess = true
    settings.allowFileAccessFromFileURLs = true
    // 支持通过JS打开新窗口
    settings.setSupportMultipleWindows(true)
    // 支持自动加载图片
    settings.loadsImagesAutomatically = true
    // 设置编码格式
    settings.defaultTextEncodingName = "UTF-8"
    settings.setGeolocationEnabled(true)
    settings.cacheMode = WebSettings.LOAD_DEFAULT
    settings.databaseEnabled = true
    settings.domStorageEnabled = true
    settings.blockNetworkImage = false
    // 取消系统字体缩放
    settings.textZoom = 100

    settings.databasePath = context.cacheDir.absolutePath + "/databases"

    // 设置混合协议
    settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW

    CookieManager.getInstance().setAcceptCookie(true)
    CookieManager.getInstance().setAcceptThirdPartyCookies(this, true)
}

class WebViewClientWrap(
    var onOpenUrl: ((String) -> Unit)? = null,
    var onLoadStart: ((String) -> Unit)? = null,
    var onLoadFinish: ((String) -> Unit)? = null,
) : WebViewClient() {
    override fun onReceivedError(
        view: WebView,
        request: WebResourceRequest,
        error: WebResourceError
    ) {
        super.onReceivedError(view, request, error)
        debugLog { "Chrome: Error ${error.errorCode}, ${error.description}" }
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        debugLog { "Chrome: HttpError ${errorResponse?.statusCode}, ${errorResponse?.reasonPhrase}" }
    }

    override fun shouldOverrideUrlLoading(webView: WebView, request: WebResourceRequest): Boolean {
        val linkUrl = request.url.toString()

        // 多窗口
        if (webView.settings.supportMultipleWindows()) {
            onOpenUrl?.invoke(linkUrl)
            return true
        }

        if (URLUtil.isNetworkUrl(linkUrl)) {
            webView.loadUrl(linkUrl)
            return true
        }

        webView.post { onOpenUrl?.invoke(linkUrl) }
        return true
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        view.post { onLoadStart?.invoke(url) }
        debugLog { "WebView:onLoadStart web=${view.hashCode()}" }
    }

    override fun onPageFinished(view: WebView, url: String) {
        super.onPageFinished(view, url)
        view.post { onLoadFinish?.invoke(url) }
        debugLog { "WebView:onLoadFinish web=${view.hashCode()}" }
    }

    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError?) {
        val alertDialog = AlertDialog.Builder(view.context)
            .setTitle("温馨提示")
            .setMessage("该网页证书校验错误，是否继续加载网页？")
            .setPositiveButton("继续") { _, _ -> handler.proceed() }
            .setNegativeButton("取消") { _, _ -> handler.cancel() }
            .setCancelable(false)
            .create()

        alertDialog.apply { setCanceledOnTouchOutside(false) }
        alertDialog.show()
    }
}

class WebChromeClientWrap : WebChromeClient()