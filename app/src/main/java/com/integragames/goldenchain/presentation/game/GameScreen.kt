package com.integragames.goldenchain.presentation.game

import android.app.Activity
import android.content.Context
import android.content.pm.ActivityInfo
import androidx.annotation.Keep
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.integragames.goldenchain.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
@Keep
@RootNavGraph
@Destination
@Composable
fun GameScreen(context: Context = LocalContext.current) {
    val viewModel: GameViewModel = viewModel()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    ActivityInfo.SCREEN_ORIENTATION_PORTRAIT.also {
        (context as? Activity)?.requestedOrientation = it
    }
    LaunchedEffect(true) {
        if (!state.isLoading) return@LaunchedEffect
        viewModel.load()
    }
    if (state.player.health <= 0 || state.enemy.health <= 0) {
        viewModel.load(false)
    }
    val infiniteTransition = rememberInfiniteTransition(label = "infinite transition")
    val animation by infiniteTransition.animateValue(
        initialValue = 0,
        targetValue = CardType.entries.size - 1,
        typeConverter = Int.VectorConverter,
        animationSpec = infiniteRepeatable(
            tween(state.animationDuration.speed),
            RepeatMode.Restart
        ),
        label = ""
    )

    Box(
        Modifier
            .fillMaxSize()
            .paint(painterResource(id = R.mipmap.bg), contentScale = ContentScale.Crop)
            .padding(8.dp)
    ) {

        Row(
            Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f)
                .align(Alignment.Center)
        ) {
            state.player.leftDeck.let {
                LeftCol(
                    animId = animation,
                    value = it,
                    onEvent = { viewModel.selectDeck(-1) }
                )
            }
            state.player.centerDeck.let {
                CenterCol(
                    animId = animation,
                    value = it,
                    onEvent = { viewModel.selectDeck(0) }
                )
            }
            state.player.rightDeck.let {
                RightCol(
                    animId = animation,
                    value = it,
                    onEvent = { viewModel.selectDeck(1) }
                )
            }
        }
        Image(
            painterResource(id = R.mipmap.char2),
            contentDescription = "Enemy character image",
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .align(Alignment.TopEnd)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .paint(ColorPainter(Color.Black.copy(alpha = 0.5f)))
                .paint(painterResource(id = R.mipmap.glow), contentScale = ContentScale.Crop)
                .padding(4.dp),
            contentScale = ContentScale.FillHeight
        )
        Column(
            modifier = Modifier
                .fillMaxHeight(0.3f)
                .align(Alignment.BottomStart)
                .clip(
                    RoundedCornerShape(16.dp)
                )
                .paint(ColorPainter(Color.Black.copy(alpha = 0.5f)))
                .paint(painterResource(id = R.mipmap.glow), contentScale = ContentScale.Crop)
                .padding(4.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight(0.15f)
                    .padding(4.dp)
                    .paint(
                        painterResource(id = R.mipmap.button),
                        contentScale = ContentScale.FillHeight
                    )
                    .clickable { viewModel.onFight() }
            ) {
                Text(
                    text = "Fight",
                    modifier = Modifier.align(Alignment.Center),
                    color = Color.White,
                    style = TextStyle(
                        fontSize = 16.sp,
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2.0f, 2.0f),
                            blurRadius = 2f
                        )
                    ),
                )
            }
            Image(
                painterResource(id = R.mipmap.char1),
                contentDescription = "Player character image",
                contentScale = ContentScale.FillHeight
            )

        }
        if (state.titleRes != 0) {
            Image(
                ColorPainter(Color.Black.copy(alpha = 0.5f)),
                contentDescription = "50% opacity black background color",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .paint(ColorPainter(Color.Black.copy(alpha = 0.5f)))
                    .padding(4.dp),
                contentScale = ContentScale.FillWidth
            )
            Image(
                painterResource(id = state.titleRes),
                contentDescription = "Title image",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .align(Alignment.Center)
                    .clip(
                        RoundedCornerShape(16.dp)
                    )
                    .paint(ColorPainter(Color.Black.copy(alpha = 0.5f)))
                    .padding(4.dp),
                contentScale = ContentScale.FillWidth
            )
        }
        HealthRow(state = state)
    }
}
@Keep
@Composable
fun RightCol(animId: Int, value: Int, onEvent: () -> Unit, color: Color = Color.Black.copy(alpha = 0.5f)) {
    val resources = remember { CardType.entries.shuffled() }
    val resId = resources[animId].resId
    Column(
        Modifier
            .fillMaxWidth()
            .padding(4.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .paint(ColorPainter(color))
            .paint(painterResource(id = R.mipmap.frame), contentScale = ContentScale.FillBounds)
            .clickable { onEvent() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(4.dp),
            painter = painterResource(id = resId),
            contentDescription = "Enemy tile choose animation"
        )
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .padding(4.dp),
            painter = painterResource(id = CardType.entries[value].resId),
            contentDescription = "Player selected tile"
        )
    }
}
@Keep
@Composable
fun CenterCol(animId: Int, value: Int, onEvent: () -> Unit, color: Color = Color.Black.copy(alpha = 0.5f)) {
    val resources = remember { CardType.entries.shuffled() }
    val resId = resources[animId].resId
    Column(
        Modifier
            .fillMaxWidth(0.56f)
            .padding(4.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .paint(ColorPainter(color))
            .paint(painterResource(id = R.mipmap.frame), contentScale = ContentScale.FillBounds)
            .clickable { onEvent() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(4.dp),
            painter = painterResource(id = resId),
            contentDescription = "Enemy tile choose animation"
        )
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .padding(4.dp),
            painter = painterResource(id = CardType.entries[value].resId),
            contentDescription = "Player selected tile"
        )

    }
}
@Keep
@Composable
fun LeftCol(animId: Int, value: Int, onEvent: () -> Unit, color: Color = Color.Black.copy(alpha = 0.5f)) {
    val resources = remember { CardType.entries.shuffled() }
    val resId = resources[animId].resId
    Column(
        Modifier
            .fillMaxWidth(0.3f)
            .padding(4.dp)
            .clip(
                RoundedCornerShape(16.dp)
            )
            .paint(ColorPainter(color))
            .paint(painterResource(id = R.mipmap.frame), contentScale = ContentScale.FillBounds)
            .clickable { onEvent() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Image(
            modifier = Modifier
                .fillMaxHeight(0.5f)
                .padding(4.dp),
            painter = painterResource(id = resId),
            contentDescription = "Enemy tile choose animation"
        )
        Image(
            modifier = Modifier
                .fillMaxHeight()
                .padding(4.dp),
            painter = painterResource(id = CardType.entries[value].resId),
            contentDescription = "Player selected tile"
        )

    }
}
@Keep
@Composable
fun HealthRow(state: GameState) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(64.dp)
            .padding(4.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
                .align(Alignment.CenterStart),
        ) {
            Row(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight(0.8f)
                    .paint(
                        painter = painterResource(id = R.mipmap.button),
                        contentScale = ContentScale.FillBounds,
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    modifier = Modifier
                        .width(64.dp)
                        .fillMaxHeight(),
                    painter = painterResource(id = R.mipmap.char1),
                    contentDescription = "Player avatar near health bar",
                    contentScale = ContentScale.FillHeight
                )
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    text = "HP: ${state.player.health}",
                    color = Color.White,
                    textAlign = TextAlign.Left,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2.0f, 2.0f),
                            blurRadius = 2f
                        )
                    )
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(150.dp)
                    .fillMaxWidth()
            ) {
                StripedProgressIndicator(
                    progress = state.player.health.toFloat(),
                    maxValue = 100f,
                    stripeColor = Color.Green,
                    stripeColorSecondary = Color.Green,
                )
            }
        }
        Column(
            modifier = Modifier
                .width(100.dp)
                .fillMaxHeight()
                .align(Alignment.CenterEnd),
        ) {
            Row(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight(0.8f)
                    .paint(
                        painter = painterResource(id = R.mipmap.button),
                        contentScale = ContentScale.FillBounds,
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    modifier = Modifier
                        .fillMaxWidth(0.5f),
                    textAlign = TextAlign.Center,
                    text = "HP: \n${state.enemy.health}",
                    color = Color.White,
                    style = TextStyle(
                        shadow = Shadow(
                            color = Color.Black,
                            offset = Offset(2.0f, 2.0f),
                            blurRadius = 2f
                        )
                    )
                )
                Image(
                    modifier = Modifier
                        .width(64.dp)
                        .fillMaxHeight()
                        .scale(-1.0f, 1.0f),
                    painter = painterResource(id = R.mipmap.char2),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = "Enemy avatar near enemy health bar"
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .width(100.dp)
                    .fillMaxWidth()
            ) {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    StripedProgressIndicator(
                        progress = state.enemy.health.toFloat(),
                        maxValue = 100f
                    )
                }
            }
        }
    }

}
@Keep
@Composable
private fun createStripeBrush(
    stripeColor: Color,
    stripeBg: Color,
    stripeWidth: Dp,
): Brush {
    val stripeWidthPx = with(LocalDensity.current) { stripeWidth.toPx() }
    val brushSizePx = 2 * stripeWidthPx
    val stripeStart = stripeWidthPx / brushSizePx

    return Brush.linearGradient(
        stripeStart to stripeBg,
        stripeStart to stripeColor,
        start = Offset(0f, 0f),
        end = Offset(brushSizePx, brushSizePx),
        tileMode = TileMode.Repeated
    )
}
@Keep
@Composable
fun BoxScope.StripedProgressIndicator(
    modifier: Modifier = Modifier,
    progress: Float,
    maxValue: Float,
    stripeColor: Color = Color.Green,
    stripeColorSecondary: Color = Color.Green,
    backgroundColor: Color = Color.White.copy(alpha = 0.5f),
    clipShape: Shape = RoundedCornerShape(16.dp),
) {
    Box(
        modifier = modifier
            .clip(clipShape)
            .align(Alignment.BottomCenter)
            .background(backgroundColor)
            .fillMaxWidth(0.8f)
            .height(10.dp)
    ) {
        Box(
            modifier = Modifier
                .clip(clipShape)
                .background(createStripeBrush(stripeColor, stripeColorSecondary, 5.dp))
                .fillMaxHeight()
                .fillMaxWidth(progress / maxValue)
        )
    }
}

