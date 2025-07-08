package com.integragames.goldenchain.presentation.home

import android.app.Activity
import android.os.Process
import androidx.activity.compose.BackHandler
import androidx.annotation.Keep
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.integragames.goldenchain.R
import com.integragames.goldenchain.presentation.destinations.GameScreenDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.navigate
@Keep
@RootNavGraph
@Destination
@Composable
fun HomeScreen(navigator: NavController) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .paint(
                painter = painterResource(id = R.mipmap.bg),
                contentScale = ContentScale.Crop
            )
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(id = R.mipmap.char1),
            contentDescription = "man image decoration",
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .paint(
                        painter = painterResource(id = R.mipmap.frame2),
                        contentScale = ContentScale.FillBounds
                    )
                    .padding(16.dp)
            ) {
                Image(
                    modifier = Modifier.align(Alignment.Center),
                    contentScale = ContentScale.FillWidth,
                    painter = painterResource(id = R.mipmap.tut),
                    contentDescription = "Quick how to play image"
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Box(modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(48.dp)
                .paint(
                    painter = painterResource(id = R.mipmap.button),
                    contentScale = ContentScale.FillHeight
                )
                .clickable {
                    navigator.navigate(GameScreenDestination)
                }) {
                Text(
                    "Play",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 16.sp, shadow = Shadow(
                            color = Color.Black, offset = Offset(2.0f, 2.0f), blurRadius = 2f
                        )
                    )
                )

            }
            Spacer(modifier = Modifier.height(16.dp))
            val activity = (LocalContext.current as? Activity)
            Box(modifier = Modifier
                .fillMaxWidth(0.4f)
                .height(48.dp)
                .paint(
                    painter = painterResource(id = R.mipmap.button),
                    contentScale = ContentScale.FillHeight
                )
                .clickable {
                    Process.killProcess(Process.myPid())
                    activity?.finishAffinity()
                }) {
                Text(
                    "Exit",
                    color = Color.White,
                    modifier = Modifier.align(Alignment.Center),
                    style = TextStyle(
                        fontSize = 16.sp, shadow = Shadow(
                            color = Color.Black, offset = Offset(2.0f, 2.0f), blurRadius = 2f
                        )
                    ),
                )

            }
        }
    }
    BackHandler{}
}