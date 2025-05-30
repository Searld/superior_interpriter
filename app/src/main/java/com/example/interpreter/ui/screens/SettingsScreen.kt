package com.example.interpreter.ui.screens

import Utils.Configs
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.interpreter.R
import com.example.interpreter.model.Block
import com.example.interpreter.model.MapperForSerialization
import com.example.interpreter.viewmodel.MainViewModel
import kotlinx.coroutines.launch
import java.nio.file.WatchEvent

@Composable
fun SettingsScreen(viewModel: MainViewModel) {
    val imageOffsetX = remember { Animatable(-100f) }
    val alpha = remember { Animatable(0f) }
    var context = LocalContext.current
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
        Column{
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var nameForSave by remember { mutableStateOf("") }

                BasicTextField(
                    value = nameForSave,
                    onValueChange = { nameForSave = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.lato))
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp)
                        .height(45.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(15.dp))
                        .background(Color(70, 106, 140).copy(alpha = 0.15f), RoundedCornerShape(15.dp)),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            if (nameForSave.isEmpty()) {
                                Text(
                                    text = "Enter name",
                                    color = Color.LightGray,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily(Font(R.font.lato))
                                )
                            }
                            innerTextField()
                        }
                    }
                )

                Button(
                    onClick = {
                        val listOfCommands = viewModel.blocks.mapNotNull { block ->
                            if(block is Block.PrintBlock)
                                "print ${block.variable?.name}"
                            else
                                block.command()
                        }
                        Configs.export(context,listOfCommands,nameForSave)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(40.dp),
                    contentPadding = PaddingValues(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(59, 160, 255), Color(121, 59, 255))
                                ),
                                shape = RoundedCornerShape(40.dp)
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Save",
                            fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold)),
                            fontSize = 19.sp
                        )
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 25.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                var nameForLoad by remember { mutableStateOf("") }
                var expanded by remember { mutableStateOf(false) }
                var algorithms = remember { mutableStateListOf<String>() }
                var selectedAlgo by remember { mutableStateOf("") }
                BasicTextField(
                    value = if(!selectedAlgo.isEmpty()) selectedAlgo else "Select algo",
                    readOnly = true,
                    onValueChange = { nameForLoad = it },
                    singleLine = true,
                    textStyle = TextStyle(
                        color = Color.White,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center,
                        fontFamily = FontFamily(Font(R.font.lato))
                    ),
                    cursorBrush = SolidColor(Color.White),
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp)
                        .height(45.dp)
                        .border(1.dp, Color.White, RoundedCornerShape(15.dp))
                        .background(Color(70, 106, 140).copy(alpha = 0.15f), RoundedCornerShape(15.dp)),
                    decorationBox = { innerTextField ->
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 8.dp)
                                .clickable{

                                    algorithms.addAll(Configs.getSavedFileNames(context))
                                    expanded = true
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            innerTextField()
                        }
                    }
                )
                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier
                        .background(Color(15, 24, 36))
                        .width(IntrinsicSize.Min)
                ) {
                    algorithms.forEach { algo ->
                        DropdownMenuItem(
                            text = { Text(text = algo, color = Color.White) },
                            onClick = {
                                selectedAlgo = algo
                                expanded = false
                            }
                        )
                    }
                }
                Button(
                    onClick = {
                        viewModel.loadBlocks(Configs.import(context, selectedAlgo ))
                    },
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 15.dp)
                        .height(45.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Transparent,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(40.dp),
                    contentPadding = PaddingValues(),
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = Brush.horizontalGradient(
                                    colors = listOf(Color(59, 160, 255), Color(121, 59, 255))
                                ),
                                shape = RoundedCornerShape(40.dp)
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Load",
                            fontFamily = FontFamily(Font(R.font.lato, FontWeight.Bold)),
                            fontSize = 19.sp
                        )
                    }
                }
            }
        }

    }
}
