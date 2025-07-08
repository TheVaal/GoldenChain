package com.integragames.goldenchain.presentation.about

import android.Manifest
import android.content.Context
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.FileProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.integragames.goldenchain.BuildConfig
import com.integragames.goldenchain.R
import com.integragames.goldenchain.data.util.web.createFileForCameraImageFlaming
import com.integragames.goldenchain.data.util.web.getChromeClient
import com.integragames.goldenchain.data.util.web.rememberWebView
import com.integragames.goldenchain.presentation.destinations.GDPRScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.navigate

@RootNavGraph
@Destination
@Composable
fun InitializationScreen(
    data: String,
    userId: String,
    context: Context = LocalContext.current,
    navigator: NavController,
) {
    val viewModel: WebViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val permissionLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) {
            viewModel.onEvent(InitializationViewUIEvent.OnPermissionResult(it))
        }
    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) {
        viewModel.onEvent(InitializationViewUIEvent.OnGalleryResult(it))
    }
    val cameraLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            viewModel.onEvent(InitializationViewUIEvent.OnCameraResult(result))
        }

    val chromeClient =
        getChromeClient(
            onEvent = viewModel::onEvent
        )
    val screen = rememberWebView(
        chromeClient = chromeClient,
        onEvent = viewModel::onEvent
    )
    LaunchedEffect(true) {
        if (!state.isLoading) return@LaunchedEffect
        viewModel.load(
            screen,
            data,
            userId,
            LauncherHolder(
                galleryLauncher = galleryLauncher,
                cameraLauncher = cameraLauncher,
                permissionLauncher = permissionLauncher
            ),
            chrome = ChromeHolder(chrome = chromeClient),
            sharedPreferences = context.getSharedPreferences(
                BuildConfig.APPLICATION_ID,
                Context.MODE_PRIVATE
            )
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.mipmap.bg),
                contentScale = ContentScale.FillBounds
            ),
    ) {

        WebPageView(data, state, viewModel::onEvent)
    }


}

@Composable
private fun WebPageView(
    link: String,
    state: WebViewState,
    onEvent: (InitializationViewUIEvent) -> Unit,
) {

    if (state.screen != null) {
        AndroidView(
            factory = {
                state.screen.apply {
                    if (state.isFirstLoad) loadUrl(link)
                }
            },
        ) { vw ->
            onEvent(InitializationViewUIEvent.UpdateWebView(vw))
        }
    }
    if (state.launchers.showLauncherChooser) {
        FileChooser(
            state = state,
            onEvent = onEvent
        )
    }
    BackHandler {
        if (state.launchers.showLauncherChooser) {
            onEvent(InitializationViewUIEvent.UpdateFileChooserVisibility(true))
            return@BackHandler
        }
        if (state.screen?.canGoBack() == true) {
            state.screen.goBack()
        }

        return@BackHandler
    }

}

@Composable
fun FileChooser(
    state: WebViewState,
    context: Context = LocalContext.current,
    onEvent: (InitializationViewUIEvent) -> Unit,
) {
    LaunchedEffect(true) {
        state.launchers.permissionLauncher?.launch(Manifest.permission.CAMERA)
    }
    Column(
        modifier = Modifier.paint(painter = ColorPainter(color = Color.Black.copy(alpha = 0.5f))),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedCard(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.3f)
        ) {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Camera")
                Button(
                    onClick = {
                        val file = context.createFileForCameraImageFlaming()
                        val cameraImageUri =
                            FileProvider.getUriForFile(
                                context,
                                "${context.packageName}${".provider"}",
                                file
                            )
                        onEvent(
                            InitializationViewUIEvent.SetUri(
                                cameraImageUri
                            )
                        )
                        state.launchers.launchCamera(cameraImageUri)
                    }
                ) {
                    Text(text = "Open")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Gallery")
                Button(
                    onClick = {
                        state.launchers.launchGallery()
                    }
                ) {
                    Text(text = "Open")
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }

}
