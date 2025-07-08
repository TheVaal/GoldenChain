package com.integragames.goldenchain.data.util

import android.webkit.JavascriptInterface

class JsInter(
    private val launchNext: () -> Unit,
) {

    @JavascriptInterface
    fun start() {
        launchNext()
    }


}