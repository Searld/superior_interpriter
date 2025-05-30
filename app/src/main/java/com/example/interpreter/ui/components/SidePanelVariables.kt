package com.example.interpreter.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.ui.window.DialogProperties
import com.example.interpreter.viewmodel.MainViewModel

@Composable
fun SidePanelVariables(viewModel: MainViewModel) {
    val panelWidthDp = 300.dp
    val density = LocalDensity.current
    val panelWidthPx = with(density) { panelWidthDp.toPx() }

    val slideAnim = remember { Animatable(-panelWidthPx) }
    val backgroundAlpha = remember { Animatable(0f) }

    var showArrayDialog by remember { mutableStateOf(false) }
    var showVarDialog by remember { mutableStateOf(false) }
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
                onClick = { showVarDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
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
                        "Variable",
                        fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold))
                    )
                }
            }
            Button(
                onClick = { showArrayDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
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
                        "Array",
                        fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold))
                    )
                }
            }

            Button(
                onClick = { viewModel.addAssignArrayBlock()},
                modifier = Modifier.fillMaxWidth()
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
                        "Assign array",
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
                "Variables and arrays:",
                fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold)),
                fontSize = 26.sp,
                color = Color.LightGray,
                modifier = Modifier.padding(10.dp, 30.dp, 10.dp, 0.dp)
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
            viewModel.arrays.forEach { arr ->
                val name = arr.name
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(60.dp)
                        .padding(10.dp,20.dp,10.dp,0.dp)
                        .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(8.dp))
                        .background(Color.Transparent)
                        .clickable {
                            viewModel.insertIntoSelectedSlot(arr)
                            viewModel.onItemSelected(null)},
                    contentAlignment = Alignment.Center
                )
                {
                    Text(
                        "$name[]",
                        color = Color.White,
                        fontSize = 11.sp
                    )
                }
            }
            if(showVarDialog)
                CreatingDialog("Create variable","Enter name:",
                    {showVarDialog = false}, viewModel,"Var")
            if(showArrayDialog)
                CreatingDialog("Create array","Enter name:",
                    {showArrayDialog = false}, viewModel, "Array")
        }
    }
}
@Composable
fun CreatingDialog(title: String, desc: String,
                   onClose: () -> Unit,
                   viewModel: MainViewModel,
                   type: String)
{

        var name by remember { mutableStateOf("") }

        AlertDialog(
            modifier = Modifier.border(
                BorderStroke(1.dp, Color.White.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(25.dp)
            ),
            containerColor =Color(5, 5, 5).copy(alpha = 0.95f),
            properties = DialogProperties(
                dismissOnBackPress = true,
                dismissOnClickOutside = true
            ),
            onDismissRequest = { onClose() },
            title = { Text(text = title, color = Color.White) },
            text = {
                Column {
                    Text(text = desc, color = Color.White)
                    TextField(
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        modifier = Modifier.padding(0.dp,10.dp),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = Color.White.copy(alpha = 0.09f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.09f),
                            focusedIndicatorColor = Color.White,
                            unfocusedIndicatorColor = Color.Transparent,
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White
                        )
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    if(type == "Array")
                        viewModel.addCreatingArrayBlock(name, "0")
                    else
                        viewModel.addVariable(name)

                    onClose()
                },
                    modifier = Modifier.background(
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
                    )) {
                    Text("Create")
                }
            },
            dismissButton = {
                Button(onClick = {onClose() },
                    modifier = Modifier.border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(40.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    )) {
                    Text("Cancel")
                }
            }
        )

}
