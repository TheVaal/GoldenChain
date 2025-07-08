package com.integragames.goldenchain.data.util.web

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.view.View
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.WebResourceRequest
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.integragames.goldenchain.data.util.JsInter
import com.integragames.goldenchain.presentation.about.InitializationViewUIEvent
import java.io.File

@SuppressLint("ObsoleteSdkInt", "SetJavaScriptEnabled")
@Composable
fun rememberWebView(
    chromeClient: ExtWebChromeClient,
    context: Context = LocalContext.current,
    onEvent: (InitializationViewUIEvent) -> Unit,
    ): WebView {
    return remember {
        WebView(context).apply {
            isSaveEnabled = true
            isFocusable = true
            isFocusableInTouchMode = true
            isVerticalScrollBarEnabled = false
            isHorizontalScrollBarEnabled = false
            CookieManager.getInstance().apply cookies@{
                setAcceptCookie(true)
                setAcceptThirdPartyCookies(this@apply, true)
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                importantForAutofill = WebView.IMPORTANT_FOR_AUTOFILL_NO_EXCLUDE_DESCENDANTS
            }
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            webViewClient = getWebClient()

            webChromeClient = chromeClient
            setLayerType(View.LAYER_TYPE_HARDWARE, null)
            with(settings) {
                mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
                javaScriptEnabled = true
                domStorageEnabled = true
                loadsImagesAutomatically = true
                databaseEnabled = true
                useWideViewPort = true
                allowFileAccess = true
                javaScriptCanOpenWindowsAutomatically = true
                loadWithOverviewMode = true
                allowContentAccess = true
                setSupportMultipleWindows(false)
                builtInZoomControls = true
                displayZoomControls = false
                cacheMode = WebSettings.LOAD_DEFAULT
                mediaPlaybackRequiresUserGesture = false
                setGeolocationEnabled(false)
                safeBrowsingEnabled = true
            }
            addJavascriptInterface(JsInter{onEvent(InitializationViewUIEvent.Open)}, "android")
        }

    }
}

fun getChromeClient(
    onEvent: (InitializationViewUIEvent) -> Unit,
): ExtWebChromeClient = ExtWebChromeClient(

    onEvent = onEvent
)

private fun getWebClient() = object : WebViewClient() {

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        return try {
            val urlT = request?.url?.toString() ?: return false
            urlT.takeUnless { it.startsWith("http") }
                ?.let {
                    view?.context?.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(it)))
                    true
                } ?: false
        } catch (e: Exception) {
            true
        }
    }
}

fun Context.createFileForCameraImageFlaming(): File {
    val imageFileName = "${System.currentTimeMillis()}"
    val storageDir = this.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}