package com.integragames.goldenchain.presentation.about

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.webkit.PermissionRequest
import android.webkit.ValueCallback
import android.webkit.WebChromeClient
import android.webkit.WebView
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.result.ActivityResult
import androidx.lifecycle.ViewModel
import com.integragames.goldenchain.data.util.web.ExtWebChromeClient
import com.onesignal.OneSignal
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class WebViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(WebViewState())
    val uiState = _uiState.asStateFlow()
    private var state: WebViewState
        get() = _uiState.value
        set(newState) {
            _uiState.update { newState }
        }

    fun load(
        webView: WebView,
        params: String,
        userId: String,
        launcherHolder: LauncherHolder,
        chrome: ChromeHolder,
        sharedPreferences: SharedPreferences? = null
    ) {
        state = state.copy(
            screen = webView,
            userId = userId,
            launchers = launcherHolder,
            chromeClient = chrome,
            sharedPreferences = sharedPreferences
        )
        state.screen?.loadUrl(params)
    }

    fun onEvent(event: InitializationViewUIEvent) {
        when (event) {
            is InitializationViewUIEvent.Load -> {
                if (!state.isFirstLoad) return
                state = if (state.userId.isNotEmpty()) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        state.launchers.permissionLauncher?.launch(Manifest.permission.POST_NOTIFICATIONS)
                    }
                    OneSignal.setExternalUserId(
                        state.sharedPreferences?.getString("userId", "") ?: ""
                    )
                    state.copy(

                        isLoading = false,
                        isFirstLoad = false,
                        userId = ""
                    )

                } else {
                    state.copy(isLoading = false, isFirstLoad = false)
                }
            }

            is InitializationViewUIEvent.OnPermissionResult -> {
                runCatching {
                    val request = state.chromeClient.chrome?.permissionRequest
                    if (event.granted) {

                        request?.grant(request.resources)
                        clearPermission()
                    } else {
                        request?.deny()
                    }
                }
            }

            is InitializationViewUIEvent.OnGalleryResult -> {
                runCatching {
                    val chromeClient = state.chromeClient.chrome
                    chromeClient?.parseResult(event.uri, chromeClient.pathCallback)
                }
            }

            is InitializationViewUIEvent.OnCameraResult -> {
                runCatching {
                    if (event.result.resultCode == Activity.RESULT_OK) {
                        val chromeClient = state.chromeClient.chrome
                        chromeClient?.parseResult(
                            state.launchers.cameraImageUri,
                            chromeClient.pathCallback
                        )
                    }
                }
            }

            is InitializationViewUIEvent.UpdateWebView -> {
                state = state.copy(screen = event.value)
            }

            is InitializationViewUIEvent.UpdateFileChooserVisibility -> {

                state = state.copy(
                    launchers =
                    state.launchers.copy(showLauncherChooser = !state.launchers.showLauncherChooser)
                )
                if (event.resetValue)
                    state.chromeClient.pathCallback?.onReceiveValue(null)
                state.chromeClient.chrome?.pathCallback?.onReceiveValue(null)

            }

            is InitializationViewUIEvent.OnNavigateBack -> state =
                state.copy(lastBackPressed = event.time)

            is InitializationViewUIEvent.HandlePermission -> {
                state.launchers.permissionLauncher?.launch(Manifest.permission.CAMERA)
            }

            is InitializationViewUIEvent.SetUri -> state =
                state.copy(launchers = state.launchers.copy(cameraImageUri = event.uri))

            InitializationViewUIEvent.Open -> {
                state = state.copy(openGame = true)
            }
        }
    }

    private fun clearPermission() {
        state.chromeClient.chrome?.permissionRequest = null
    }
}

data class WebViewState(
    val isLoading: Boolean = true,
    val launchers: LauncherHolder = LauncherHolder(),
    val openGame: Boolean = false,
    val userId: String = "",
    val screen: WebView? = null,
    val isFirstLoad: Boolean = true,
    val chromeClient: ChromeHolder = ChromeHolder(),
    val lastBackPressed: Long = 0,
    val sharedPreferences: SharedPreferences? = null,
)

sealed class InitializationViewUIEvent {
    data object Load : InitializationViewUIEvent()
    data class OnPermissionResult(val granted: Boolean) : InitializationViewUIEvent()
    data class OnGalleryResult(val uri: Uri?) : InitializationViewUIEvent()
    data class OnCameraResult(val result: ActivityResult) : InitializationViewUIEvent()
    data class UpdateWebView(val value: WebView) : InitializationViewUIEvent()
    data class UpdateFileChooserVisibility(val resetValue: Boolean = false) :
        InitializationViewUIEvent()

    data class OnNavigateBack(val time: Long) : InitializationViewUIEvent()
    data class HandlePermission(val request: PermissionRequest?) : InitializationViewUIEvent()
    data class SetUri(val uri: Uri) : InitializationViewUIEvent()
    data object Open : InitializationViewUIEvent()
}

data class LauncherHolder(
    val showLauncherChooser: Boolean = false,
    val galleryLauncher: ManagedActivityResultLauncher<String, Uri?>? = null,
    val cameraLauncher: ManagedActivityResultLauncher<Intent, ActivityResult>? = null,
    val permissionLauncher: ManagedActivityResultLauncher<String, Boolean>? = null,
    val cameraImageUri: Uri? = null,
) {
    fun launchGallery() {
        galleryLauncher?.launch("image/*")
    }

    fun launchCamera(uri: Uri) {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)

        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        cameraLauncher?.launch(intent)
    }


}

data class ChromeHolder(
    val chrome: ExtWebChromeClient? = null,
    val pathCallback: ValueCallback<Array<Uri>>? = null,
    val fileChooserParams: WebChromeClient.FileChooserParams? = null,
)