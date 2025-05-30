package com.example.interpreter.ui.screens

import android.window.SplashScreen
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.example.interpreter.R
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

@Composable
fun SplashScreen(onFinish: () -> Unit) {
    val screenWidthDp =  LocalConfiguration.current.screenWidthDp.toFloat()
    val leftOffset = remember { Animatable(-300f) }
    val rightOffset = remember { Animatable(300f) }

    val leftScaleX = remember { Animatable(2f) }
    val rightScaleX = remember { Animatable(2f) }

    LaunchedEffect(Unit) {
        coroutineScope {
            launch {
                delay(1000)
                onFinish()
            }
            launch {
                leftOffset.animateTo(0f, tween(600, easing = FastOutSlowInEasing))
                delay(200)
                leftOffset.animateTo(1200f, tween(400))
            }

            launch {
                rightOffset.animateTo(0f, tween(600))
                delay(200)
                rightOffset.animateTo(-1200f, tween(400))
            }
            launch {
                leftScaleX.animateTo(1f, tween(700))
                delay(200)
                leftScaleX.animateTo(2.2f, tween(400))
            }

            launch {
                rightScaleX.animateTo(1f, tween(700))
                delay(200)
                rightScaleX.animateTo(2.2f, tween(400))
            }
        }

    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(5,5,5)),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Searld",
            fontSize = 40.sp,
            fontFamily = FontFamily(Font(R.font.lato)),
            modifier = Modifier
                .graphicsLayer {
                    translationX = leftOffset.value
                    scaleX = leftScaleX.value
                }
                .offset(y= -20.dp),
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(Color(59, 160, 255), Color.White)
                ),
            )
        )
        Text(
            text = "krauveiss",
            fontFamily = FontFamily(Font(R.font.lato)),
            color = Color.White,
            modifier = Modifier
                .graphicsLayer {
                    translationX = rightOffset.value
                    scaleX = rightScaleX.value
                }
                .offset( y = 40.dp),
            style = TextStyle(
                brush = Brush.linearGradient(
                    colors = listOf(Color(59, 160, 255), Color.White)
                ),
                fontSize = 40.sp
            )
        )
    }
}
