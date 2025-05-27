package com.example.interpreter.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.interpreter.R
import kotlinx.coroutines.launch
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.example.interpreter.viewmodel.MainViewModel

@Composable
fun SidePanelVariables(viewModel: MainViewModel) {
    val panelWidthDp = 300.dp
    val density = LocalDensity.current
    val panelWidthPx = with(density) { panelWidthDp.toPx() }

    val slideAnim = remember { Animatable(-panelWidthPx) }
    val backgroundAlpha = remember { Animatable(0f) }

    var showDialog by remember { mutableStateOf(false) }
    LaunchedEffect(viewModel.selectedItem.value) {
        if (viewModel.selectedItem.value == "Variables") {
            launch {
                slideAnim.animateTo(0f, animationSpec = tween(350, easing = FastOutSlowInEasing))
            }
            launch {
                backgroundAlpha.animateTo(0.4f, tween(350))
            }
        } else {
            launch {
                backgroundAlpha.animateTo(0f, tween(300))
            }
            launch {
                slideAnim.animateTo(-panelWidthPx, animationSpec = tween(300, easing = FastOutSlowInEasing))
            }
        }
    }

    if (backgroundAlpha.value > 0f) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = backgroundAlpha.value))
                .clickable { viewModel.onItemSelected(null) }
                .zIndex(1f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxHeight()
            .width(panelWidthDp)
            .offset { IntOffset(slideAnim.value.toInt(), 0) }
            .background(Color(9, 15, 23))
            .zIndex(2f),
        contentAlignment = Alignment.TopStart
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp, 25.dp, 10.dp, 0.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(59, 160, 255),
                                Color(121, 59, 255)
                            )
                        ),
                        shape = RoundedCornerShape(40.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(40.dp),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Create variable",
                        fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold))
                    )
                }
            }

            Button(
                onClick = { viewModel.addAssignmentBlock()},
                modifier = Modifier.fillMaxWidth()
                    .padding(10.dp, 15.dp, 10.dp, 0.dp)
                    .background(
                        brush = Brush.horizontalGradient(
                            colors = listOf(
                                Color(59, 160, 255),
                                Color(121, 59, 255)
                            )
                        ),
                        shape = RoundedCornerShape(40.dp)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(40.dp),
                contentPadding = PaddingValues()
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "Assign var",
                        fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold))
                    )
                }
            }
            Text(
                "Variables:",
                fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold)),
                fontSize = 26.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(10.dp, 20.dp, 10.dp, 0.dp)
            )

            viewModel.variables.forEach { variable ->
                val varName = variable.name
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(10.dp,20.dp,10.dp,0.dp)
                        .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                        .background(Color.Transparent)
                        .clickable { viewModel.insertIntoSelectedSlot(variable)
                                     viewModel.onItemSelected(null)},
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        "$varName",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }

        }
    }
    if (showDialog) {
        var variableName by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("New Variable") },
            text = {
                Column {
                    Text("Enter variable name:")
                    TextField(
                        value = variableName,
                        onValueChange = { variableName = it },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.addVariable(variableName)
                    showDialog = false
                }) {
                    Text("Create")
                }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }
}
