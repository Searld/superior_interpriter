package com.example.interpreter.components

import AssignArrBlock
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.interpreter.R
import com.example.interpreter.model.Array
import com.example.interpreter.model.Block
import com.example.interpreter.model.Value
import com.example.interpreter.ui.components.ConditionBlock
import com.example.interpreter.ui.components.PrintBlock
import com.example.interpreter.ui.components.WhileBlock
import com.example.interpreter.viewmodel.MainViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.min
import kotlin.math.roundToInt

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
                SwipeToDeleteBlock(block = block, viewModel = viewModel)
                {
                    when(block)
                    {
                        is Block.ConditionBlock -> ConditionBlock(block, viewModel)
                        is Block.EndifBlock -> EndifBlock()
                        is Block.AssignmentBlock -> AssignmentBlock(block, viewModel)
                        is Block.VariableBlock -> InitializeVarBlock(block.variable.name)
                        is Block.PrintBlock -> PrintBlock(viewModel, block)
                        is Block.CreatingArrayBlock -> CreateArrayBlock(block)
                        is Block.AssignArrBlock -> AssignArrBlock(block, viewModel)
                        is Block.WhileBlock -> WhileBlock(block, viewModel)
                    }
                }

            }

        }
    }
}
@Composable
fun SwipeToDeleteBlock(
    block: Block,
    viewModel: MainViewModel,
    content: @Composable () -> Unit
) {
    var offsetX by remember { mutableStateOf(0f) }
    val swipeThreshold = LocalConfiguration.current.screenWidthDp * 0.3f
    val animatedOffset by animateFloatAsState(
        targetValue = offsetX,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        )
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(animatedOffset.roundToInt(), 0) }
            .pointerInput(block.id) {
                detectHorizontalDragGestures(
                    onDragEnd = {
                        if (offsetX < -swipeThreshold) {
                            viewModel.removeBlock(block.id)
                            offsetX = 0f
                        } else {
                            offsetX = 0f
                        }
                    },
                    onHorizontalDrag = { _, dragAmount ->
                        offsetX = (offsetX + dragAmount)
                            .coerceIn(-swipeThreshold * 2, 0f)
                    }
                )
            },
        contentAlignment = Alignment.Center
    ) {
        if (offsetX < 0) {
            Box(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(start = 50.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Удалить",
                    tint = Color(59, 160, 255).copy(
                        alpha = min(1f, abs(offsetX) / swipeThreshold)
                    ),
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        content()
    }
}