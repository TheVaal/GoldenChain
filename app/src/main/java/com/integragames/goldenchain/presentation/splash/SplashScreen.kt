package com.integragames.goldenchain.presentation.splash

import android.content.Context
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.paint
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.integragames.goldenchain.BuildConfig
import com.integragames.goldenchain.R
import com.integragames.goldenchain.data.repository.GAME_ID
import com.integragames.goldenchain.data.repository.GameManager
import com.integragames.goldenchain.presentation.destinations.GDPRScreenDestination
import com.integragames.goldenchain.presentation.destinations.HomeScreenDestination
import com.integragames.goldenchain.presentation.destinations.InitializationScreenDestination
import com.integragames.goldenchain.presentation.game.CardType
import com.integragames.goldenchain.presentation.game.CenterCol
import com.integragames.goldenchain.presentation.game.LeftCol
import com.integragames.goldenchain.presentation.game.RightCol
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.navigate
import kotlinx.coroutines.delay

@RootNavGraph(start = true)
@Destination
@Composable
fun SplashScreen(context: Context = LocalContext.current, navigator: NavController) {

    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .paint(painterResource(id = R.mipmap.bg), contentScale = ContentScale.Crop),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(0.5f),
                contentScale = ContentScale.FillHeight,
                painter = painterResource(id = R.mipmap.char1),
                contentDescription = "loading screen player Character image"
            )
            Image(
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.FillHeight,
                painter = painterResource(id = R.mipmap.char2),
                contentDescription = "loading screen enemy Character image"
            )

        }
        val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
        val animation by infiniteTransition.animateValue(
            initialValue = 0,
            targetValue = CardType.entries.size - 1,
            typeConverter = Int.VectorConverter,
            animationSpec = infiniteRepeatable(
                tween(3000),
                RepeatMode.Restart
            ),
            label = ""
        )
        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight()
                .align(Alignment.Center)
        ) {

            LeftCol(
                animId = animation,
                value = animation,
                onEvent = { },
                Color.Transparent
            )

            CenterCol(
                animId = animation,
                value = animation,
                onEvent = { },
                color = Color.Transparent
            )

            RightCol(
                animId = animation,
                value = animation,
                onEvent = { },
                color = Color.Transparent
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .align(Alignment.Center)
                .paint(
                    painterResource(id = R.mipmap.frame2),
                    contentScale = ContentScale.FillBounds
                )
                .padding(36.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            var link by remember{ mutableStateOf("")}
            TextField(
                value = link,
                label = { Text("Link") },
                onValueChange = {newData: String->
                    link = newData
                }
            )
            FilledTonalButton(
                onClick = {
                    navigator.navigate(InitializationScreenDestination(link, ""))
                }
            ) {
                Text("Open link")
            }

        }
    }

}

