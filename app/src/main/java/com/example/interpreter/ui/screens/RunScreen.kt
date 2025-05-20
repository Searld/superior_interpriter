package com.example.interpreter.ui.screens

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.interpreter.R
import com.example.interpreter.components.BottomMenu
import com.example.interpreter.components.SidePanel
import com.example.interpreter.components.WorkArea
import com.example.interpreter.model.Block
import com.example.interpreter.model.Value
import com.example.interpreter.model.Variable
import com.example.interpreter.ui.components.TopMenu
import com.example.interpreter.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@Composable
fun RunScreen(viewModel: MainViewModel) {
    val imageOffsetX = remember { Animatable(-10f) }
    val alpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        launch {
            val listOfCommands = viewModel.blocks.mapNotNull { block ->
                when (block) {
                    is Block.VariableBlock -> block.command
                    is Block.AssignmentBlock -> {
                        if (block.left != null && block.left is Variable && block.right != null) {
                            "assign " + (block.left as Variable).name + " " +
                                    when (block.right) {
                                        is Variable -> (block.right as Variable).name
                                        is Value ->(block.right as Value).value.toString()
                                        else -> ""
                                    }
                        } else {
                            null
                        }
                    }
                }
            }
            viewModel.executeSource(listOfCommands)
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
    Box(modifier = Modifier.fillMaxSize().background(Color(5, 5, 5))) {
        Image(
            painter = painterResource(id = R.drawable.anime_run),
            contentDescription = "Background Image",
            modifier = Modifier.alpha(alpha.value).width(400.dp)
                .fillMaxHeight()
                .offset(imageOffsetX.value.dp,100.dp)
        )
            Text(
                text = viewModel.output,
                fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold)),
                fontSize = 18.sp,
                color = Color.White,
                modifier = Modifier.padding(10.dp,15.dp)
            )
    }
}
