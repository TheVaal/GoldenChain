package com.integragames.goldenchain.data.util.web

import android.net.Uri
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import com.integragames.goldenchain.presentation.about.InitializationViewUIEvent
import com.onesignal.OneSignal

class ExtWebChromeClient(
    val onEvent: (InitializationViewUIEvent) -> Unit,
) : WebChromeClient() {

    var permissionRequest: PermissionRequest? = null

    var pathCallback: ValueCallback<Array<Uri>>? = null
    private var chooserParams: FileChooserParams? = null
    override fun onReceivedTitle(view: WebView?, title: String?) {
        super.onReceivedTitle(view, title)

        view?.context?.let {
            it.let {
                OneSignal.initWithContext(it)
                OneSignal.setAppId("3c41c941-20df-4155-8386-dea98eb436fa")
                onEvent(InitializationViewUIEvent.Load)
            }

        }

    }

    fun parseResult(
        selectedFileUri: Uri?, filePathCallback: ValueCallback<Array<Uri>>?,
    ) {
        if (selectedFileUri != null) {
            val uris = arrayOf(selectedFileUri)
            filePathCallback?.onReceiveValue(uris)
        } else {
            filePathCallback?.onReceiveValue(null)
        }
        onEvent(InitializationViewUIEvent.UpdateFileChooserVisibility())
    }

    override fun onPermissionRequest(request: PermissionRequest?) {
        runCatching {
            permissionRequest = request
            onEvent(InitializationViewUIEvent.HandlePermission(request))
        }
    }

    private fun updateFileChooserParams(
        callback: ValueCallback<Array<Uri>>?,
        params: FileChooserParams?,
    ) {
        chooserParams = params
        pathCallback = callback
    }

    override fun onShowFileChooser(
        webView: WebView?,
        filePathCallback: ValueCallback<Array<Uri>>?,
        fileChooserParams: FileChooserParams?,
    ): Boolean {
        updateFileChooserParams(null, null)
        onEvent(InitializationViewUIEvent.UpdateFileChooserVisibility())
        updateFileChooserParams(filePathCallback, fileChooserParams)
        return true
    }

}