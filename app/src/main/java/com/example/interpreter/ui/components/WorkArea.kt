package com.example.interpreter.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.interpreter.MainActivity
import com.example.interpreter.R
import com.example.interpreter.model.Block
import com.example.interpreter.model.Variable
import com.example.interpreter.ui.components.PrintBlock
import com.example.interpreter.viewmodel.MainViewModel
import kotlinx.coroutines.launch

@Composable
fun WorkArea(blocks: List<Block>, viewModel: MainViewModel) {
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
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.9f)
            .background(brush = Brush.radialGradient(
                colors = listOf(Color(15, 24, 36),Color(5, 5, 5)),
                center = Offset(500f, 500f),
                radius = 700f
            ))
            .pointerInput(Unit) {

            },
        contentAlignment = Alignment.Center
    )
    {
        Image(
            painter = painterResource(id = R.drawable.anime_background),
            contentDescription = "Background Image",
            modifier = Modifier.alpha(alpha.value).width(600.dp)
                .fillMaxHeight()
                .offset(imageOffsetX.value.dp,65.dp)
        )
        Column (
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally) {
            blocks.forEach { block ->
                when(block) {
                    is Block.VariableBlock -> InitializeVarBlock(block.variable.name)
                    is Block.AssignmentBlock -> AssignmentBlock(block, viewModel)
                }
            }

        }
    }
}