package com.example.interpreter.ui.screens

import Utils.Exceptions
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.example.interpreter.R
import com.example.interpreter.model.Block
import com.example.interpreter.model.Value
import com.example.interpreter.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@Composable
fun RunScreen(viewModel: MainViewModel) {
    val imageOffsetX = remember { Animatable(-10f) }
    val alpha = remember { Animatable(0f) }
    var text = remember { mutableStateOf<String>("") }
    var errorMessage by remember { mutableStateOf("") }
    LaunchedEffect(Unit) {
        launch {
            val listOfCommands = viewModel.blocks.mapNotNull { block ->
                block.command()
            }
            viewModel.executeSource(listOfCommands)
            { error ->
                Exceptions.handleException(error)
            }
            viewModel.blocks.forEach { block ->
                if (block is Block.PrintBlock)
                    text.value +=
                        "${block.variable?.name} = ${viewModel.output?.env[block.variable?.name].toString()}\n"


            }
        }
        launch {
            imageOffsetX.animateTo(
                targetValue = -100f,
                animationSpec = tween(
                    durationMillis = 500,
                    easing = LinearOutSlowInEasing
                )
            )
        }
        launch {
            alpha.animateTo(
                targetValue = 0.08f,
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
            painter = painterResource(id = R.drawable.anime_run),
            contentDescription = "Background Image",
            modifier = Modifier.alpha(alpha.value).width(400.dp)
                .fillMaxHeight()
                .offset(imageOffsetX.value.dp,100.dp)
        )
            Text(
                text = text.value,
                fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold)),
                fontSize = 25.sp,
                color = Color.White,
                modifier = Modifier.padding(10.dp,30.dp)
            )
    }
}
