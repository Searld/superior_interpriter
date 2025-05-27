package com.example.interpreter.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.interpreter.R
import kotlinx.coroutines.launch

@Composable
fun SettingsScreen() {
    val imageOffsetX = remember { Animatable(-100f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            imageOffsetX.animateTo(
                targetValue = 105f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 0.25f,
                animationSpec = tween(
                    durationMillis = 600,
                    easing = LinearOutSlowInEasing
                )
            )
        }
    }
    Box(modifier = Modifier.fillMaxSize()
        .background(brush = Brush.radialGradient(
            colors = listOf(Color(15, 24, 36),Color(5, 5, 5)),
            center = Offset(500f, 1000f),
            radius = 700f
        ))) {
        Image(
            painter = painterResource(id = R.drawable.anime_settings),
            contentDescription = "Background Image",
            modifier = Modifier.alpha(alpha.value).width(600.dp)
                .fillMaxHeight()
                .offset(imageOffsetX.value.dp,120.dp)
        )
    }
}
